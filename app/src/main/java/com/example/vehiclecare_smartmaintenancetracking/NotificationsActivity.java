package com.example.vehiclecare_smartmaintenancetracking;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vehiclecare_smartmaintenancetracking.adapter.ReminderAdapter;
import com.example.vehiclecare_smartmaintenancetracking.models.ReminderEntity;
import com.example.vehiclecare_smartmaintenancetracking.models.VehicleEntity;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.ReminderViewModel;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.VehicleViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationsActivity extends AppCompatActivity {
    private ReminderViewModel reminderViewModel;
    private VehicleViewModel vehicleViewModel;
    private ReminderAdapter adapter;
    private List<ReminderEntity> allReminders = new ArrayList<>();
    private String selectedVehicleId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);

        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        vehicleViewModel = new ViewModelProvider(this).get(VehicleViewModel.class);

        initViews();
        setupRecyclerView();
        observeViewModel();
    }

    private void initViews() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        
        ChipGroup chipGroup = findViewById(R.id.chipGroupVehicles);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty() || checkedIds.get(0) == R.id.chipAllVehicles) {
                selectedVehicleId = null;
            } else {
                Chip chip = findViewById(checkedIds.get(0));
                selectedVehicleId = (String) chip.getTag();
            }
            filterReminders();
        });
    }

    private void setupRecyclerView() {
        RecyclerView rv = findViewById(R.id.rvNotifications);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReminderAdapter();
        rv.setAdapter(adapter);
    }

    private void observeViewModel() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            reminderViewModel.getReminders(uid).observe(this, reminders -> {
                if (reminders != null) {
                    allReminders = reminders;
                    filterReminders();
                }
            });

            vehicleViewModel.getVehicles(uid).observe(this, vehicles -> {
                if (vehicles != null) {
                    populateVehicleChips(vehicles);
                }
            });

            reminderViewModel.refreshReminders(uid, getString(R.string.supabase_key));
        }
    }

    private void populateVehicleChips(List<VehicleEntity> vehicles) {
        ChipGroup chipGroup = findViewById(R.id.chipGroupVehicles);
        // Keep "All Vehicles" chip, remove others
        int childCount = chipGroup.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            if (chipGroup.getChildAt(i).getId() != R.id.chipAllVehicles) {
                chipGroup.removeViewAt(i);
            }
        }

        for (VehicleEntity vehicle : vehicles) {
            Chip chip = new Chip(this);
            chip.setText(vehicle.getName());
            chip.setCheckable(true);
            chip.setTag(vehicle.getId());
            chipGroup.addView(chip);
        }
    }

    private void filterReminders() {
        if (selectedVehicleId == null) {
            adapter.setReminders(allReminders);
        } else {
            List<ReminderEntity> filtered = new ArrayList<>();
            for (ReminderEntity r : allReminders) {
                if (selectedVehicleId.equals(r.getVehicleId())) {
                    filtered.add(r);
                }
            }
            adapter.setReminders(filtered);
        }
    }
}
