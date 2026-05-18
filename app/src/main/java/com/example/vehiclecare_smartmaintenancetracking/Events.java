package com.example.vehiclecare_smartmaintenancetracking;

import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.vehiclecare_smartmaintenancetracking.viewmodel.AuthViewModel;

public class Events {
    public static void setupLoginEvents(final AppCompatActivity activity) {
        AuthViewModel authViewModel = new ViewModelProvider(activity).get(AuthViewModel.class);
        String supabaseKey = activity.getString(R.string.supabase_key);

        TextView tvSignUp = activity.findViewById(R.id.tvSignUp);
        if (tvSignUp != null) {
            tvSignUp.setOnClickListener(v -> {
                Intent intent = new Intent(activity, CreateAccountActivity.class);
                activity.startActivity(intent);
            });
        }

        View btnSignIn = activity.findViewById(R.id.btnSignIn);
        if (btnSignIn != null) {
            btnSignIn.setOnClickListener(v -> {
                String email = getEmailFromLayout(activity, R.id.emailInput);
                String password = getPasswordFromLayout(activity, R.id.passwordInput);

                if (validateInput(email, password)) {
                    authViewModel.signIn(email, password, supabaseKey);
                }
            });
        }
    }

    public static String getTextFromContainer(AppCompatActivity activity, int containerId) {
        View container = activity.findViewById(containerId);
        if (container instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) container;
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (child instanceof EditText) {
                    return ((EditText) child).getText().toString();
                }
            }
        }
        return "";
    }

    private static String getEmailFromLayout(AppCompatActivity activity, int containerId) {
        return getTextFromContainer(activity, containerId);
    }

    private static String getPasswordFromLayout(AppCompatActivity activity, int containerId) {
        return getEmailFromLayout(activity, containerId);
    }

    private static boolean validateInput(String email, String password) {
        return !email.isEmpty() && !password.isEmpty();
    }

    public static void setupAddVehicleEntry(final AppCompatActivity activity) {
        // This is a placeholder for wherever the user triggers the "Add Vehicle" screen
        // For example, if there's a button in MainActivity or a Dashboard
    }
}
