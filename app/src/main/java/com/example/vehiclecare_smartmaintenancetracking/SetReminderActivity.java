package com.example.vehiclecare_smartmaintenancetracking;

import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.example.vehiclecare_smartmaintenancetracking.fcm.ReminderWorker;
import com.example.vehiclecare_smartmaintenancetracking.models.VehicleEntity;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.ReminderViewModel;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.VehicleViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SetReminderActivity extends AppCompatActivity {
    private ReminderViewModel reminderViewModel;
    private VehicleViewModel vehicleViewModel;
    
    private AutoCompleteTextView actvVehicle;
    private TextInputEditText etType, etMileage, etDate, etNotes;
    private MaterialCardView optionMileage, optionDate, optionBoth;
    private View llMileageInput, llDateInput;
    private List<VehicleEntity> vehicleList = new ArrayList<>();
    private String selectedVehicleId = null;
    private String selectedReminderType = "Mileage";

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    Toast.makeText(this, "Notifications are disabled. You won't receive alerts.", Toast.LENGTH_SHORT).show();
                }
            });

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
        checkNotificationPermission();
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void initViews() {
        actvVehicle = findViewById(R.id.actvReminderVehicle);
        etType = findViewById(R.id.etMaintenanceType);
        etMileage = findViewById(R.id.etMileageInterval);
        etDate = findViewById(R.id.etReminderDate);
        etNotes = findViewById(R.id.etReminderNotes);
        
        optionMileage = findViewById(R.id.optionMileage);
        optionDate = findViewById(R.id.optionDate);
        optionBoth = findViewById(R.id.optionBoth);

        llMileageInput = findViewById(R.id.llMileageInputSection);
        llDateInput = findViewById(R.id.llDateInputSection);

        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());
        findViewById(R.id.btnSetReminder).setOnClickListener(v -> saveReminder());

        setupDatePicker();
    }

    private void setupDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        etDate.setOnClickListener(v -> new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            etDate.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());
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

        // Update input section visibility
        llMileageInput.setVisibility((type.equals("Mileage") || type.equals("Both")) ? View.VISIBLE : View.GONE);
        llDateInput.setVisibility((type.equals("Date") || type.equals("Both")) ? View.VISIBLE : View.GONE);
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
        String date = etDate.getText() != null ? etDate.getText().toString().trim() : "";
        String notes = etNotes.getText() != null ? etNotes.getText().toString().trim() : "";
        String supabaseKey = getString(R.string.supabase_key);

        if (validate(type, mileageStr, date)) {
            Integer mileage = (selectedReminderType.equals("Mileage") || selectedReminderType.equals("Both")) && !mileageStr.isEmpty() 
                    ? Integer.parseInt(mileageStr) : null;
            String selectedDate = (selectedReminderType.equals("Date") || selectedReminderType.equals("Both")) && !date.isEmpty() 
                    ? date : null;
            
            reminderViewModel.addReminder(userId, selectedVehicleId, type, selectedReminderType, mileage, selectedDate, notes, supabaseKey);

            if (selectedDate != null) {
                scheduleLocalNotification(type, selectedDate);
            }
        }
    }

    private void scheduleLocalNotification(String maintenanceType, String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date reminderDate = sdf.parse(dateStr);
            if (reminderDate != null) {
                long currentTime = System.currentTimeMillis();
                long reminderTime = reminderDate.getTime();
                
                // Set to 9 AM of that day
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(reminderDate);
                calendar.set(Calendar.HOUR_OF_DAY, 9);
                calendar.set(Calendar.MINUTE, 0);
                long delay = calendar.getTimeInMillis() - currentTime;

                if (delay > 0) {
                    Data inputData = new Data.Builder()
                            .putString("title", "Maintenance Due: " + maintenanceType)
                            .putString("body", "It's time for your scheduled " + maintenanceType.toLowerCase() + ".")
                            .build();

                    OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                            .setInputData(inputData)
                            .build();

                    WorkManager.getInstance(this).enqueue(workRequest);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean validate(String type, String mileage, String date) {
        if (selectedVehicleId == null) {
            Toast.makeText(this, "Select a vehicle", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (type.isEmpty()) {
            Toast.makeText(this, "Enter maintenance type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ((selectedReminderType.equals("Mileage") || selectedReminderType.equals("Both")) && mileage.isEmpty()) {
            Toast.makeText(this, "Mileage interval is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ((selectedReminderType.equals("Date") || selectedReminderType.equals("Both")) && date.isEmpty()) {
            Toast.makeText(this, "Date is required", Toast.LENGTH_SHORT).show();
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
