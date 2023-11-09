package com.example.igproject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.igproject.Fragments.WeatherFragment;
import com.example.igproject.Fragments.NewsFragment;
import com.example.igproject.LocalData.MainActivityListener;
import com.example.igproject.LocalData.WeatherData;
import com.example.igproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements MainActivityListener {

    //Weather data is taken only once from api, main stores it to pass when fragment is loaded
    private WeatherData weatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Load weather and news data from api
        getData();

        //Setup bottom menu, to move between fragments (news, weather, profile, map)
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

    //Changes the current fragment based on menu input or when the ui updates
    public void replaceFragment(int id){
        if (id == R.id.news)
            loadFragment(new NewsFragment());
        else if (id == R.id.weather){
            //New fragment every time, not that expensive and it refreshes (useful for async pull of data)
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
            //The ifs check if the fragment is active, if it isn't then there's no need to update the ui
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