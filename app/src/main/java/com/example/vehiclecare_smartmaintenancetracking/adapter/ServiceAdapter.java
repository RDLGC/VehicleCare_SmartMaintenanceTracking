package com.example.vehiclecare_smartmaintenancetracking.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vehiclecare_smartmaintenancetracking.R;
import com.example.vehiclecare_smartmaintenancetracking.models.ServiceEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private List<ServiceEntity> services = new ArrayList<>();
    private OnServiceLongClickListener longClickListener;

    public interface OnServiceLongClickListener {
        void onServiceLongClick(ServiceEntity service);
    }

    public void setOnServiceLongClickListener(OnServiceLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setServices(List<ServiceEntity> services) {
        this.services = services;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceEntity service = services.get(position);
        holder.tvName.setText(service.getServiceType());
        holder.tvCost.setText(String.format(Locale.US, "$ %.2f", service.getCost()));
        holder.tvDate.setText(service.getServiceDate());
        holder.tvProvider.setText(service.getProvider());
        holder.tvMileage.setText(String.format(Locale.US, "%,d miles", service.getMileage()));

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onServiceLongClick(service);
                return true;
            }
            return false;
        });
        
        holder.tvVehicle.setText("Vehicle ID: " + (service.getVehicleId() != null && service.getVehicleId().length() > 8 
                ? service.getVehicleId().substring(0, 8) + "..." : service.getVehicleId()));
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvVehicle, tvCost, tvDate, tvProvider, tvMileage;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvServiceName);
            tvVehicle = itemView.findViewById(R.id.tvVehicleName);
            tvCost = itemView.findViewById(R.id.tvServiceCost);
            tvDate = itemView.findViewById(R.id.tvServiceDate);
            tvProvider = itemView.findViewById(R.id.tvServiceProvider);
            tvMileage = itemView.findViewById(R.id.tvServiceMileage);
        }
    }
}
