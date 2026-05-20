package com.example.vehiclecare_smartmaintenancetracking.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vehiclecare_smartmaintenancetracking.R;
import com.example.vehiclecare_smartmaintenancetracking.models.VehicleEntity;
import java.util.ArrayList;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {
    private List<VehicleEntity> vehicles = new ArrayList<>();
    private OnVehicleClickListener listener;

    public interface OnVehicleClickListener {
        void onVehicleClick(VehicleEntity vehicle);
        void onEditClick(VehicleEntity vehicle);
        void onDeleteClick(VehicleEntity vehicle);
    }

    public void setOnVehicleClickListener(OnVehicleClickListener listener) {
        this.listener = listener;
    }

    public void setVehicles(List<VehicleEntity> vehicles) {
        this.vehicles = vehicles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vehicle, parent, false);
        return new VehicleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        VehicleEntity current = vehicles.get(position);
        holder.tvName.setText(current.getName());
        holder.tvDetail.setText(String.format("%s • %s", current.getType(), current.getModelTrim()));
        holder.tvMileage.setText(String.format("%,d miles", current.getMileage()));

        // Set icon based on type
        int iconRes = R.drawable.ic_car;
        if ("Motorcycle".equalsIgnoreCase(current.getType())) {
            iconRes = android.R.drawable.ic_menu_directions; // Fallback or custom icon
        } else if ("Truck".equalsIgnoreCase(current.getType())) {
            iconRes = android.R.drawable.ic_menu_send; // Fallback or custom icon
        }
        holder.ivIcon.setImageResource(iconRes);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onVehicleClick(current);
            }
        });

        holder.ivEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(current);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(current);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    class VehicleViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName, tvDetail, tvMileage;
        private final ImageView ivIcon, ivEdit;

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvVehicleName);
            tvDetail = itemView.findViewById(R.id.tvVehicleDetail);
            tvMileage = itemView.findViewById(R.id.tvMileage);
            ivIcon = itemView.findViewById(R.id.ivVehicleIcon);
            ivEdit = itemView.findViewById(R.id.ivEditVehicle);
        }
    }
}
