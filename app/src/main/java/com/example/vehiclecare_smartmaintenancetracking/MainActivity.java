package com.example.vehiclecare_smartmaintenancetracking;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.lifecycle.ViewModelProvider;

import com.example.vehiclecare_smartmaintenancetracking.viewmodel.AuthViewModel;

import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        observeViewModel();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Events.setupLoginEvents(this);
    }

    private void observeViewModel() {
        authViewModel.getSuccessMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                authViewModel.setSuccessMessage(null);
                
                // Navigate to Landing page on successful login
                Intent intent = new Intent(this, LandingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        authViewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });

        authViewModel.isLoading().observe(this, isLoading -> {
            // Update UI loading state (e.g., show ProgressBar)
        });
    }
}
