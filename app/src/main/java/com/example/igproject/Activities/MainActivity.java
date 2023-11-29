package com.example.igproject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.igproject.Fragments.WeatherFragment;
import com.example.igproject.Fragments.NewsFragment;
import com.example.igproject.LocalData.AttendanceData;
import com.example.igproject.LocalData.MainActivityListener;
import com.example.igproject.LocalData.NewsData;
import com.example.igproject.LocalData.WeatherData;
import com.example.igproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements MainActivityListener {

    //Weather data is taken only once from api, main stores it to pass when fragment is loaded
    private WeatherData weatherData;
    private AttendanceData attendanceData;
    private NewsData newsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //App starting animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.frameLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.bottomNavigationView).setVisibility(View.VISIBLE);
                findViewById(R.id.logoMotionLayout).setVisibility(View.GONE);
            }
        }, 3000);

        //Load weather and news data from api
        getData();

        //First fragment to show
        replaceFragment(R.id.news);

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
        //Listener to update the ui when the load is completed
        weatherData.setMainActivityListener(this);
        //Loads weather data
        weatherData.loadData();

        attendanceData = new AttendanceData();
        attendanceData.setMainActivityListener(this);
        attendanceData.loadData();

        newsData = new NewsData();
        newsData.setMainActivityListener(this);
        newsData.loadData();
    }

    //Changes the current fragment based on menu input or when the ui updates
    public void replaceFragment(int id){
        if (id == R.id.news){
            NewsFragment newsFragment = NewsFragment.newInstance(attendanceData, newsData);
            loadFragment(newsFragment);
        }
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