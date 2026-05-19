package com.example.vehiclecare_smartmaintenancetracking.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "services")
public class ServiceEntity {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @ColumnInfo(name = "id")
    private String id;

    @SerializedName("vehicle_id")
    @ColumnInfo(name = "vehicle_id")
    private String vehicleId;

    @SerializedName("service_type")
    @ColumnInfo(name = "service_type")
    private String serviceType;

    @SerializedName("service_date")
    @ColumnInfo(name = "service_date")
    private String serviceDate;

    @SerializedName("mileage")
    @ColumnInfo(name = "mileage")
    private Integer mileage;

    @SerializedName("cost")
    @ColumnInfo(name = "cost")
    private Double cost;

    @SerializedName("provider")
    @ColumnInfo(name = "provider")
    private String provider;

    @SerializedName("notes")
    @ColumnInfo(name = "notes")
    private String notes;

    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    private String createdAt;

    public ServiceEntity(@NonNull String id, String vehicleId, String serviceType, String serviceDate, Integer mileage, Double cost, String provider, String notes, String createdAt) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.serviceType = serviceType;
        this.serviceDate = serviceDate;
        this.mileage = mileage;
        this.cost = cost;
        this.provider = provider;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getServiceDate() { return serviceDate; }
    public void setServiceDate(String serviceDate) { this.serviceDate = serviceDate; }

    public Integer getMileage() { return mileage; }
    public void setMileage(Integer mileage) { this.mileage = mileage; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
