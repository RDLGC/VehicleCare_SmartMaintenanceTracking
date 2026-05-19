package com.example.vehiclecare_smartmaintenancetracking;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.vehiclecare_smartmaintenancetracking.models.VehicleEntity;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.ReminderViewModel;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.VehicleViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SetReminderActivity extends AppCompatActivity {
    private ReminderViewModel reminderViewModel;
    private VehicleViewModel vehicleViewModel;
    
    private AutoCompleteTextView actvVehicle;
    private TextInputEditText etType, etMileage, etNotes;
    private MaterialCardView optionMileage, optionDate, optionBoth;
    private List<VehicleEntity> vehicleList = new ArrayList<>();
    private String selectedVehicleId = null;
    private String selectedReminderType = "Mileage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_reminder);

        reminderViewModel = new ViewModelProvider(this).get(ReminderViewModel.class);
        vehicleViewModel = new ViewModelProvider(this).get(VehicleViewModel.class);

        initViews();
        setupTypeSelector();
        loadVehicles();
        observeViewModel();
    }

    private void initViews() {
        actvVehicle = findViewById(R.id.actvReminderVehicle);
        etType = findViewById(R.id.etMaintenanceType);
        etMileage = findViewById(R.id.etMileageInterval);
        etNotes = findViewById(R.id.etReminderNotes);
        
        optionMileage = findViewById(R.id.optionMileage);
        optionDate = findViewById(R.id.optionDate);
        optionBoth = findViewById(R.id.optionBoth);

        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());
        findViewById(R.id.btnSetReminder).setOnClickListener(v -> saveReminder());
    }

    private void setupTypeSelector() {
        optionMileage.setOnClickListener(v -> selectType("Mileage"));
        optionDate.setOnClickListener(v -> selectType("Date"));
        optionBoth.setOnClickListener(v -> selectType("Both"));
        
        selectType("Mileage"); // Default
    }

    private void selectType(String type) {
        selectedReminderType = type;
        
        int selectedColor = 0xFFEBF9F1;
        int unselectedColor = 0xFFFFFFFF;
        int selectedStroke = getResources().getColor(R.color.primary_green);
        int unselectedStroke = getResources().getColor(R.color.border_gray);

        optionMileage.setCardBackgroundColor(type.equals("Mileage") ? selectedColor : unselectedColor);
        optionMileage.setStrokeColor(type.equals("Mileage") ? selectedStroke : unselectedStroke);

        optionDate.setCardBackgroundColor(type.equals("Date") ? selectedColor : unselectedColor);
        optionDate.setStrokeColor(type.equals("Date") ? selectedStroke : unselectedStroke);

        optionBoth.setCardBackgroundColor(type.equals("Both") ? selectedColor : unselectedColor);
        optionBoth.setStrokeColor(type.equals("Both") ? selectedStroke : unselectedStroke);
    }

    private void loadVehicles() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            vehicleViewModel.getVehicles(userId).observe(this, vehicles -> {
                if (vehicles != null) {
                    vehicleList = vehicles;
                    List<String> names = new ArrayList<>();
                    for (VehicleEntity v : vehicles) names.add(v.getName());
                    actvVehicle.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names));
                }
            });
            actvVehicle.setOnItemClickListener((p, v, pos, id) -> selectedVehicleId = vehicleList.get(pos).getId());
        }
    }

    private void saveReminder() {
        String userId = FirebaseAuth.getInstance().getUid();
        String type = etType.getText() != null ? etType.getText().toString().trim() : "";
        String mileageStr = etMileage.getText() != null ? etMileage.getText().toString().trim() : "";
        String notes = etNotes.getText() != null ? etNotes.getText().toString().trim() : "";
        String supabaseKey = getString(R.string.supabase_key);

        if (validate(type, mileageStr)) {
            Integer mileage = mileageStr.isEmpty() ? null : Integer.parseInt(mileageStr);
            reminderViewModel.addReminder(userId, selectedVehicleId, type, selectedReminderType, mileage, null, notes, supabaseKey);
        }
    }

    private boolean validate(String type, String mileage) {
        if (selectedVehicleId == null) {
            Toast.makeText(this, "Select a vehicle", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (type.isEmpty()) {
            Toast.makeText(this, "Enter maintenance type", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void observeViewModel() {
        reminderViewModel.getError().observe(this, error -> {
            if (error != null) Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        });

        reminderViewModel.getAddSuccess().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Reminder set!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        reminderViewModel.isLoading().observe(this, loading -> findViewById(R.id.btnSetReminder).setEnabled(!loading));
    }
}
