package com.example.vehiclecare_smartmaintenancetracking;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vehiclecare_smartmaintenancetracking.adapter.ServiceAdapter;
import com.example.vehiclecare_smartmaintenancetracking.models.ServiceEntity;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.ServiceViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import android.text.Editable;
import android.text.TextWatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServiceHistoryActivity extends AppCompatActivity {
    private ServiceViewModel serviceViewModel;
    private ServiceAdapter adapter;
    private TextView tvTotalServices, tvTotalSpent, tvEmpty;
    private List<ServiceEntity> allServices = new ArrayList<>();
    private String currentSearch = "";
    private String currentFilter = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_history);

        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);

        initViews();
        setupRecyclerView();
        setupFilters();
        observeViewModel();
        
        String supabaseKey = getString(R.string.supabase_key);
        serviceViewModel.refreshServices(supabaseKey);
    }

    private void initViews() {
        tvTotalServices = findViewById(R.id.tvTotalServices);
        tvTotalSpent = findViewById(R.id.tvTotalSpent);
        tvEmpty = findViewById(R.id.tvEmptyHistory);
        
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());

        TextInputEditText etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                currentSearch = s.toString().toLowerCase();
                applyFilters();
            }
        });
    }

    private void setupFilters() {
        ChipGroup chipGroup = findViewById(R.id.chipGroupFilters);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty() || checkedIds.get(0) == R.id.chipAll) {
                currentFilter = "All";
            } else {
                Chip chip = findViewById(checkedIds.get(0));
                currentFilter = chip.getText().toString();
            }
            applyFilters();
        });
    }

    private void setupRecyclerView() {
        RecyclerView rv = findViewById(R.id.rvServiceHistory);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ServiceAdapter();
        adapter.setOnServiceLongClickListener(service -> {
            String supabaseKey = getString(R.string.supabase_key);
            serviceViewModel.deleteService(service.getId(), supabaseKey);
        });
        rv.setAdapter(adapter);
    }

    private void observeViewModel() {
        serviceViewModel.getServices().observe(this, services -> {
            if (services != null) {
                allServices = services;
                applyFilters();
            }
        });

        serviceViewModel.getTotalSpent().observe(this, total -> {
            if (total != null) {
                tvTotalSpent.setText(String.format(Locale.US, "$ %.2f", total));
            } else {
                tvTotalSpent.setText("$ 0.00");
            }
        });
    }

    private void applyFilters() {
        List<ServiceEntity> filtered = new ArrayList<>();
        for (ServiceEntity s : allServices) {
            boolean matchesSearch = s.getServiceType().toLowerCase().contains(currentSearch) ||
                                  (s.getProvider() != null && s.getProvider().toLowerCase().contains(currentSearch));
            
            boolean matchesChip = currentFilter.equals("All") ||
                                 (currentFilter.equals("Oil Changes") && s.getServiceType().contains("Oil")) ||
                                 (currentFilter.equals("Tire Services") && s.getServiceType().contains("Tire")) ||
                                 (currentFilter.equals("Brake Services") && s.getServiceType().contains("Brake")) ||
                                 (currentFilter.equals("Inspections") && s.getServiceType().contains("Inspection"));

            if (matchesSearch && matchesChip) {
                filtered.add(s);
            }
        }

        adapter.setServices(filtered);
        tvTotalServices.setText(String.valueOf(filtered.size()));
        tvEmpty.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
