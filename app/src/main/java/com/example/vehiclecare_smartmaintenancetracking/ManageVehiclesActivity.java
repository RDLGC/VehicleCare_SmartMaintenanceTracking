package com.example.vehiclecare_smartmaintenancetracking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vehiclecare_smartmaintenancetracking.adapter.VehicleAdapter;
import com.example.vehiclecare_smartmaintenancetracking.models.VehicleEntity;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.VehicleViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class ManageVehiclesActivity extends AppCompatActivity {
    private VehicleViewModel vehicleViewModel;
    private VehicleAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_vehicles);

        vehicleViewModel = new ViewModelProvider(this).get(VehicleViewModel.class);
        
        initViews();
        setupRecyclerView();
        observeViewModel();
        
        // Initial fetch
        String userId = FirebaseAuth.getInstance().getUid();
        String supabaseKey = getString(R.string.supabase_key);
        if (userId != null) {
            vehicleViewModel.refreshVehicles(userId, supabaseKey);
        }
    }

    private void initViews() {
        progressBar = findViewById(R.id.pbLoading);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        
        View.OnClickListener addListener = v -> {
            Intent intent = new Intent(this, AddVehicleActivity.class);
            startActivity(intent);
        };

        findViewById(R.id.fabAddVehicle).setOnClickListener(addListener);
        findViewById(R.id.ivAddVehicleHeader).setOnClickListener(addListener);
    }

    private void setupRecyclerView() {
        RecyclerView rvVehicles = findViewById(R.id.rvVehicles);
        rvVehicles.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VehicleAdapter();
        rvVehicles.setAdapter(adapter);
        
        adapter.setOnVehicleClickListener(new VehicleAdapter.OnVehicleClickListener() {
            @Override
            public void onVehicleClick(VehicleEntity vehicle) {
                // Handle vehicle click
            }

            @Override
            public void onDeleteClick(VehicleEntity vehicle) {
                String supabaseKey = getString(R.string.supabase_key);
                vehicleViewModel.deleteVehicle(vehicle.getId(), supabaseKey);
            }
        });
    }

    private void observeViewModel() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            vehicleViewModel.getVehicles(userId).observe(this, vehicles -> {
                if (vehicles != null && !vehicles.isEmpty()) {
                    adapter.setVehicles(vehicles);
                    tvEmptyState.setVisibility(View.GONE);
                } else {
                    tvEmptyState.setVisibility(View.VISIBLE);
                }
            });
        }

        vehicleViewModel.isLoading().observe(this, loading -> {
            if (loading != null) {
                progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            }
        });
    }
}
