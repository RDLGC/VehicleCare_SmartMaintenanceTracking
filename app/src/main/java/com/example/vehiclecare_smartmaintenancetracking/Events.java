package com.example.vehiclecare_smartmaintenancetracking;

import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Events {
    public static void setupLoginEvents(final AppCompatActivity activity) {
        TextView tvSignUp = activity.findViewById(R.id.tvSignUp);
        if (tvSignUp != null) {
            tvSignUp.setOnClickListener(v -> {
                Intent intent = new Intent(activity, CreateAccountActivity.class);
                activity.startActivity(intent);
            });
        }
    }
}
