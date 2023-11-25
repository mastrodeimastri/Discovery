package com.example.igproject.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.igproject.LocalData.DeparturesData;
import com.example.igproject.Models.Departure;
import com.example.igproject.Models.StopGroup;
import com.example.igproject.Models.StopTime;
import com.example.igproject.Models.TripStop;
import com.example.igproject.R;
import com.example.igproject.RecyclerViewAdapters.DeparturesRVA;
import com.example.igproject.ViewModels.MapViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.sql.Time;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;

public class MapsFragment extends Fragment implements GoogleMap.OnMarkerClickListener {
    private static MapViewModel _viewModel;

    private Boolean Modified = false;

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

        @Override
        public synchronized void onMapReady(GoogleMap googleMap) {
            loadMap(googleMap);
            map = googleMap;
        }

        private void loadMap(GoogleMap googleMap) {
            Handler bgHandler = new Handler(handlerThread.getLooper());

            bgHandler.post(() -> {
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
            getActivity().findViewById(R.id.map).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.loading_maps).setVisibility(View.GONE);

            if(!Modified){
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.43713, 12.33265), 16));
            }

        }
        
    };

    @Override
    public void onPause() {
        super.onPause();
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