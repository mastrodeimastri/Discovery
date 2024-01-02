package com.example.igproject.Fragments.Profile;

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

import com.example.igproject.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {

    private FirebaseAuth authClient;

    private Button logInbtn;

    public LogInFragment() {
        // Required empty public constructor
    }

    public static LogInFragment newInstance() {
        LogInFragment fragment = new LogInFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authClient = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logInbtn = view.findViewById(R.id.loginBtn);

        logInbtn.setOnClickListener((v) -> {
            EditText usr = view.findViewById(R.id.usernameLogIn);
            EditText pwd = view.findViewById(R.id.pwdLogIn);
            authClient.signInWithEmailAndPassword(
                    usr.getText().toString(),
                    pwd.getText().toString()).addOnSuccessListener( l -> {
                        ProfileFragment fragment = ProfileFragment.newInstance();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack("logIn").commit();
                    });
        });

        TextView registerLink = view.findViewById(R.id.registerLink);

        registerLink.setOnClickListener( v -> {
            RegisterFragment fragment = RegisterFragment.newInstance();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack("logIn").commit();
        });

        TextView forgivePwd = view.findViewById(R.id.forgivePwd);

        forgivePwd.setOnClickListener( v -> {
            InsertEmailFragment fragment = InsertEmailFragment.newInstance();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack("logIn").commit();
        });


    }
}