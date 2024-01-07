package com.example.igproject.Fragments.Navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.igproject.LocalData.DeparturesData;
import com.example.igproject.Models.Departure;
import com.example.igproject.Models.StopGroup;
import com.example.igproject.Models.TripStop;
import com.example.igproject.R;
import com.example.igproject.ViewModels.MapViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapsFragment extends Fragment implements GoogleMap.OnMarkerClickListener {
    private static MapViewModel _viewModel;

    private Boolean Modified = false;

    private static Boolean isSync = false;

    private HandlerThread handlerThread;

    private Handler mainHandler;

    private ArrayList<StopGroup> stops;

    private FusedLocationProviderClient fusedLocationClient;

    private GoogleMap map;

    public MapsFragment(MapViewModel viewModel) {

        _viewModel = viewModel;

        handlerThread = new HandlerThread("dbThread");

        handlerThread.start();

        mainHandler = new Handler();
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public synchronized void onMapReady(GoogleMap googleMap) {
            if (!isSync) {
                loadMap(googleMap);
                map = googleMap;
            }

        }

        private void loadMap(GoogleMap googleMap) {
            Handler bgHandler = new Handler(handlerThread.getLooper());
            isSync = true;
            bgHandler.post(() -> {
                stops = new ArrayList<>(_viewModel.renderStops());
                if (stops != null) {
                    mainHandler.post(() -> {
                        renderMarkers(googleMap);
                        isSync = false;
                    });
                }
            });
        }

        private void renderMarkers(GoogleMap googleMap) {

            for (StopGroup s : stops) {
                LatLng stopPos = new LatLng(Double.parseDouble(s.stopLat), Double.parseDouble(s.stopLon));
                googleMap.addMarker(new MarkerOptions().position(stopPos).title(s.stopGroupName));
            }
            googleMap.setOnMarkerClickListener(MapsFragment.this);
            if (getActivity() != null) {
                getActivity().findViewById(R.id.map).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.loading_maps).setVisibility(View.GONE);
                ImageButton button = getActivity().findViewById(R.id.posBtn);
                button.setOnClickListener(v -> {


                    LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                    boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                    if(isNetworkEnabled) {
                        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            getActivity().requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
                        }
                    }

                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(getActivity(), l -> {
                                if (l != null) {
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(l.getLatitude(), l.getLongitude()), 16));
                                }
                            });
                });
            }


            if(!Modified){
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.43713, 12.33265), 16));
            }

        }
        
    };

    @Override
    public void onPause() {
        super.onPause();

        // imposto il flag Modified a true
        // così quando vado a renderizzare la vista rimango sul punto dove avevo messo in pausa
        Modified = true;
    }

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

        // binding del LocationService così da porterlo riusare più avanti
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        renderDeparture(marker.getTitle(), _viewModel.getTodayDate());
        return true;
    }

    private void renderDeparture(String title,  Date date){

        Handler bgHandler = new Handler(handlerThread.getLooper());
        bgHandler.post(() -> {
            ArrayList<Departure> departures = new ArrayList<>(_viewModel.getDepartures(title, date));
            ArrayList<List<TripStop>> tripStops = new ArrayList<>();

            for(Departure departure : departures) {
                tripStops.add(_viewModel.getTripStops(departure.tripId, departure.stopSequence));
            }
            mainHandler.post(()->{
                DeparturesData data = new DeparturesData(departures, tripStops, title);
                DeparturesFragment fragment = DeparturesFragment.newInstance(data);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).addToBackStack("maps").commit();
            });
        });
    }

}