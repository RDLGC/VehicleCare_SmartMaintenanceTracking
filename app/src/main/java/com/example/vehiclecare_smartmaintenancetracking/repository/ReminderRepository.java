package com.example.vehiclecare_smartmaintenancetracking.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.vehiclecare_smartmaintenancetracking.database.ReminderDao;
import com.example.vehiclecare_smartmaintenancetracking.models.ReminderEntity;
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

public class ReminderRepository {
    private final SupabaseApi supabaseApi;
    private final ReminderDao reminderDao;
    private final ExecutorService executorService;
    
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> addSuccessLiveData = new MutableLiveData<>();

    public ReminderRepository(SupabaseApi supabaseApi, ReminderDao reminderDao) {
        this.supabaseApi = supabaseApi;
        this.reminderDao = reminderDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void addReminder(String userId, String vehicleId, String maintenanceType, String reminderType, Integer mileage, String date, String notes, String supabaseKey) {
        loadingLiveData.setValue(true);
        addSuccessLiveData.setValue(false);
        
        Map<String, Object> data = new HashMap<>();
        data.put("user_id", userId);
        data.put("vehicle_id", vehicleId);
        data.put("maintenance_type", maintenanceType);
        data.put("reminder_type", reminderType);
        data.put("mileage_interval", mileage);
        data.put("specific_date", date);
        data.put("notes", notes);
        
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(data);
        
        supabaseApi.addReminder(supabaseKey, "Bearer " + supabaseKey, "application/json", "return=minimal", list)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        loadingLiveData.setValue(false);
                        if (response.isSuccessful() || response.code() == 201) {
                            addSuccessLiveData.setValue(true);
                            refreshReminders(userId, supabaseKey);
                        } else {
                            errorLiveData.setValue("Failed to set reminder: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        loadingLiveData.setValue(false);
                        errorLiveData.setValue(t.getMessage());
                    }
                });
    }

    public void refreshReminders(String userId, String supabaseKey) {
        supabaseApi.getReminders(supabaseKey, "Bearer " + supabaseKey, userId)
                .enqueue(new Callback<List<ReminderEntity>>() {
                    @Override
                    public void onResponse(Call<List<ReminderEntity>> call, Response<List<ReminderEntity>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            executorService.execute(() -> reminderDao.insertAll(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ReminderEntity>> call, Throwable t) {
                        errorLiveData.setValue("Sync failed: " + t.getMessage());
                    }
                });
    }

    public LiveData<String> getError() { return errorLiveData; }
    public LiveData<Boolean> getLoading() { return loadingLiveData; }
    public LiveData<Boolean> getAddSuccess() { return addSuccessLiveData; }
}
