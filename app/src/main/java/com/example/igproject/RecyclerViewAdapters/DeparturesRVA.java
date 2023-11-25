package com.example.igproject.RecyclerViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.igproject.LocalData.DeparturesData;
import com.example.igproject.Models.TripStop;
import com.example.igproject.R;

import java.util.ArrayList;
import java.util.List;

public class DeparturesRVA extends RecyclerView.Adapter<DeparturesRVA.MyViewHolder> {

    Context context;

    DeparturesData departuresData;

    public DeparturesRVA(Context context, DeparturesData departures) {
        this.context = context;
        this.departuresData = departures;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_map_departures, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.time.setText(departuresData.departures.get(position).departureTime.substring(0,5));
        holder.route.setText(departuresData.departures.get(position).routeName);
        holder.route.setBackgroundColor(Color.parseColor("#" + departuresData.departures.get(position).routeColor));
        holder.route.setTextColor(Color.parseColor("#" + departuresData.departures.get(position).routeTextColor));
        holder.destination.setText(departuresData.departures.get(position).destination);
        holder.rView.setLayoutManager(new LinearLayoutManager(context));
        holder.rView.setAdapter(new TripStopsRVA(context, departuresData.tripStops.get(position)));

    }

    @Override
    public int getItemCount() {
        return departuresData.departures.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;

        TextView route, time, destination;

        RecyclerView rView;

        List<TripStop> tripStops = new ArrayList<>();

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.departureCard);
            route = itemView.findViewById(R.id.route);
            time = itemView.findViewById(R.id.depTime);
            destination = itemView.findViewById(R.id.destination);
            rView = itemView.findViewById(R.id.rViewTripStops);
            itemView.setOnClickListener((view) -> {
                if(rView.getVisibility() != View.VISIBLE) {
                    rView.setVisibility(View.VISIBLE);
                } else {
                    rView.setVisibility(View.GONE);
                }
            });
        }

    }
}
