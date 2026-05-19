package com.example.vehiclecare_smartmaintenancetracking.network;

import com.example.vehiclecare_smartmaintenancetracking.models.UserEntity;
import com.example.vehiclecare_smartmaintenancetracking.models.VehicleEntity;
import com.example.vehiclecare_smartmaintenancetracking.models.ServiceEntity;
import com.example.vehiclecare_smartmaintenancetracking.models.ReminderEntity;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PATCH;
import retrofit2.http.Query;

public interface SupabaseApi {
    // Profile endpoints...
    @GET("rest/v1/profiles")
    Call<List<UserEntity>> getProfile(
            @Header("apikey") String apiKey,
            @Header("Authorization") String authToken,
            @Query("firebase_uid") String firebaseUid
    );

    @POST("rest/v1/profiles")
    Call<Void> createProfile(
            @Header("apikey") String apiKey,
            @Header("Authorization") String authToken,
            @Header("Content-Type") String contentType,
            @Header("Prefer") String prefer,
            @Body List<java.util.Map<String, Object>> profile
    );

    @PATCH("rest/v1/profiles")
    Call<Void> updateProfile(
            @Header("apikey") String apiKey,
            @Header("Authorization") String authToken,
            @Header("Content-Type") String contentType,
            @Query("firebase_uid") String firebaseUid,
            @Body java.util.Map<String, Object> updates
    );

    // Vehicle endpoints
    @GET("rest/v1/vehicles")
    Call<List<VehicleEntity>> getVehicles(
            @Header("apikey") String apiKey,
            @Header("Authorization") String authToken,
            @Query("user_id") String userId
    );

    @POST("rest/v1/vehicles")
    Call<Void> addVehicle(
            @Header("apikey") String apiKey,
            @Header("Authorization") String authToken,
            @Header("Content-Type") String contentType,
            @Header("Prefer") String prefer,
            @Body List<java.util.Map<String, Object>> vehicleList
    );

    // Service Record endpoints
    @GET("rest/v1/services")
    Call<List<ServiceEntity>> getServices(
            @Header("apikey") String apiKey,
            @Header("Authorization") String authToken,
            @Query("vehicle_id") String vehicleId
    );

    @GET("rest/v1/services")
    Call<List<ServiceEntity>> getAllServices(
            @Header("apikey") String apiKey,
            @Header("Authorization") String authToken
    );

    @POST("rest/v1/services")
    Call<Void> addService(
            @Header("apikey") String apiKey,
            @Header("Authorization") String authToken,
            @Header("Content-Type") String contentType,
            @Header("Prefer") String prefer,
            @Body List<java.util.Map<String, Object>> serviceList
    );

    // Reminder endpoints
    @GET("rest/v1/reminders")
    Call<List<ReminderEntity>> getReminders(
            @Header("apikey") String apiKey,
            @Header("Authorization") String authToken,
            @Query("user_id") String userId
    );

    @POST("rest/v1/reminders")
    Call<Void> addReminder(
            @Header("apikey") String apiKey,
            @Header("Authorization") String authToken,
            @Header("Content-Type") String contentType,
            @Header("Prefer") String prefer,
            @Body List<java.util.Map<String, Object>> reminderList
    );
}
