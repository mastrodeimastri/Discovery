package com.example.igproject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.os.Handler;
import android.widget.EditText;

import com.example.igproject.Fragments.Navigation.MapsFragment;
import com.example.igproject.Fragments.Profile.ProfileFragment;
import com.example.igproject.Fragments.Weather.WeatherFragment;
import com.example.igproject.Fragments.News.NewsFragment;
import com.example.igproject.LocalData.AttendanceData;
import com.example.igproject.LocalData.MainActivityListener;
import com.example.igproject.LocalData.NewsData;
import com.example.igproject.LocalData.WeatherData;
import com.example.igproject.R;
import com.example.igproject.ViewModels.MapViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements MainActivityListener {

    //Weather data is taken only once from api, main stores it to pass when fragment is loaded
    private WeatherData weatherData;
    private AttendanceData attendanceData;
    private NewsData newsData;



    private MapViewModel mapViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);

        mapViewModel = new MapViewModel(this);

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
        //Loads weather data
        weatherData.loadData();
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
        } else if (id == R.id.map) {
            // Caricamento frammento pagina navigazione
            loadFragment(new MapsFragment(mapViewModel));
        } else {
            loadFragment(new ProfileFragment());
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
            case "map":
                if (getSupportFragmentManager().findFragmentById(R.id.frameLayout) instanceof MapsFragment)
                    replaceFragment(R.id.map);
                break;
            case "profile":
                if (getSupportFragmentManager().findFragmentById(R.id.frameLayout) instanceof MapsFragment)
                    replaceFragment(R.id.profile);
        }
    }

    public void toReportPage(View view) {

    }

    public void onLogInBtnClicked(View view) {
        EditText username = this.findViewById(R.id.usernameLogIn);
        EditText pwd = this.findViewById(R.id.pwdLogIn);

        if(username.getText().length() > 0 && pwd.getText().length() > 0) {
            FirebaseAuth authClient = FirebaseAuth.getInstance();
            authClient.signInWithEmailAndPassword(username.getText().toString(), pwd.getText().toString())
                    .addOnSuccessListener(authResult -> replaceFragment(R.id.profile));
        }
    }
}