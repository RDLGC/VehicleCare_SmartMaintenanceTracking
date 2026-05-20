package com.example.vehiclecare_smartmaintenancetracking.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.vehiclecare_smartmaintenancetracking.database.VehicleDao;
import com.example.vehiclecare_smartmaintenancetracking.models.VehicleEntity;
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

public class VehicleRepository {
    private final SupabaseApi supabaseApi;
    private final VehicleDao vehicleDao;
    private final ExecutorService executorService;
    
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> addSuccessLiveData = new MutableLiveData<>();

    public VehicleRepository(SupabaseApi supabaseApi, VehicleDao vehicleDao) {
        this.supabaseApi = supabaseApi;
        this.vehicleDao = vehicleDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<VehicleEntity>> getLocalVehicles(String userId) {
        return vehicleDao.getVehiclesForUser(userId);
    }

    public LiveData<VehicleEntity> getVehicleById(String id) {
        return vehicleDao.getVehicleById(id);
    }

    public void addVehicle(String userId, String name, String type, Integer year, Integer mileage, String modelTrim, String supabaseKey) {
        loadingLiveData.setValue(true);
        addSuccessLiveData.setValue(false);
        
        String tempId = java.util.UUID.randomUUID().toString();
        VehicleEntity newVehicle = new VehicleEntity(tempId, userId, name, type, year, mileage, modelTrim, null);
        
        Map<String, Object> vehicleData = new HashMap<>();
        vehicleData.put("id", tempId);
        vehicleData.put("user_id", userId);
        vehicleData.put("name", name);
        vehicleData.put("type", type);
        vehicleData.put("year", year);
        vehicleData.put("mileage", mileage);
        vehicleData.put("model_trim", modelTrim);
        
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(vehicleData);
        
        // Optimistic insert
        executorService.execute(() -> vehicleDao.insertVehicle(newVehicle));

        supabaseApi.addVehicle(supabaseKey, "Bearer " + supabaseKey, "application/json", "return=minimal", list)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        loadingLiveData.setValue(false);
                        if (response.isSuccessful() || response.code() == 201) {
                            addSuccessLiveData.setValue(true);
                            refreshVehicles(userId, supabaseKey);
                        } else {
                            errorLiveData.setValue("Failed to add vehicle: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        loadingLiveData.setValue(false);
                        errorLiveData.setValue(t.getMessage());
                    }
                });
    }

    public void updateVehicle(VehicleEntity vehicle, String supabaseKey) {
        loadingLiveData.setValue(true);
        addSuccessLiveData.setValue(false);

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", vehicle.getName());
        updates.put("type", vehicle.getType());
        updates.put("year", vehicle.getYear());
        updates.put("mileage", vehicle.getMileage());
        updates.put("model_trim", vehicle.getModelTrim());

        // Optimistic update
        executorService.execute(() -> vehicleDao.insertVehicle(vehicle));

        supabaseApi.updateVehicle(supabaseKey, "Bearer " + supabaseKey, "application/json", "eq." + vehicle.getId(), updates)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        loadingLiveData.setValue(false);
                        if (response.isSuccessful()) {
                            addSuccessLiveData.setValue(true);
                            refreshVehicles(vehicle.getUserId(), supabaseKey);
                        } else {
                            errorLiveData.setValue("Update failed: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        loadingLiveData.setValue(false);
                        errorLiveData.setValue(t.getMessage());
                    }
                });
    }

    public void refreshVehicles(String userId, String supabaseKey) {
        supabaseApi.getVehicles(supabaseKey, "Bearer " + supabaseKey, userId)
                .enqueue(new Callback<List<VehicleEntity>>() {
                    @Override
                    public void onResponse(Call<List<VehicleEntity>> call, Response<List<VehicleEntity>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            executorService.execute(() -> {
                                vehicleDao.insertAll(response.body());
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<List<VehicleEntity>> call, Throwable t) {
                        errorLiveData.setValue("Sync failed: " + t.getMessage());
                    }
                });
    }

    public void deleteVehicle(String vehicleId, String supabaseKey) {
        loadingLiveData.setValue(true);
        supabaseApi.deleteVehicle(supabaseKey, "Bearer " + supabaseKey, "eq." + vehicleId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        loadingLiveData.setValue(false);
                        if (response.isSuccessful()) {
                            executorService.execute(() -> {
                                // Find and delete from local DB
                                vehicleDao.deleteById(vehicleId);
                            });
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
