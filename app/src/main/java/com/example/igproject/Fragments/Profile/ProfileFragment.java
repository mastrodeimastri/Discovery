package com.example.igproject.Fragments.Profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.igproject.Fragments.Profile.Guide.GuideFragment;
import com.example.igproject.LocalData.ReportsData;
import com.example.igproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    FirebaseAuth authClient;

    public ProfileFragment() {
        // Required empty public constructor
        authClient = FirebaseAuth.getInstance();
    }


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button guideBtn = view.findViewById(R.id.guideBtn);
        guideBtn.setOnClickListener( v -> {
            GuideFragment fragment = GuideFragment.newInstance();
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .addToBackStack("profile")
                    .commit();
        });

        Button logInBtn = view.findViewById(R.id.loginBtn);
        logInBtn.setOnClickListener( v -> {
            LogInFragment fragment = LogInFragment.newInstance();
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .addToBackStack("profile")
                    .commit();
        });

        Button logOutBtn = view.findViewById(R.id.logoutBtn);
        logOutBtn.setOnClickListener( v -> {
            FirebaseAuth authClient = FirebaseAuth.getInstance();
            authClient.signOut();
        });

        Button reportBtn = view.findViewById(R.id.reportBtn);
        reportBtn.setOnClickListener( v -> {
            // TODO: creare il un servizio che mi faccia le operazioni CRUD per i report
            ReportsData reportsData = new ReportsData(new ArrayList<>());
            ReportFragment fragment = ReportFragment.newInstance(reportsData);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack("profile").commit();
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(user != null) {
                view.findViewById(R.id.loginBtn).setVisibility(View.GONE);
                view.findViewById(R.id.logoutBtn).setVisibility(View.VISIBLE);
                view.findViewById(R.id.reportBtn).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.loginBtn).setVisibility(View.VISIBLE);
                view.findViewById(R.id.logoutBtn).setVisibility(View.GONE);
                view.findViewById(R.id.reportBtn).setVisibility(View.GONE);
            }
        });

        if(authClient.getCurrentUser() != null) {
            view.findViewById(R.id.loginBtn).setVisibility(View.GONE);
            view.findViewById(R.id.logoutBtn).setVisibility(View.VISIBLE);
            view.findViewById(R.id.reportBtn).setVisibility(View.VISIBLE);
        }
    }
}