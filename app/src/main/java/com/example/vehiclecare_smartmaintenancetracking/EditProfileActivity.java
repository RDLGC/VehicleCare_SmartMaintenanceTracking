package com.example.vehiclecare_smartmaintenancetracking;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.vehiclecare_smartmaintenancetracking.models.UserEntity;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.UserViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class EditProfileActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private UserEntity currentUser;
    
    private TextInputEditText etFullName, etEmail, etPhone, etLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        initViews();
        setupActions();
        observeViewModel();
    }

    private void initViews() {
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etLocation = findViewById(R.id.etLocation);
        
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());
    }

    private void setupActions() {
        findViewById(R.id.btnSave).setOnClickListener(v -> {
            if (currentUser != null) {
                String fullName = etFullName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String location = etLocation.getText().toString().trim();

                if (validate(fullName, email)) {
                    currentUser.setFullName(fullName);
                    currentUser.setEmail(email);
                    currentUser.setPhone(phone);
                    currentUser.setLocation(location);
                    
                    userViewModel.updateProfile(currentUser, getString(R.string.supabase_key));
                }
            }
        });
    }

    private boolean validate(String name, String email) {
        if (name.isEmpty()) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void observeViewModel() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            userViewModel.getProfile(uid).observe(this, user -> {
                if (user != null && currentUser == null) { // Only set once to allow editing
                    currentUser = user;
                    etFullName.setText(user.getFullName());
                    etEmail.setText(user.getEmail());
                    etPhone.setText(user.getPhone());
                    etLocation.setText(user.getLocation());
                }
            });
        }

        userViewModel.getUpdateSuccess().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        userViewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });

        userViewModel.isLoading().observe(this, loading -> {
            findViewById(R.id.btnSave).setEnabled(!loading);
        });
    }
}
