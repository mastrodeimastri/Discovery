package com.example.igproject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.igproject.Fragments.MapsFragment;
import com.example.igproject.Fragments.WeatherFragment;
import com.example.igproject.Fragments.NewsFragment;
import com.example.igproject.LocalData.MainActivityListener;
import com.example.igproject.LocalData.WeatherData;
import com.example.igproject.R;
import com.example.igproject.ViewModels.MapViewModel;
import com.google.android.gms.maps.MapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements MainActivityListener {
    private WeatherData weatherData;



    private MapViewModel mapViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapViewModel = new MapViewModel(this);

        setContentView(R.layout.activity_main);
        getData();

        BottomNavigationView bottomMenu = findViewById(R.id.bottomNavigationView);
        bottomMenu.setOnItemSelectedListener(item -> {
            replaceFragment(item.getItemId());
            return true;
        });
    }

    //Async load of news and weather data
    private void getData(){
        weatherData = new WeatherData();
        //Loads weather data
        weatherData.loadData();
        //Listener to update the ui when the load is completed
        weatherData.setMainActivityListener(this);
    }

    //Change the current fragment based on menu input or update
    public void replaceFragment(int id){

        if (id == R.id.news)
            loadFragment(new NewsFragment());
        else if (id == R.id.weather){
            WeatherFragment weatherFragment = WeatherFragment.newInstance(weatherData);
            loadFragment(weatherFragment);
        } else if (id == R.id.map) {
            // Caricamento frammento pagina navigazione
            loadFragment(new MapsFragment(mapViewModel));
        } else {
            //caricamentp pagina profilo
        }
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    //Listener activation to update ui
    @Override
    public void onDataUpdates(String id) {
        switch (id) {
            case "weather":
                if (getSupportFragmentManager().findFragmentById(R.id.frameLayout) instanceof WeatherFragment)
                    replaceFragment(R.id.weather);
                break;
            case "news":
                if (getSupportFragmentManager().findFragmentById(R.id.frameLayout) instanceof NewsFragment)
                    replaceFragment(R.id.news);
                break;
            case "map":
                if (getSupportFragmentManager().findFragmentById(R.id.frameLayout) instanceof MapsFragment)
                    replaceFragment(R.id.map);
                break;
        }
    }

}