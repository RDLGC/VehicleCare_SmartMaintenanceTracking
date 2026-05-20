package com.example.vehiclecare_smartmaintenancetracking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
    private List<com.example.vehiclecare_smartmaintenancetracking.models.VehicleEntity> currentVehicles;

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

    @Override
    protected void onResume() {
        super.onResume();
        refreshUI();
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
                this.currentVehicles = vehicles;
                refreshUI();
            });

            serviceViewModel.getFilteredServices().observe(this, services -> {
                if (services != null) {
                    ((TextView) findViewById(R.id.tvLandingServiceCount)).setText(String.valueOf(services.size()));
                    updateRecentHistory(services);
                }
            });

            reminderViewModel.getFilteredReminders().observe(this, reminders -> {
                if (reminders != null) {
                    updateUpcomingMaintenance(reminders);
                }
            });

            String supabaseKey = getString(R.string.supabase_key);
            serviceViewModel.refreshServices(supabaseKey);
            reminderViewModel.refreshReminders(userId, supabaseKey);
        }
    }

    private void refreshUI() {
        if (currentVehicles == null || currentVehicles.isEmpty()) return;

        android.content.SharedPreferences prefs = getSharedPreferences("VehicleCare", MODE_PRIVATE);
        String activeVehicleId = prefs.getString("active_vehicle_id", null);

        com.example.vehiclecare_smartmaintenancetracking.models.VehicleEntity active = null;
        if (activeVehicleId != null) {
            for (com.example.vehiclecare_smartmaintenancetracking.models.VehicleEntity v : currentVehicles) {
                if (activeVehicleId.equals(v.getId())) {
                    active = v;
                    break;
                }
            }
        }
        if (active == null) {
            active = currentVehicles.get(0);
            // Save this as active if none selected
            prefs.edit().putString("active_vehicle_id", active.getId()).apply();
        }

        ((TextView) findViewById(R.id.tvHeaderSubtitle)).setText(active.getName());
        ((TextView) findViewById(R.id.tvCarName)).setText(active.getName());
        ((TextView) findViewById(R.id.tvCarDetail)).setText(String.format(Locale.US, "%d • %s", active.getYear(), active.getModelTrim()));
        ((TextView) findViewById(R.id.tvLandingMileage)).setText(String.format(Locale.US, "%,d mi", active.getMileage()));

        // Update selected vehicle in ViewModels to trigger filtered observations
        serviceViewModel.setSelectedVehicleId(active.getId());
        reminderViewModel.setSelectedVehicleId(active.getId());
    }

    private void updateRecentHistory(List<ServiceEntity> services) {
        LinearLayout container = findViewById(R.id.llHistoryList);
        TextView tvEmpty = findViewById(R.id.tvNoRecentHistory);
        container.removeAllViews();

        if (services == null || services.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            return;
        }

        tvEmpty.setVisibility(View.GONE);
        // Show last 2
        int count = Math.min(services.size(), 2);
        for (int i = 0; i < count; i++) {
            ServiceEntity s = services.get(services.size() - 1 - i);
            View view = getLayoutInflater().inflate(R.layout.item_service_minimal, container, false);
            ((TextView) view.findViewById(R.id.tvServiceTitle)).setText(s.getServiceType());
            ((TextView) view.findViewById(R.id.tvServiceDate)).setText(s.getServiceDate());
            ((TextView) view.findViewById(R.id.tvServiceDetail)).setText(String.format(Locale.US, "%s • $%.2f", s.getProvider(), s.getCost()));
            container.addView(view);
            
            if (i < count - 1) {
                View divider = new View(this);
                divider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
                divider.setBackgroundColor(0xFFF0F0F0);
                container.addView(divider);
            }
        }
    }

    private void updateUpcomingMaintenance(List<ReminderEntity> reminders) {
        LinearLayout container = findViewById(R.id.llUpcomingList);
        TextView tvEmpty = findViewById(R.id.tvNoUpcomingReminders);
        container.removeAllViews();

        if (reminders == null || reminders.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            return;
        }

        tvEmpty.setVisibility(View.GONE);
        // Show last 3
        int count = Math.min(reminders.size(), 3);
        for (int i = 0; i < count; i++) {
            ReminderEntity r = reminders.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_reminder_minimal, container, false);
            ((TextView) view.findViewById(R.id.tvReminderTitle)).setText(r.getMaintenanceType());
            
            String desc = "";
            if ("Mileage".equals(r.getReminderType())) desc = "Due in " + r.getMileageInterval() + " mi";
            else desc = "Due on " + r.getSpecificDate();
            
            ((TextView) view.findViewById(R.id.tvReminderDesc)).setText(desc);
            container.addView(view);

            if (i < count - 1) {
                View divider = new View(this);
                divider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
                divider.setBackgroundColor(0xFFF0F0F0);
                container.addView(divider);
            }
        }
    }
}
