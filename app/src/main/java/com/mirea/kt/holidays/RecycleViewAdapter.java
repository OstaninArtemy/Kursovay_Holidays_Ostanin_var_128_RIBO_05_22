package com.mirea.kt.holidays;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.HolidayViewHolder> {
    private List<HolidaysCountry> holidays;
    public RecycleViewAdapter(List<HolidaysCountry> holidays) {
        this.holidays = holidays;
    }
    @NonNull
    @Override
    public HolidayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_holidays_country, parent, false);
        return new HolidayViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HolidayViewHolder holder, int position) {
        HolidaysCountry holiday = holidays.get(position);
        holder.bind(holiday);
    }
    @Override
    public int getItemCount() {
        return holidays.size();
    }
    public static class HolidayViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewHoliday;
        public HolidayViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHoliday = itemView.findViewById(R.id.textViewHoliday);
        }

        public void bind(HolidaysCountry holiday) {
            textViewHoliday.setText(holiday.getName()+"-"+holiday.getDate());
        }
    }
}

