package com.example.vehiclecare_smartmaintenancetracking.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.vehiclecare_smartmaintenancetracking.database.AppDatabase;
import com.example.vehiclecare_smartmaintenancetracking.repository.AuthRepository;
import com.example.vehiclecare_smartmaintenancetracking.network.SupabaseApi;
import com.google.firebase.auth.FirebaseUser;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        
        String baseUrl = application.getString(com.example.vehiclecare_smartmaintenancetracking.R.string.supabase_url);

        // Initialize Retrofit for Supabase
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        SupabaseApi supabaseApi = retrofit.create(SupabaseApi.class);
        AppDatabase db = AppDatabase.getInstance(application);
        
        this.authRepository = new AuthRepository(supabaseApi, db.userDao());
    }

    public void signUp(String name, String email, String password, String supabaseKey) {
        authRepository.signUp(name, email, password, supabaseKey, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(String message) {
                successMessage.setValue(message);
            }

            @Override
            public void onError(String error) {
                // error is already handled by repository's errorLiveData
            }
        });
    }

    public void signIn(String email, String password, String supabaseKey) {
        authRepository.signIn(email, password, supabaseKey, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(String message) {
                successMessage.setValue(message);
            }

            @Override
            public void onError(String error) {
                // error is already handled by repository's errorLiveData
            }
        });
    }

    public LiveData<FirebaseUser> getUser() { return authRepository.getUserLiveData(); }
    public LiveData<String> getError() { return authRepository.getErrorLiveData(); }
    public LiveData<Boolean> isLoading() { return authRepository.getLoadingLiveData(); }

    public LiveData<String> getSuccessMessage() { return successMessage; }

    public void setSuccessMessage(String message) {
        successMessage.setValue(message);
    }
}
