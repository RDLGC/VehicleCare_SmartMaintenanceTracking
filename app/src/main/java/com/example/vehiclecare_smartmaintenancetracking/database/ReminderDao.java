package com.example.vehiclecare_smartmaintenancetracking.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.vehiclecare_smartmaintenancetracking.models.ReminderEntity;
import java.util.List;

@Dao
public interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReminder(ReminderEntity reminder);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ReminderEntity> reminders);

    @Query("SELECT * FROM reminders WHERE user_id = :userId")
    LiveData<List<ReminderEntity>> getRemindersForUser(String userId);

    @Query("SELECT * FROM reminders WHERE vehicle_id = :vehicleId")
    LiveData<List<ReminderEntity>> getRemindersForVehicle(String vehicleId);

    @Delete
    void deleteReminder(ReminderEntity reminder);

    @Query("DELETE FROM reminders")
    void clearAll();
}
