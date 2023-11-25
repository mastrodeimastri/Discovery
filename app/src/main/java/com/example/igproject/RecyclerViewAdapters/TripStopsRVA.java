package com.example.igproject.RecyclerViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.igproject.Models.TripStop;
import com.example.igproject.R;

import java.util.ConcurrentModificationException;
import java.util.List;

public class TripStopsRVA extends RecyclerView.Adapter<TripStopsRVA.MyViewHolder> {

    private Context context;
    private List<TripStop> tripStops;

    public TripStopsRVA(Context context, List<TripStop> tripStops) {
        this.context = context;
        this.tripStops = tripStops;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_trip_stops, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tripStop.setText(tripStops.get(position).stopName);
        holder.tripTime.setText(tripStops.get(position).departureTime.substring(0,5));
    }

    @Override
    public int getItemCount() {
        return tripStops.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView tripStop, tripTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tripStop = itemView.findViewById(R.id.tripStop);
            tripTime = itemView.findViewById(R.id.tripTime);
        }
    }
}
