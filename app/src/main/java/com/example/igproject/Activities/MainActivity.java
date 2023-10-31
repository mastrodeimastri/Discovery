package com.example.igproject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.accounts.NetworkErrorException;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;

import com.example.igproject.Fragments.WeatherFragment;
import com.example.igproject.Fragments.NewsFragment;
import com.example.igproject.LocalData.MainActivityListener;
import com.example.igproject.LocalData.WeatherData;
import com.example.igproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.igproject.ViewModels.SettingsViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainActivityListener {
    private WeatherData weatherData;


    private SettingsViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new SettingsViewModel(this);

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
        switch (id){
            case "weather":
                if (getSupportFragmentManager().findFragmentById(R.id.frameLayout) instanceof WeatherFragment)
                    replaceFragment(R.id.weather);
                break;
            case "news":
                if (getSupportFragmentManager().findFragmentById(R.id.frameLayout) instanceof NewsFragment)
                    replaceFragment(R.id.news);
                break;
        }
    }
}