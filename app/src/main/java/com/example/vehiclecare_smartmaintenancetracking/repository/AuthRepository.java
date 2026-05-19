package com.example.vehiclecare_smartmaintenancetracking.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.vehiclecare_smartmaintenancetracking.database.UserDao;
import com.example.vehiclecare_smartmaintenancetracking.models.UserEntity;
import com.example.vehiclecare_smartmaintenancetracking.network.SupabaseApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final FirebaseAuth firebaseAuth;
    private final SupabaseApi supabaseApi;
    private final UserDao userDao;
    private final ExecutorService executorService;

    private final MutableLiveData<FirebaseUser> userLiveData;
    private final MutableLiveData<String> errorLiveData;
    private final MutableLiveData<Boolean> loadingLiveData;

    public AuthRepository(SupabaseApi supabaseApi, UserDao userDao) {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.supabaseApi = supabaseApi;
        this.userDao = userDao;
        this.executorService = Executors.newSingleThreadExecutor();
        this.userLiveData = new MutableLiveData<>();
        this.errorLiveData = new MutableLiveData<>();
        this.loadingLiveData = new MutableLiveData<>();
        
        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.setValue(firebaseAuth.getCurrentUser());
        }
    }

    public interface AuthCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    public void signUp(String name, String email, String password, String supabaseKey, AuthCallback callback) {
        loadingLiveData.setValue(true);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        if (firebaseUser != null) {
                            createSupabaseProfile(firebaseUser.getUid(), name, email, supabaseKey, callback);
                        }
                    } else {
                        loadingLiveData.setValue(false);
                        String error = task.getException() != null ? task.getException().getMessage() : "Registration failed";
                        errorLiveData.setValue(error);
                        callback.onError(error);
                    }
                });
    }

    private void createSupabaseProfile(String uid, String name, String email, String supabaseKey, AuthCallback callback) {
        // Create a list containing one map (Supabase often prefers arrays for inserts)
        java.util.Map<String, Object> profileData = new java.util.HashMap<>();
        profileData.put("firebase_uid", uid);
        profileData.put("full_name", name);
        profileData.put("email", email);
        profileData.put("avatar_url", null);
        profileData.put("phone", null);
        profileData.put("location", null);
        
        java.util.List<java.util.Map<String, Object>> profileList = new java.util.ArrayList<>();
        profileList.add(profileData);
        
        supabaseApi.createProfile(supabaseKey, "Bearer " + supabaseKey, "application/json", "return=minimal", profileList)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        loadingLiveData.setValue(false);
                        if (response.isSuccessful() || response.code() == 201) {
                            UserEntity localUser = new UserEntity(uid, name, email, null, null, null, System.currentTimeMillis());
                            saveUserLocally(localUser);
                            updateFcmToken(uid, supabaseKey);
                            userLiveData.setValue(firebaseAuth.getCurrentUser());
                            callback.onSuccess("Account created! Welcome " + email);
                        } else {
                            StringBuilder sb = new StringBuilder("Sync Error: ");
                            sb.append(response.code());
                            try (okhttp3.ResponseBody errorBody = response.errorBody()) {
                                if (errorBody != null) {
                                    sb.append(" - ").append(errorBody.string());
                                }
                            } catch (Exception e) {
                                sb.append(" (Check Supabase RLS Policies)");
                            }
                            String msg = sb.toString();
                            errorLiveData.setValue(msg);
                            callback.onError(msg);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        loadingLiveData.setValue(false);
                        errorLiveData.setValue(t.getMessage());
                        callback.onError(t.getMessage());
                    }
                });
    }

    private void updateFcmToken(String uid, String supabaseKey) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String token = task.getResult();
                java.util.Map<String, Object> update = new java.util.HashMap<>();
                update.put("fcm_token", token);
                
                supabaseApi.updateProfile(supabaseKey, "Bearer " + supabaseKey, "application/json", "eq." + uid, update)
                        .enqueue(new Callback<Void>() {
                            @Override public void onResponse(Call<Void> call, Response<Void> response) {}
                            @Override public void onFailure(Call<Void> call, Throwable t) {}
                        });
            }
        });
    }

    public void signIn(String email, String password, String supabaseKey, AuthCallback callback) {
        loadingLiveData.setValue(true);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = task.getResult().getUser();
                        if (firebaseUser != null) {
                            fetchSupabaseProfile(firebaseUser.getUid(), supabaseKey, callback);
                        }
                    } else {
                        loadingLiveData.setValue(false);
                        String error = task.getException() != null ? task.getException().getMessage() : "Sign in failed";
                        errorLiveData.setValue(error);
                        callback.onError(error);
                    }
                });
    }

    public void fetchSupabaseProfile(String uid, String supabaseKey, AuthCallback callback) {
        loadingLiveData.setValue(true);
        supabaseApi.getProfile(supabaseKey, "Bearer " + supabaseKey, "eq." + uid)
                .enqueue(new Callback<java.util.List<UserEntity>>() {
                    @Override
                    public void onResponse(Call<java.util.List<UserEntity>> call, Response<java.util.List<UserEntity>> response) {
                        loadingLiveData.setValue(false);
                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                            UserEntity remoteUser = response.body().get(0);
                            remoteUser.setLastSync(System.currentTimeMillis());
                            saveUserLocally(remoteUser);
                            userLiveData.setValue(firebaseAuth.getCurrentUser());
                            if (callback != null) callback.onSuccess("Profile synced");
                        } else {
                            if (callback != null) callback.onError("Failed to sync profile");
                        }
                    }

                    @Override
                    public void onFailure(Call<java.util.List<UserEntity>> call, Throwable t) {
                        loadingLiveData.setValue(false);
                        if (callback != null) callback.onError(t.getMessage());
                    }
                });
    }

    public void updateProfile(UserEntity user, String supabaseKey, AuthCallback callback) {
        loadingLiveData.setValue(true);
        java.util.Map<String, Object> updates = new java.util.HashMap<>();
        updates.put("full_name", user.getFullName());
        updates.put("phone", user.getPhone());
        updates.put("location", user.getLocation());
        updates.put("avatar_url", user.getAvatarUrl());

        supabaseApi.updateProfile(supabaseKey, "Bearer " + supabaseKey, "application/json", "eq." + user.getFirebaseUid(), updates)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        loadingLiveData.setValue(false);
                        if (response.isSuccessful()) {
                            user.setLastSync(System.currentTimeMillis());
                            saveUserLocally(user);
                            if (callback != null) callback.onSuccess("Profile updated");
                        } else {
                            if (callback != null) callback.onError("Update failed: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        loadingLiveData.setValue(false);
                        if (callback != null) callback.onError(t.getMessage());
                    }
                });
    }

    private void saveUserLocally(UserEntity user) {
        executorService.execute(() -> userDao.insertUser(user));
    }

    public LiveData<FirebaseUser> getUserLiveData() { return userLiveData; }
    public LiveData<UserEntity> getLocalUserLiveData(String uid) { return userDao.getUserLiveData(uid); }
    public LiveData<String> getErrorLiveData() { return errorLiveData; }
    public LiveData<Boolean> getLoadingLiveData() { return loadingLiveData; }

    public void signOut() {
        firebaseAuth.signOut();
        executorService.execute(userDao::clearAll);
        userLiveData.setValue(null);
    }
}
