package com.example.vehiclecare_smartmaintenancetracking;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.vehiclecare_smartmaintenancetracking.models.VehicleEntity;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.ServiceViewModel;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.VehicleViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddServiceActivity extends AppCompatActivity {
    private ServiceViewModel serviceViewModel;
    private VehicleViewModel vehicleViewModel;
    
    private AutoCompleteTextView actvVehicle;
    private TextInputEditText etType, etDate, etMileage, etCost, etProvider, etNotes;
    private List<VehicleEntity> vehicleList = new ArrayList<>();
    private String selectedVehicleId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_service);

        serviceViewModel = new ViewModelProvider(this).get(ServiceViewModel.class);
        vehicleViewModel = new ViewModelProvider(this).get(VehicleViewModel.class);

        initViews();
        setupDatePicker();
        loadVehicles();
        observeViewModel();
    }

    private void initViews() {
        actvVehicle = findViewById(R.id.actvVehicle);
        etType = findViewById(R.id.etServiceType);
        etDate = findViewById(R.id.etDate);
        etMileage = findViewById(R.id.etMileage);
        etCost = findViewById(R.id.etCost);
        etProvider = findViewById(R.id.etServiceProvider);
        etNotes = findViewById(R.id.etNotes);

        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());
        findViewById(R.id.btnSave).setOnClickListener(v -> saveService());
    }

    private void setupDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        etDate.setText(sdf.format(calendar.getTime()));

        etDate.setOnClickListener(v -> new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            etDate.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void loadVehicles() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            vehicleViewModel.getVehicles(userId).observe(this, vehicles -> {
                if (vehicles != null) {
                    vehicleList = vehicles;
                    List<String> names = new ArrayList<>();
                    for (VehicleEntity v : vehicles) {
                        names.add(v.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
                    actvVehicle.setAdapter(adapter);
                }
            });

            actvVehicle.setOnItemClickListener((parent, view, position, id) -> {
                selectedVehicleId = vehicleList.get(position).getId();
            });
        }
    }

    private void saveService() {
        String type = etType.getText() != null ? etType.getText().toString().trim() : "";
        String date = etDate.getText() != null ? etDate.getText().toString().trim() : "";
        String mileageStr = etMileage.getText() != null ? etMileage.getText().toString().trim() : "";
        String costStr = etCost.getText() != null ? etCost.getText().toString().trim() : "";
        String provider = etProvider.getText() != null ? etProvider.getText().toString().trim() : "";
        String notes = etNotes.getText() != null ? etNotes.getText().toString().trim() : "";
        String supabaseKey = getString(R.string.supabase_key);

        if (validate(type, mileageStr)) {
            int mileage = Integer.parseInt(mileageStr);
            double cost = costStr.isEmpty() ? 0.0 : Double.parseDouble(costStr);
            serviceViewModel.addService(selectedVehicleId, type, date, mileage, cost, provider, notes, supabaseKey);
        }
    }

    private boolean validate(String type, String mileage) {
        if (selectedVehicleId == null) {
            Toast.makeText(this, "Please select a vehicle", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (type.isEmpty()) {
            Toast.makeText(this, "Service type is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mileage.isEmpty()) {
            Toast.makeText(this, "Mileage is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void observeViewModel() {
        serviceViewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });

        serviceViewModel.getAddSuccess().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Service recorded successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        serviceViewModel.isLoading().observe(this, loading -> findViewById(R.id.btnSave).setEnabled(!loading));
    }
}
