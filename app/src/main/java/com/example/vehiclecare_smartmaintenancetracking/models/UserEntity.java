package com.example.vehiclecare_smartmaintenancetracking.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    @NonNull
    @SerializedName("firebase_uid")
    @ColumnInfo(name = "firebase_uid")
    private String firebaseUid;

    @SerializedName("full_name")
    @ColumnInfo(name = "full_name")
    private String fullName;

    @SerializedName("email")
    @ColumnInfo(name = "email")
    private String email;

    @SerializedName("avatar_url")
    @ColumnInfo(name = "avatar_url")
    private String avatarUrl;

    @SerializedName("phone")
    @ColumnInfo(name = "phone")
    private String phone;

    @SerializedName("location")
    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "last_sync")
    private long lastSync;

    public UserEntity(@NonNull String firebaseUid, String fullName, String email, String avatarUrl, String phone, String location, long lastSync) {
        this.firebaseUid = firebaseUid;
        this.fullName = fullName;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.phone = phone;
        this.location = location;
        this.lastSync = lastSync;
    }

    @NonNull
    public String getFirebaseUid() { return firebaseUid; }
    public void setFirebaseUid(@NonNull String firebaseUid) { this.firebaseUid = firebaseUid; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public long getLastSync() { return lastSync; }
    public void setLastSync(long lastSync) { this.lastSync = lastSync; }
}
