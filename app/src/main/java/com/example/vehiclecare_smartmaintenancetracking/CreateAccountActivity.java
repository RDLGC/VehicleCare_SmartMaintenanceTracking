package com.example.vehiclecare_smartmaintenancetracking;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.AuthViewModel;

public class CreateAccountActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupEvents();
        observeViewModel();
    }

    private void setupEvents() {
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        View btnCreate = findViewById(R.id.btnCreateAccount);
        if (btnCreate != null) {
            btnCreate.setOnClickListener(v -> {
                // We'll use the same helper logic from Events to extract data
                String name = Events.getTextFromContainer(this, R.id.nameInputContainer); // Assuming we add these helper methods
                String email = Events.getTextFromContainer(this, R.id.emailInputContainer);
                String password = Events.getTextFromContainer(this, R.id.passwordInputContainer);
                String confirm = Events.getTextFromContainer(this, R.id.confirmPasswordContainer);
                String supabaseKey = getString(R.string.supabase_key);

                if (validateInput(name, email, password, confirm)) {
                    authViewModel.signUp(name, email, password, supabaseKey);
                }
            });
        }
    }

    private void observeViewModel() {
        authViewModel.getSuccessMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                authViewModel.setSuccessMessage(null); // Clear it
                finish();
            }
        });

        authViewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateInput(String name, String email, String password, String confirm) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(confirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
