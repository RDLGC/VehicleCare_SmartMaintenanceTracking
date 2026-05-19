package com.example.vehiclecare_smartmaintenancetracking.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "reminders")
public class ReminderEntity {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @ColumnInfo(name = "id")
    private String id;

    @SerializedName("user_id")
    @ColumnInfo(name = "user_id")
    private String userId;

    @SerializedName("vehicle_id")
    @ColumnInfo(name = "vehicle_id")
    private String vehicleId;

    @SerializedName("maintenance_type")
    @ColumnInfo(name = "maintenance_type")
    private String maintenanceType;

    @SerializedName("reminder_type")
    @ColumnInfo(name = "reminder_type")
    private String reminderType; // 'Mileage', 'Date', 'Both'

    @SerializedName("mileage_interval")
    @ColumnInfo(name = "mileage_interval")
    private Integer mileageInterval;

    @SerializedName("specific_date")
    @ColumnInfo(name = "specific_date")
    private String specificDate;

    @SerializedName("is_active")
    @ColumnInfo(name = "is_active")
    private boolean isActive;

    @SerializedName("notes")
    @ColumnInfo(name = "notes")
    private String notes;

    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    private String createdAt;

    public ReminderEntity(@NonNull String id, String userId, String vehicleId, String maintenanceType, String reminderType, Integer mileageInterval, String specificDate, boolean isActive, String notes, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.maintenanceType = maintenanceType;
        this.reminderType = reminderType;
        this.mileageInterval = mileageInterval;
        this.specificDate = specificDate;
        this.isActive = isActive;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }

    public String getMaintenanceType() { return maintenanceType; }
    public void setMaintenanceType(String maintenanceType) { this.maintenanceType = maintenanceType; }

    public String getReminderType() { return reminderType; }
    public void setReminderType(String reminderType) { this.reminderType = reminderType; }

    public Integer getMileageInterval() { return mileageInterval; }
    public void setMileageInterval(Integer mileageInterval) { this.mileageInterval = mileageInterval; }

    public String getSpecificDate() { return specificDate; }
    public void setSpecificDate(String specificDate) { this.specificDate = specificDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
