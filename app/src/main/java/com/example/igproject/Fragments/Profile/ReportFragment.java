package com.example.igproject.Fragments.Profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.igproject.LocalData.ReportsData;
import com.example.igproject.R;
import com.example.igproject.RecyclerViewAdapters.ReportsRVA;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class ReportFragment extends Fragment {

    private static final String REPORTS_ARGS = "reports";

    private ReportsData reportsData;

    public ReportFragment() {
        // Required empty public constructor
    }

    public static ReportFragment newInstance(ReportsData reportsData) {
        ReportFragment fragment = new ReportFragment();

        Bundle args = new Bundle();
        args.putParcelable(REPORTS_ARGS, reportsData);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_report, container, false);

        RecyclerView rView = view.findViewById(R.id.reportsRecyclerView);
        rView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Reports").get().addOnCompleteListener(t -> {
            List<QueryDocumentSnapshot> reports = new ArrayList<>();
            for(QueryDocumentSnapshot document : t.getResult()) {
                reports.add(document);
            }
            rView.setAdapter(new ReportsRVA(reports));
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button createReportBtn = view.findViewById(R.id.createReportBtn);

        createReportBtn.setOnClickListener( v -> {
            ReportDetailFragment fragment = ReportDetailFragment.newInstance();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack("logIn").commit();
        });
    }
}