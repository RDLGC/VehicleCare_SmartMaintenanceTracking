package com.example.vehiclecare_smartmaintenancetracking;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vehiclecare_smartmaintenancetracking.adapter.ServiceAdapter;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.ServiceViewModel;
import java.util.Locale;

public class ServiceHistoryActivity extends AppCompatActivity {
    private ServiceViewModel serviceViewModel;
    private ServiceAdapter adapter;
    private TextView tvTotalServices, tvTotalSpent, tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_history);

        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);

        initViews();
        setupRecyclerView();
        observeViewModel();
        
        String supabaseKey = getString(R.string.supabase_key);
        serviceViewModel.refreshServices(supabaseKey);
    }

    private void initViews() {
        tvTotalServices = findViewById(R.id.tvTotalServices);
        tvTotalSpent = findViewById(R.id.tvTotalSpent);
        tvEmpty = findViewById(R.id.tvEmptyHistory);
        
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        RecyclerView rv = findViewById(R.id.rvServiceHistory);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ServiceAdapter();
        rv.setAdapter(adapter);
    }

    private void observeViewModel() {
        serviceViewModel.getServices().observe(this, services -> {
            if (services != null && !services.isEmpty()) {
                adapter.setServices(services);
                tvTotalServices.setText(String.valueOf(services.size()));
                tvEmpty.setVisibility(View.GONE);
            } else {
                tvEmpty.setVisibility(View.VISIBLE);
                tvTotalServices.setText("0");
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
}
