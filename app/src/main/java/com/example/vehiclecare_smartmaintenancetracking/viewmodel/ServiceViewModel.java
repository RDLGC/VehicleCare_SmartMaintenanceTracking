package com.example.vehiclecare_smartmaintenancetracking.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.vehiclecare_smartmaintenancetracking.database.AppDatabase;
import com.example.vehiclecare_smartmaintenancetracking.models.ServiceEntity;
import com.example.vehiclecare_smartmaintenancetracking.network.SupabaseApi;
import com.example.vehiclecare_smartmaintenancetracking.repository.ServiceRepository;
import java.util.List;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceViewModel extends AndroidViewModel {
    private final ServiceRepository serviceRepository;

    public ServiceViewModel(@NonNull Application application) {
        super(application);
        
        String baseUrl = application.getString(com.example.vehiclecare_smartmaintenancetracking.R.string.supabase_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        SupabaseApi api = retrofit.create(SupabaseApi.class);
        AppDatabase db = AppDatabase.getInstance(application);
        
        this.serviceRepository = new ServiceRepository(api, db.serviceDao());
    }

    public void addService(String vehicleId, String type, String date, Integer mileage, Double cost, String provider, String notes, String supabaseKey) {
        serviceRepository.addService(vehicleId, type, date, mileage, cost, provider, notes, supabaseKey);
    }

    public LiveData<List<ServiceEntity>> getServices() {
        return serviceRepository.getLocalServices();
    }

    public LiveData<Double> getTotalSpent() {
        return serviceRepository.getTotalSpent();
    }
    
    public void refreshServices(String supabaseKey) {
        serviceRepository.refreshAllServices(supabaseKey);
    }

    public LiveData<String> getError() { return serviceRepository.getError(); }
    public LiveData<Boolean> isLoading() { return serviceRepository.getLoading(); }
    public LiveData<Boolean> getAddSuccess() { return serviceRepository.getAddSuccess(); }
}
