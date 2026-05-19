package com.example.vehiclecare_smartmaintenancetracking.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vehiclecare_smartmaintenancetracking.R;
import com.example.vehiclecare_smartmaintenancetracking.models.ReminderEntity;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {
    private List<ReminderEntity> reminders = new ArrayList<>();

    public void setReminders(List<ReminderEntity> reminders) {
        this.reminders = reminders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        ReminderEntity reminder = reminders.get(position);
        holder.bind(reminder);
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    static class ReminderViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc, tvBadge, tvDetail;
        MaterialCardView card, iconContainer;
        ImageView ivIcon;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvReminderTitle);
            tvDesc = itemView.findViewById(R.id.tvReminderDesc);
            tvBadge = itemView.findViewById(R.id.tvReminderBadge);
            tvDetail = itemView.findViewById(R.id.tvReminderDetail);
            card = (MaterialCardView) itemView;
            iconContainer = itemView.findViewById(R.id.cvIconContainer);
            ivIcon = itemView.findViewById(R.id.ivReminderIcon);
        }

        public void bind(ReminderEntity reminder) {
            tvTitle.setText(reminder.getMaintenanceType());
            tvDesc.setText(reminder.getNotes() != null && !reminder.getNotes().isEmpty() ? reminder.getNotes() : "Maintenance reminder");
            
            String detail = "";
            if ("Mileage".equals(reminder.getReminderType())) {
                detail = "Every " + reminder.getMileageInterval() + " miles";
            } else if ("Date".equals(reminder.getReminderType())) {
                detail = "On " + reminder.getSpecificDate();
            } else {
                detail = reminder.getMileageInterval() + " miles or " + reminder.getSpecificDate();
            }
            tvDetail.setText(detail);

            // Simple logic for "Urgent" vs "Info" - can be expanded
            if ("Mileage".equals(reminder.getReminderType())) {
                // Urgent style
                card.setCardBackgroundColor(0xFFFFF0F0);
                card.setStrokeColor(0xFFFFCDD2);
                iconContainer.setCardBackgroundColor(0xFFFFFFFF);
                ivIcon.setImageResource(android.R.drawable.ic_menu_manage);
                ivIcon.setColorFilter(0xFFFF3B30);
                tvBadge.setText("URGENT");
                tvBadge.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFFF3B30));
            } else {
                // Info style
                card.setCardBackgroundColor(0xFFF0F9FF);
                card.setStrokeColor(0xFFB3E5FC);
                iconContainer.setCardBackgroundColor(0xFFFFFFFF);
                ivIcon.setImageResource(android.R.drawable.ic_popup_reminder);
                ivIcon.setColorFilter(0xFF0288D1);
                tvBadge.setText("INFO");
                tvBadge.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFE1F5FE));
                tvBadge.setTextColor(0xFF0288D1);
            }
        }
    }
}
