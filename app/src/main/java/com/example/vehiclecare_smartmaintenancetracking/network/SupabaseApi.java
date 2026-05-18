package com.example.vehiclecare_smartmaintenancetracking.network;

import com.example.vehiclecare_smartmaintenancetracking.models.UserEntity;
import com.example.vehiclecare_smartmaintenancetracking.models.VehicleEntity;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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
}
