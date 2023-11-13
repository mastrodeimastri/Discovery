package com.example.igproject.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.igproject.LocalData.Weather;
import com.example.igproject.LocalData.WeatherData;
import com.example.igproject.R;
import com.example.igproject.RecyclerViewAdapters.OnClickRVAListener;
import com.example.igproject.RecyclerViewAdapters.WeatherDaysRVA;
import com.example.igproject.RecyclerViewAdapters.WeatherHoursRVA;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class WeatherFragment extends Fragment implements OnClickRVAListener {

    private static final String ARG_WEATHER = "weatherData";
    private WeatherData weatherData;
    private WeatherHoursRVA weatherHoursRVA;
    private WeatherDaysRVA weatherDaysRVA;
    private TextView tempNow, timeNow, dayNow;
    private ImageView weatherNow;
    private int currentHour;

    private final Handler handler = new Handler();
    //To keep the clock in the ui up to date, every second the handler updates it
    private final Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            //Check if time changed and set text view
            updateCurrentTime();
            handler.postDelayed(this, 1000); // Update every 1 second
        }
    };

    public WeatherFragment() {
        // Required empty public constructor
    }

    //Necessary fragment creation function
    public static WeatherFragment newInstance(WeatherData weatherData) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_WEATHER, weatherData);
        fragment.setArguments(args);
        return fragment;
    }

    //Necessary fragment creation function
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Data is passed from main activity with parcelable (it becomes a string, then is unraveled)
            weatherData = getArguments().getParcelable(ARG_WEATHER);
        }
    }

    //Necessary fragment creation function
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        //Check if data finished to load, the constructor for WeatherData runs on main thread (the load
        // from api function is called later) so there is no risk for weatherData to be null
        if (weatherData.isDataLoaded()){
            //If the data finished loading the loading screen disappears
            LinearLayout loading = view.findViewById(R.id.loading_weather);
            loading.setVisibility(View.GONE);

            //If the data finished loading the weather ui becomes visible
            LinearLayout weatherMainLayout = view.findViewById(R.id.weatherMainLayout);
            weatherMainLayout.setVisibility(View.VISIBLE);

            //The views and the layouts are set up
            setUpWeatherViews(view);
            //The ui clock update thread starts
            handler.post(updateTimeRunnable);
        }

        return view;
    }

    //Fragment function not in background anymore
    @Override
    public void onResume() {
        super.onResume();
        handler.post(updateTimeRunnable); // Start updating time
    }

    //Fragment function in background
    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(updateTimeRunnable); // Stop updating time
    }

    //Static function to get an image id based on weather enum
    public static int getWeatherImage(Weather weather){
        //Weather images are free use but with citation, this link should be placed somewhere (like in profile)
        //<a href="https://www.vecteezy.com/free-vector/weather">Weather Vectors by Vecteezy</a>
        switch (weather) {
            case SUN:
                return R.drawable.sun;
            case NIGHT:
                return R.drawable.moon;
            case CLOUDS:
                return R.drawable.clouds;
            case PARTIAL_CLOUDS_SUN:
                return R.drawable.clouds_sun;
            case PARTIAL_CLOUDS_MOON:
                return R.drawable.clouds_moon;
            case RAIN:
                return R.drawable.rain;
            case HEAVY_RAIN:
                return R.drawable.storm;
            case SNOW:
                return R.drawable.snow;
            default:
                return R.drawable.sun;
        }
    }

    private void setUpWeatherViews(View view){
        //The list with weather info about every hour of the day (by default it's on current day)
        RecyclerView recyclerViewHours = view.findViewById(R.id.weatherHoursRecyclerView);
        weatherHoursRVA = new WeatherHoursRVA(view.getContext(), weatherData.weatherDays[0].weatherHours);
        recyclerViewHours.setAdapter(weatherHoursRVA);
        recyclerViewHours.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //The clickable list with general weather info about every day (by default selected current day)
        RecyclerView recyclerViewDays = view.findViewById(R.id.weatherDaysRecyclerView);
        weatherDaysRVA = new WeatherDaysRVA(view.getContext(), weatherData.weatherDays, this);
        recyclerViewDays.setAdapter(weatherDaysRVA);
        recyclerViewDays.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        tempNow = view.findViewById(R.id.textTempNow);
        timeNow = view.findViewById(R.id.textTimeNow);
        dayNow = view.findViewById(R.id.textDayNow);
        weatherNow = view.findViewById(R.id.imageWeatherNow);
        updateCurrentTime();
        //Updates the top card with today's info after updateCurrentTime() gets the current time info
        updateCardNow();

        //Moves the list of weather info about every hour to position corresponding to current hour
        //Requires updateCurrentTime() to be called before to set currentHour
        recyclerViewHours.scrollToPosition(currentHour);
    }

    //Finds what time it is and updates the ui to display it
    private void updateCurrentTime() {
        ZoneId veniceTimeZone = ZoneId.of("Europe/Rome"); // Venice time zone
        ZonedDateTime zonedDateTime = ZonedDateTime.now(veniceTimeZone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        //Only updates the general info only if the current hour changed
        if (currentHour != zonedDateTime.getHour()){
            currentHour = zonedDateTime.getHour();
            updateCardNow();
        }

        //The clock is updated every time
        String currentTime = zonedDateTime.format(formatter);
        timeNow.setText(currentTime);
    }

    //Updates the ui top card with today's info
    private void updateCardNow(){
        tempNow.setText(weatherData.weatherDays[0].weatherHours[currentHour].getTempAsString());
        dayNow.setText(weatherData.weatherDays[0].getDayOfWeek());
        weatherNow.setImageResource(getWeatherImage(weatherData.weatherDays[0].weatherHours[currentHour].weather));
    }

    //Implemented from WeatherDaysRVA.OnDayListener interface, to give the RVA (recyclerview adaptors)
    // access to the weatherData every time a click could require the lists to change displayed info
    @Override
    public void onClick(int position) {
        //Changes what day's hourly info is currently being displayed based on the array pos of the item clicked
        weatherHoursRVA.updateData(weatherData.weatherDays[position].weatherHours);
        weatherDaysRVA.updateSelectedPos(position);
    }
}