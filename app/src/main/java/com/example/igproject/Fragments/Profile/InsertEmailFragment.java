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
import android.widget.Toast;

import com.example.igproject.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InsertEmailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertEmailFragment extends Fragment {

    public InsertEmailFragment() {
        // Required empty public constructor
    }

    public static InsertEmailFragment newInstance() {
        InsertEmailFragment fragment = new InsertEmailFragment();
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
        return inflater.inflate(R.layout.fragment_insert_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button submit = view.findViewById(R.id.submitEmail);

        submit.setOnClickListener( v -> {
            EditText email = view.findViewById(R.id.emailPwd);
            FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
                    .addOnSuccessListener( l -> {
                        Toast.makeText(getActivity(), "Abbiamo ricevuto la tua richiesta di reset password",
                                Toast.LENGTH_LONG).show();
                        LogInFragment fragment = LogInFragment.newInstance();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack("register").commit();
                    });
        });
    }
}