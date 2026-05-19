package com.example.vehiclecare_smartmaintenancetracking.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.vehiclecare_smartmaintenancetracking.models.UserEntity;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity user);

    @Query("SELECT * FROM users WHERE firebase_uid = :uid LIMIT 1")
    UserEntity getUser(String uid);

    @Query("SELECT * FROM users WHERE firebase_uid = :uid LIMIT 1")
    LiveData<UserEntity> getUserLiveData(String uid);

    @Query("DELETE FROM users")
    void clearAll();
}
