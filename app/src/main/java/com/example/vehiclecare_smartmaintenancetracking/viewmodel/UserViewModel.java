package com.example.vehiclecare_smartmaintenancetracking.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.vehiclecare_smartmaintenancetracking.database.AppDatabase;
import com.example.vehiclecare_smartmaintenancetracking.models.UserEntity;
import com.example.vehiclecare_smartmaintenancetracking.network.SupabaseApi;
import com.example.vehiclecare_smartmaintenancetracking.repository.AuthRepository;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<Boolean> updateSuccess = new MutableLiveData<>();

    public UserViewModel(@NonNull Application application) {
        super(application);
        
        String baseUrl = application.getString(com.example.vehiclecare_smartmaintenancetracking.R.string.supabase_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        SupabaseApi supabaseApi = retrofit.create(SupabaseApi.class);
        AppDatabase db = AppDatabase.getInstance(application);
        this.authRepository = new AuthRepository(supabaseApi, db.userDao());
    }

    public LiveData<UserEntity> getProfile(String uid) {
        return authRepository.getLocalUserLiveData(uid);
    }

    public void syncProfile(String uid, String supabaseKey) {
        authRepository.fetchSupabaseProfile(uid, supabaseKey, null);
    }

    public void updateProfile(UserEntity user, String supabaseKey) {
        authRepository.updateProfile(user, supabaseKey, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(String message) {
                updateSuccess.setValue(true);
            }

            @Override
            public void onError(String error) {
                updateSuccess.setValue(false);
            }
        });
    }

    public LiveData<Boolean> getUpdateSuccess() { return updateSuccess; }
    public LiveData<Boolean> isLoading() { return authRepository.getLoadingLiveData(); }
    public LiveData<String> getError() { return authRepository.getErrorLiveData(); }
}
