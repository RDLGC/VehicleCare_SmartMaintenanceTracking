package com.example.vehiclecare_smartmaintenancetracking.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.vehiclecare_smartmaintenancetracking.models.ServiceEntity;
import java.util.List;

@Dao
public interface ServiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertService(ServiceEntity service);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ServiceEntity> services);

    @Query("SELECT * FROM services ORDER BY service_date DESC")
    LiveData<List<ServiceEntity>> getAllServices();

    @Query("SELECT * FROM services WHERE vehicle_id = :vehicleId ORDER BY service_date DESC")
    LiveData<List<ServiceEntity>> getServicesForVehicle(String vehicleId);

    @Query("SELECT SUM(cost) FROM services")
    LiveData<Double> getTotalSpent();

    @Delete
    void deleteService(ServiceEntity service);

    @Query("DELETE FROM services")
    void clearAll();
}
