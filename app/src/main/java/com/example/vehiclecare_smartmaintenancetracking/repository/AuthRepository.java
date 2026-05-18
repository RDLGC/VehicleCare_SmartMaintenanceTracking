package com.example.vehiclecare_smartmaintenancetracking.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.vehiclecare_smartmaintenancetracking.database.UserDao;
import com.example.vehiclecare_smartmaintenancetracking.models.UserEntity;
import com.example.vehiclecare_smartmaintenancetracking.network.SupabaseApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
        
        java.util.List<java.util.Map<String, Object>> profileList = new java.util.ArrayList<>();
        profileList.add(profileData);
        
        supabaseApi.createProfile(supabaseKey, "Bearer " + supabaseKey, "application/json", "return=minimal", profileList)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        loadingLiveData.setValue(false);
                        if (response.isSuccessful() || response.code() == 201) {
                            UserEntity localUser = new UserEntity(uid, name, email, null, System.currentTimeMillis());
                            saveUserLocally(localUser);
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

    private void fetchSupabaseProfile(String uid, String supabaseKey, AuthCallback callback) {
        // Implementation for fetching profile will go here
        loadingLiveData.setValue(false);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        userLiveData.setValue(user);
        if (user != null) {
            callback.onSuccess("Welcome " + user.getEmail());
        }
    }

    private void saveUserLocally(UserEntity user) {
        executorService.execute(() -> userDao.insertUser(user));
    }

    public LiveData<FirebaseUser> getUserLiveData() { return userLiveData; }
    public LiveData<String> getErrorLiveData() { return errorLiveData; }
    public LiveData<Boolean> getLoadingLiveData() { return loadingLiveData; }

    public void signOut() {
        firebaseAuth.signOut();
        executorService.execute(userDao::clearAll);
        userLiveData.setValue(null);
    }
}
