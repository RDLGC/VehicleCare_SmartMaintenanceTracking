package com.example.vehiclecare_smartmaintenancetracking.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "vehicles")
public class VehicleEntity {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @ColumnInfo(name = "id")
    private String id;

    @SerializedName("user_id")
    @ColumnInfo(name = "user_id")
    private String userId;

    @SerializedName("name")
    @ColumnInfo(name = "name")
    private String name;

    @SerializedName("type")
    @ColumnInfo(name = "type")
    private String type; // 'Car', 'Motorcycle', 'Truck'

    @SerializedName("year")
    @ColumnInfo(name = "year")
    private Integer year;

    @SerializedName("mileage")
    @ColumnInfo(name = "mileage")
    private Integer mileage;

    @SerializedName("model_trim")
    @ColumnInfo(name = "model_trim")
    private String modelTrim;

    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    private String createdAt;

    public VehicleEntity(@NonNull String id, String userId, String name, String type, Integer year, Integer mileage, String modelTrim, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.year = year;
        this.mileage = mileage;
        this.modelTrim = modelTrim;
        this.createdAt = createdAt;
    }

    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Integer getMileage() { return mileage; }
    public void setMileage(Integer mileage) { this.mileage = mileage; }

    public String getModelTrim() { return modelTrim; }
    public void setModelTrim(String modelTrim) { this.modelTrim = modelTrim; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
