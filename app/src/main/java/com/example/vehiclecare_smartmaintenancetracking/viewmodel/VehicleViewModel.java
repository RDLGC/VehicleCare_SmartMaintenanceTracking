package com.example.vehiclecare_smartmaintenancetracking.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.vehiclecare_smartmaintenancetracking.database.AppDatabase;
import com.example.vehiclecare_smartmaintenancetracking.models.VehicleEntity;
import com.example.vehiclecare_smartmaintenancetracking.network.SupabaseApi;
import com.example.vehiclecare_smartmaintenancetracking.repository.VehicleRepository;
import java.util.List;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VehicleViewModel extends AndroidViewModel {
    private final VehicleRepository vehicleRepository;

    public VehicleViewModel(@NonNull Application application) {
        super(application);
        
        String baseUrl = application.getString(com.example.vehiclecare_smartmaintenancetracking.R.string.supabase_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        SupabaseApi api = retrofit.create(SupabaseApi.class);
        AppDatabase db = AppDatabase.getInstance(application);
        
        this.vehicleRepository = new VehicleRepository(api, db.vehicleDao());
    }

    public void addVehicle(String userId, String name, String type, Integer year, Integer mileage, String modelTrim, String supabaseKey) {
        vehicleRepository.addVehicle(userId, name, type, year, mileage, modelTrim, supabaseKey);
    }

    public LiveData<List<VehicleEntity>> getVehicles(String userId) {
        return vehicleRepository.getLocalVehicles(userId);
    }
    
    public void refreshVehicles(String userId, String supabaseKey) {
        vehicleRepository.refreshVehicles(userId, supabaseKey);
    }

    public LiveData<String> getError() { return vehicleRepository.getError(); }
    public LiveData<Boolean> isLoading() { return vehicleRepository.getLoading(); }
}
