package com.example.vehiclecare_smartmaintenancetracking.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.vehiclecare_smartmaintenancetracking.models.ReminderEntity;
import java.util.List;
import com.example.vehiclecare_smartmaintenancetracking.database.AppDatabase;
import com.example.vehiclecare_smartmaintenancetracking.network.SupabaseApi;
import com.example.vehiclecare_smartmaintenancetracking.repository.ReminderRepository;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReminderViewModel extends AndroidViewModel {
    private final ReminderRepository reminderRepository;

    public ReminderViewModel(@NonNull Application application) {
        super(application);
        
        String baseUrl = application.getString(com.example.vehiclecare_smartmaintenancetracking.R.string.supabase_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        SupabaseApi api = retrofit.create(SupabaseApi.class);
        AppDatabase db = AppDatabase.getInstance(application);
        
        this.reminderRepository = new ReminderRepository(api, db.reminderDao());
    }

    public void addReminder(String userId, String vehicleId, String maintenanceType, String reminderType, Integer mileage, String date, String notes, String supabaseKey) {
        reminderRepository.addReminder(userId, vehicleId, maintenanceType, reminderType, mileage, date, notes, supabaseKey);
    }

    public LiveData<List<ReminderEntity>> getReminders(String userId) {
        return reminderRepository.getReminders(userId);
    }

    public void refreshReminders(String userId, String supabaseKey) {
        reminderRepository.refreshReminders(userId, supabaseKey);
    }

    public LiveData<String> getError() { return reminderRepository.getError(); }
    public LiveData<Boolean> isLoading() { return reminderRepository.getLoading(); }
    public LiveData<Boolean> getAddSuccess() { return reminderRepository.getAddSuccess(); }
}
