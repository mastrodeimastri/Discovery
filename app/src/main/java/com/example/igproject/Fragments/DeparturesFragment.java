package com.example.igproject.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.igproject.LocalData.DeparturesData;
import com.example.igproject.Models.Departure;
import com.example.igproject.Models.TripStop;
import com.example.igproject.R;
import com.example.igproject.RecyclerViewAdapters.DeparturesRVA;
import com.example.igproject.ViewModels.MapViewModel;

import java.util.List;


public class DeparturesFragment extends Fragment {

    private static final String DEPARTURES_ARG = "departures";
    private DeparturesData departuresData;

    private MapViewModel _viewModel;

    public DeparturesFragment() {
        // Required empty public constructor
    }

    public static DeparturesFragment newInstance(DeparturesData departureList) {
        DeparturesFragment fragment = new DeparturesFragment();
        Bundle args = new Bundle();
        args.putParcelable(DEPARTURES_ARG, departureList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            departuresData = getArguments().getParcelable(DEPARTURES_ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_departures, container, false);

        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                if(fragmentManager.getBackStackEntryCount() != 0) {
                    fragmentManager.popBackStack();
                }
                return true;
            }
            return false;
        });
        TextView textView = view.findViewById(R.id.stopName);
        textView.setText(departuresData.stopName);
        RecyclerView rView = view.findViewById(R.id.departuresRecyclerView);

        rView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rView.setAdapter(new DeparturesRVA(getContext(), departuresData));
        // Inflate the layout for this fragment
        return view;
    }
}