package com.example.vehiclecare_smartmaintenancetracking.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.vehiclecare_smartmaintenancetracking.models.UserEntity;
import com.example.vehiclecare_smartmaintenancetracking.models.VehicleEntity;
import com.example.vehiclecare_smartmaintenancetracking.models.ServiceEntity;
import com.example.vehiclecare_smartmaintenancetracking.models.ReminderEntity;

@Database(entities = {UserEntity.class, VehicleEntity.class, ServiceEntity.class, ReminderEntity.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract VehicleDao vehicleDao();
    public abstract ServiceDao serviceDao();
    public abstract ReminderDao reminderDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "vehicle_care_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
