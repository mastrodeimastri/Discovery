package com.example.igproject.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.igproject.Models.Stop;
import com.example.igproject.Models.StopGroup;
import com.example.igproject.R;
import com.example.igproject.ViewModels.MapViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.checkerframework.common.initializedfields.qual.EnsuresInitializedFields;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapsFragment extends Fragment {

    private static MapViewModel _viewModel;

    private HandlerThread handlerThread;

    private Handler mainHandler;
    private ArrayList<StopGroup> stops;

    public MapsFragment(MapViewModel viewModel){

        _viewModel = viewModel;

        handlerThread = new HandlerThread("dbThread");
        handlerThread.start();

        mainHandler = new Handler();

        stops = new ArrayList<>();
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public synchronized void onMapReady(GoogleMap googleMap) {
            loadMap(googleMap);
        }

        private void loadMap(GoogleMap googleMap) {
            Handler bgHandler = new Handler(handlerThread.getLooper());

            bgHandler.post(() -> {
                //_viewModel.importData();
                for (StopGroup stop : _viewModel.renderStops()) {
                    stops.add(stop);
                }
                mainHandler.post(() -> {
                    renderStops(googleMap);
                });

            });
        }

        private void renderStops(GoogleMap googleMap) {
            for (StopGroup s: stops) {
                LatLng stopPos = new LatLng(Double.parseDouble(s.stopLat), Double.parseDouble(s.stopLon));
                googleMap.addMarker(new MarkerOptions().position(stopPos).title(s.stopGroupName));
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(45.43713, 12.33265)));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}