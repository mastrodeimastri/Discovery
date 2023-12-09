package com.example.igproject.RecyclerViewAdapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.igproject.Models.Report;
import com.example.igproject.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.List;

public class ReportsRVA extends RecyclerView.Adapter<ReportsRVA.MyViewHolder> {


    List<Report> reports;

    public ReportsRVA(List<Report> reports) {
        this.reports = reports;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.objectView.setText(reports.get(position).object);
        holder.btn.setOnClickListener((view) -> {
            // TODO: renderizzare la pagina per la modifica e la creazione di un report
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView objectView;
        Button btn;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            objectView = itemView.findViewById(R.id.reportObject);
            btn = itemView.findViewById(R.id.editReportBtn);
        }
    }
}
