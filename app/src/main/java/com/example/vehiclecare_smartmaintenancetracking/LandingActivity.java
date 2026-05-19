package com.example.vehiclecare_smartmaintenancetracking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.VehicleViewModel;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.ServiceViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.example.vehiclecare_smartmaintenancetracking.models.ReminderEntity;
import com.example.vehiclecare_smartmaintenancetracking.models.ServiceEntity;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.ReminderViewModel;
import java.util.List;
import java.util.Locale;

public class LandingActivity extends AppCompatActivity {
    private VehicleViewModel vehicleViewModel;
    private ServiceViewModel serviceViewModel;
    private ReminderViewModel reminderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_landing);

        vehicleViewModel = new ViewModelProvider(this).get(VehicleViewModel.class);
        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);

        setupActions();
        observeViewModel();
    }

    private void setupActions() {
        findViewById(R.id.flNotifications).setOnClickListener(v -> {
            startActivity(new Intent(this, NotificationsActivity.class));
        });

        findViewById(R.id.ivSettings).setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
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

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                startActivity(new Intent(this, UserProfileActivity.class));
                return true;
            } else if (id == R.id.nav_history) {
                startActivity(new Intent(this, ServiceHistoryActivity.class));
                return true;
            } else if (id == R.id.nav_alerts) {
                startActivity(new Intent(this, NotificationsActivity.class));
                return true;
            } else if (id == R.id.nav_home) {
                // Already on home
                return true;
            }
            return false;
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
                    updateRecentHistory(services);
                }
            });

            reminderViewModel.getReminders(userId).observe(this, reminders -> {
                if (reminders != null) {
                    updateUpcomingMaintenance(reminders);
                }
            });

            String supabaseKey = getString(R.string.supabase_key);
            serviceViewModel.refreshServices(supabaseKey);
            reminderViewModel.refreshReminders(userId, supabaseKey);
        }
    }

    private void updateRecentHistory(List<ServiceEntity> services) {
        if (services.size() >= 1) {
            ServiceEntity s1 = services.get(services.size() - 1);
            ((TextView) findViewById(R.id.tvRecent1Title)).setText(s1.getServiceType());
            // Need a way to show date/provider - can add more views if needed or just use strings
        }
        if (services.size() >= 2) {
            ServiceEntity s2 = services.get(services.size() - 2);
            ((TextView) findViewById(R.id.tvRecent2Title)).setText(s2.getServiceType());
        }
    }

    private void updateUpcomingMaintenance(List<ReminderEntity> reminders) {
        if (reminders.size() >= 1) {
            ReminderEntity r1 = reminders.get(0);
            ((TextView) findViewById(R.id.tvOilTitle)).setText(r1.getMaintenanceType());
        }
        if (reminders.size() >= 2) {
            ReminderEntity r2 = reminders.get(1);
            ((TextView) findViewById(R.id.tvTireTitle)).setText(r2.getMaintenanceType());
        }
        if (reminders.size() >= 3) {
            ReminderEntity r3 = reminders.get(2);
            ((TextView) findViewById(R.id.tvCoolantTitle)).setText(r3.getMaintenanceType());
        }
    }
}
