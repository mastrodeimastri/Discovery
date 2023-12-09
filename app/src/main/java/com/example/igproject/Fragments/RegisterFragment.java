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

import com.example.igproject.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();

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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button registerBtn = view.findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(v -> {
            FirebaseAuth authClient = FirebaseAuth.getInstance();

            EditText email = view.findViewById(R.id.emailRegister);
            EditText pwd = view.findViewById(R.id.pwdRegister);
            EditText cPwd = view.findViewById(R.id.confirmPwd);

            if(pwd.getText().toString().equals(cPwd.getText().toString()) && pwd.getText().toString().length() > 0) {
                authClient.createUserWithEmailAndPassword(email.getText().toString(), pwd.getText().toString())
                        .addOnSuccessListener( l -> {
                            LogInFragment fragment = LogInFragment.newInstance();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack("register").commit();
                        });
            }


        });
    }
}