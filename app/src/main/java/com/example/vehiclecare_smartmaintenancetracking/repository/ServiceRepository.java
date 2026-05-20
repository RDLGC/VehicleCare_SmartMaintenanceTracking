package com.example.vehiclecare_smartmaintenancetracking.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.vehiclecare_smartmaintenancetracking.database.ServiceDao;
import com.example.vehiclecare_smartmaintenancetracking.models.ServiceEntity;
import com.example.vehiclecare_smartmaintenancetracking.network.SupabaseApi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceRepository {
    private final SupabaseApi supabaseApi;
    private final ServiceDao serviceDao;
    private final ExecutorService executorService;
    
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> addSuccessLiveData = new MutableLiveData<>();

    public ServiceRepository(SupabaseApi supabaseApi, ServiceDao serviceDao) {
        this.supabaseApi = supabaseApi;
        this.serviceDao = serviceDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<ServiceEntity>> getLocalServices() {
        return serviceDao.getAllServices();
    }

    public LiveData<List<ServiceEntity>> getServicesForVehicle(String vehicleId) {
        return serviceDao.getServicesForVehicle(vehicleId);
    }

    public LiveData<Double> getTotalSpent() {
        return serviceDao.getTotalSpent();
    }

    public void addService(String vehicleId, String type, String date, Integer mileage, Double cost, String provider, String notes, String supabaseKey) {
        loadingLiveData.setValue(true);
        addSuccessLiveData.setValue(false);
        
        String id = java.util.UUID.randomUUID().toString();
        String createdAt = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.US).format(new java.util.Date());
        ServiceEntity newService = new ServiceEntity(id, vehicleId, type, date, mileage, cost, provider, notes, createdAt);

        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("vehicle_id", vehicleId);
        data.put("service_type", type);
        data.put("service_date", date);
        data.put("mileage", mileage);
        data.put("cost", cost);
        data.put("provider", provider);
        data.put("notes", notes);
        
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(data);
        
        // Optimistic insert
        executorService.execute(() -> serviceDao.insertService(newService));
        
        supabaseApi.addService(supabaseKey, "Bearer " + supabaseKey, "application/json", "return=minimal", list)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        loadingLiveData.setValue(false);
                        if (response.isSuccessful() || response.code() == 201) {
                            addSuccessLiveData.setValue(true);
                            refreshAllServices(supabaseKey);
                        } else {
                            errorLiveData.setValue("Failed to log service: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        loadingLiveData.setValue(false);
                        errorLiveData.setValue(t.getMessage());
                    }
                });
    }

    public void refreshAllServices(String supabaseKey) {
        supabaseApi.getAllServices(supabaseKey, "Bearer " + supabaseKey)
                .enqueue(new Callback<List<ServiceEntity>>() {
                    @Override
                    public void onResponse(Call<List<ServiceEntity>> call, Response<List<ServiceEntity>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            executorService.execute(() -> serviceDao.insertAll(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ServiceEntity>> call, Throwable t) {
                        errorLiveData.setValue("Sync failed: " + t.getMessage());
                    }
                });
    }

    public void deleteService(String serviceId, String supabaseKey) {
        loadingLiveData.setValue(true);
        supabaseApi.deleteService(supabaseKey, "Bearer " + supabaseKey, "eq." + serviceId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        loadingLiveData.setValue(false);
                        if (response.isSuccessful()) {
                            executorService.execute(serviceDao::clearAll);
                        } else {
                            errorLiveData.setValue("Delete failed: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        loadingLiveData.setValue(false);
                        errorLiveData.setValue(t.getMessage());
                    }
                });
    }

    public LiveData<String> getError() { return errorLiveData; }
    public LiveData<Boolean> getLoading() { return loadingLiveData; }
    public LiveData<Boolean> getAddSuccess() { return addSuccessLiveData; }
}
