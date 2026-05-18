package com.example.vehiclecare_smartmaintenancetracking.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.vehiclecare_smartmaintenancetracking.models.VehicleEntity;
import java.util.List;

@Dao
public interface VehicleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVehicle(VehicleEntity vehicle);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<VehicleEntity> vehicles);

    @Query("SELECT * FROM vehicles WHERE user_id = :userId ORDER BY created_at DESC")
    LiveData<List<VehicleEntity>> getVehiclesForUser(String userId);

    @Delete
    void deleteVehicle(VehicleEntity vehicle);

    @Query("DELETE FROM vehicles")
    void clearAll();
}
