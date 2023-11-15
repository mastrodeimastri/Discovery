package com.example.igproject.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.igproject.Models.StopGroup;
import com.example.igproject.Models.StopTime;
import com.example.igproject.R;
import com.example.igproject.ViewModels.MapViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class MapsFragment extends Fragment implements GoogleMap.OnMarkerClickListener {
    private static MapViewModel _viewModel;

    private HandlerThread handlerThread;

    private Handler mainHandler;

    private ArrayList<StopGroup> stops;

    private GoogleMap map;

    public MapsFragment(MapViewModel viewModel){

        _viewModel = viewModel;

        handlerThread = new HandlerThread("dbThread");

        handlerThread.start();

        mainHandler = new Handler();
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
            map = googleMap;
            map.setInfoWindowAdapter(new departurePopUp());
        }

        private void loadMap(GoogleMap googleMap) {
            Handler bgHandler = new Handler(handlerThread.getLooper());

            bgHandler.post(() -> {
                //_viewModel.importData();
                stops = new ArrayList<>(_viewModel.renderStops());
                mainHandler.post(() -> {
                    renderMarkers(googleMap);
                });
            });
        }

        private void renderMarkers(GoogleMap googleMap) {
            for (StopGroup s: stops) {
                LatLng stopPos = new LatLng(Double.parseDouble(s.stopLat), Double.parseDouble(s.stopLon));
                googleMap.addMarker(new MarkerOptions().position(stopPos).title(s.stopGroupName));
            }
            googleMap.setOnMarkerClickListener(MapsFragment.this::onMarkerClick);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.43713, 12.33265), 16));
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

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    // classe per definire il popup delle partenze
    private class departurePopUp implements GoogleMap.InfoWindowAdapter {

        //private final View window;
        private final View window;

        departurePopUp() {
            window = getLayoutInflater().inflate(R.layout.departure_popup, null);;
        }

        @Nullable
        @Override
        public View getInfoContents(@NonNull Marker marker) {
            return null;
        }

        @Nullable
        @Override
        public View getInfoWindow(@NonNull Marker marker) {
            renderDeparture(marker, Date.from(Instant.now()));
            return window;
        }

        private void renderDeparture(Marker marker,  Date date){
            String title = marker.getTitle();

            Handler bgHandler = new Handler(handlerThread.getLooper());
            bgHandler.post(() -> {
                ArrayList<StopTime> departures = new ArrayList<>(_viewModel.getStops(title, date));
                mainHandler.post(() -> {
                    for (StopTime departure: departures) {
                        TableRow row = new TableRow(window.getContext());
                        row.addView(new TextView(window.getContext()));
                    }
                });
            });
        }
    }
}