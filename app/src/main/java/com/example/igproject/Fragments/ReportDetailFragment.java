package com.example.igproject.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.igproject.Models.Report;
import com.example.igproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportDetailFragment extends Fragment {

    // TODO: aggiungere il paramentro per passare l'uid del report da modificare

    public ReportDetailFragment() {
        // Required empty public constructor
    }

    public static ReportDetailFragment newInstance() {
        ReportDetailFragment fragment = new ReportDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button createUpdateBtn = view.findViewById(R.id.createUpdateBtn);

        createUpdateBtn.setOnClickListener( v -> {
            FirebaseFirestore firestoreClient = FirebaseFirestore.getInstance();

            TextView uid = view.findViewById(R.id.reportId);

            EditText object = view.findViewById(R.id.objectDetail);
            EditText body = view.findViewById(R.id.bodyDetail);

            if(uid.getText().toString().length() > 0 ) {

            } else {

            }

            firestoreClient.collection("Users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("Reports").add(new Report(object.getText().toString(),
                            body.getText().toString())).addOnSuccessListener( l -> {
                            ProfileFragment fragment = ProfileFragment.newInstance();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack("logIn").commit();
                        });
        });
    }
}