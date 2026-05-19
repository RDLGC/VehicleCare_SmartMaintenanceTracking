package com.example.vehiclecare_smartmaintenancetracking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.AuthViewModel;

public class SettingsActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        initViews();
    }

    private void initViews() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        
        findViewById(R.id.btnEditProfile).setOnClickListener(v -> {
            startActivity(new Intent(this, EditProfileActivity.class));
        });

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            logout();
        });
    }

    private void logout() {
        // We need to add a signOut method to AuthViewModel or access repository directly
        // For now, let's trigger a logout. I'll need to check AuthViewModel.
        // authViewModel.signOut(); // I need to add this
        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
        
        // Use a simple way for now or update ViewModel
        com.google.firebase.auth.FirebaseAuth.getInstance().signOut();
        
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
