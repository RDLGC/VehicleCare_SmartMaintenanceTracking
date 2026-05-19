package com.example.vehiclecare_smartmaintenancetracking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.VehicleViewModel;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.ServiceViewModel;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Locale;

public class LandingActivity extends AppCompatActivity {
    private VehicleViewModel vehicleViewModel;
    private ServiceViewModel serviceViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_landing);

        vehicleViewModel = new ViewModelProvider(this).get(VehicleViewModel.class);
        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);

        setupActions();
        observeViewModel();
    }

    private void setupActions() {
        findViewById(R.id.flNotifications).setOnClickListener(v -> {
            // Future: Intent to notifications
        });

        findViewById(R.id.ivSettings).setOnClickListener(v -> {
            // Future: Intent to settings
        });

        findViewById(R.id.cardAddService).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddServiceActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.cardSetReminder).setOnClickListener(v -> {
            Intent intent = new Intent(this, SetReminderActivity.class);
            startActivity(intent);
        });
        
        findViewById(R.id.tvViewAllHistory).setOnClickListener(v -> {
            Intent intent = new Intent(this, ServiceHistoryActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.tvViewAllMaintenance).setOnClickListener(v -> {
            Intent intent = new Intent(this, ServiceHistoryActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnChangeVehicle).setOnClickListener(v -> {
            Intent intent = new Intent(this, ManageVehiclesActivity.class);
            startActivity(intent);
        });
    }

    private void observeViewModel() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            vehicleViewModel.getVehicles(userId).observe(this, vehicles -> {
                if (vehicles != null && !vehicles.isEmpty()) {
                    ((TextView) findViewById(R.id.tvHeaderSubtitle)).setText(vehicles.get(0).getName());
                    ((TextView) findViewById(R.id.tvCarName)).setText(vehicles.get(0).getName());
                    ((TextView) findViewById(R.id.tvLandingMileage)).setText(String.format(Locale.US, "%,d mi", vehicles.get(0).getMileage()));
                }
            });

            serviceViewModel.getServices().observe(this, services -> {
                if (services != null) {
                    ((TextView) findViewById(R.id.tvLandingServiceCount)).setText(String.valueOf(services.size()));
                }
            });
        }
    }
}
