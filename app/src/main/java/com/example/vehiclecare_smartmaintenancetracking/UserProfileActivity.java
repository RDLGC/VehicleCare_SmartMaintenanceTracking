package com.example.vehiclecare_smartmaintenancetracking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.UserViewModel;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.VehicleViewModel;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.ServiceViewModel;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Locale;

public class UserProfileActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private VehicleViewModel vehicleViewModel;
    private ServiceViewModel serviceViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        vehicleViewModel = new ViewModelProvider(this).get(VehicleViewModel.class);
        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);

        setupActions();
        observeViewModel();
    }

    private void setupActions() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        
        findViewById(R.id.btnEditProfile).setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.cvManageVehicles).setOnClickListener(v -> {
            Intent intent = new Intent(this, ManageVehiclesActivity.class);
            startActivity(intent);
        });
    }

    private void observeViewModel() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            userViewModel.getProfile(uid).observe(this, user -> {
                if (user != null) {
                    ((TextView) findViewById(R.id.tvUserName)).setText(user.getFullName());
                    ((TextView) findViewById(R.id.tvUserEmail)).setText(user.getEmail());
                    ((TextView) findViewById(R.id.tvFullName)).setText(user.getFullName());
                    ((TextView) findViewById(R.id.tvEmail)).setText(user.getEmail());
                    ((TextView) findViewById(R.id.tvPhone)).setText(user.getPhone() != null ? user.getPhone() : "Not set");
                    ((TextView) findViewById(R.id.tvLocation)).setText(user.getLocation() != null ? user.getLocation() : "Not set");
                    // Member since could be derived from first sync or last sync if not stored
                    ((TextView) findViewById(R.id.tvMemberSince)).setText("Verified User");
                }
            });

            vehicleViewModel.getVehicles(uid).observe(this, vehicles -> {
                if (vehicles != null) {
                    ((TextView) findViewById(R.id.tvVehicleCount)).setText(String.valueOf(vehicles.size()));
                    ((TextView) findViewById(R.id.tvVehiclesRegistered)).setText(
                            getString(R.string.vehicles_registered_count, vehicles.size()));
                }
            });

            serviceViewModel.getServices().observe(this, services -> {
                if (services != null) {
                    ((TextView) findViewById(R.id.tvServiceCount)).setText(String.valueOf(services.size()));
                }
            });

            serviceViewModel.getTotalSpent().observe(this, total -> {
                if (total != null) {
                    ((TextView) findViewById(R.id.tvTotalSpent)).setText(String.format(Locale.US, "$%.2f", total));
                }
            });
            
            // Initial sync
            userViewModel.syncProfile(uid, getString(R.string.supabase_key));
        }
    }
}
