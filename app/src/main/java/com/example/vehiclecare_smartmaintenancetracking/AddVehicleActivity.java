package com.example.vehiclecare_smartmaintenancetracking;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.VehicleViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class AddVehicleActivity extends AppCompatActivity {
    private VehicleViewModel vehicleViewModel;
    private String selectedType = "Car"; // Default selection
    private String vehicleId = null;
    private com.example.vehiclecare_smartmaintenancetracking.models.VehicleEntity editingVehicle;
    
    private MaterialCardView cardCar, cardMotorcycle, cardTruck;
    private TextInputEditText etName, etYear, etMileage, etModelTrim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vehicle);

        vehicleViewModel = new ViewModelProvider(this).get(VehicleViewModel.class);

        vehicleId = getIntent().getStringExtra("vehicle_id");

        initViews();
        setupTypeSelector();
        setupActions();
        observeViewModel();

        if (vehicleId != null) {
            loadVehicleData();
        }
    }

    private void loadVehicleData() {
        ((android.widget.TextView) findViewById(R.id.tvTitle)).setText("Edit Vehicle");
        ((com.google.android.material.button.MaterialButton) findViewById(R.id.btnAddVehicle)).setText("Update Vehicle");

        vehicleViewModel.getVehicleById(vehicleId).observe(this, vehicle -> {
            if (vehicle != null && editingVehicle == null) {
                editingVehicle = vehicle;
                etName.setText(vehicle.getName());
                etYear.setText(String.valueOf(vehicle.getYear()));
                etMileage.setText(String.valueOf(vehicle.getMileage()));
                etModelTrim.setText(vehicle.getModelTrim());
                selectType(vehicle.getType());
            }
        });
    }

    private void initViews() {
        cardCar = findViewById(R.id.cardCar);
        cardMotorcycle = findViewById(R.id.cardMotorcycle);
        cardTruck = findViewById(R.id.cardTruck);
        
        etName = findViewById(R.id.etVehicleName);
        etYear = findViewById(R.id.etYear);
        etMileage = findViewById(R.id.etMileage);
        etModelTrim = findViewById(R.id.etModelTrim);
        
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    private void setupTypeSelector() {
        cardCar.setOnClickListener(v -> selectType("Car"));
        cardMotorcycle.setOnClickListener(v -> selectType("Motorcycle"));
        cardTruck.setOnClickListener(v -> selectType("Truck"));
        
        // Initialize visual state
        selectType("Car");
    }

    private void selectType(String type) {
        selectedType = type;
        
        // Update visual states (using basic colors for now)
        int selectedStroke = getResources().getColor(R.color.primary_green);
        int unselectedStroke = getResources().getColor(R.color.border_gray);
        
        cardCar.setStrokeColor(type.equals("Car") ? selectedStroke : unselectedStroke);
        cardMotorcycle.setStrokeColor(type.equals("Motorcycle") ? selectedStroke : unselectedStroke);
        cardTruck.setStrokeColor(type.equals("Truck") ? selectedStroke : unselectedStroke);
        
        cardCar.setCardBackgroundColor(type.equals("Car") ? 0xFFE8F5E9 : 0xFFFFFFFF);
        cardMotorcycle.setCardBackgroundColor(type.equals("Motorcycle") ? 0xFFE8F5E9 : 0xFFFFFFFF);
        cardTruck.setCardBackgroundColor(type.equals("Truck") ? 0xFFE8F5E9 : 0xFFFFFFFF);
    }

    private void setupActions() {
        MaterialButton btnAdd = findViewById(R.id.btnAddVehicle);
        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String yearStr = etYear.getText().toString().trim();
            String mileageStr = etMileage.getText().toString().trim();
            String model = etModelTrim.getText().toString().trim();
            String userId = FirebaseAuth.getInstance().getUid();
            String supabaseKey = getString(R.string.supabase_key);

            if (validate(name, yearStr, userId)) {
                Integer year = yearStr.isEmpty() ? 0 : Integer.parseInt(yearStr);
                Integer mileage = mileageStr.isEmpty() ? 0 : Integer.parseInt(mileageStr);
                
                if (vehicleId == null) {
                    vehicleViewModel.addVehicle(userId, name, selectedType, year, mileage, model, supabaseKey);
                } else if (editingVehicle != null) {
                    editingVehicle.setName(name);
                    editingVehicle.setType(selectedType);
                    editingVehicle.setYear(year);
                    editingVehicle.setMileage(mileage);
                    editingVehicle.setModelTrim(model);
                    vehicleViewModel.updateVehicle(editingVehicle, supabaseKey);
                }
            }
        });
    }

    private boolean validate(String name, String year, String userId) {
        if (userId == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (name.isEmpty()) {
            Toast.makeText(this, "Vehicle name is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void observeViewModel() {
        vehicleViewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });

        vehicleViewModel.isLoading().observe(this, loading -> 
            findViewById(R.id.btnAddVehicle).setEnabled(!loading)
        );

        vehicleViewModel.getAddSuccess().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Vehicle added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
