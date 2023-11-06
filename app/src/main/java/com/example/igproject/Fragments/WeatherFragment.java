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
import com.example.igproject.RecyclerViewAdapters.WeatherDaysRVA;
import com.example.igproject.RecyclerViewAdapters.WeatherHoursRVA;

import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

public class WeatherFragment extends Fragment implements WeatherDaysRVA.OnDayListener {

    private static final String ARG_WEATHER = "weatherData";
    private WeatherData weatherData;
    private RecyclerView recyclerViewHours, recyclerViewDays;
    private WeatherHoursRVA weatherHoursRVA;
    private WeatherDaysRVA weatherDaysRVA;
    private LinearLayout loading;
    private TextView tempNow, timeNow, dayNow;
    private ImageView weatherNow;
    private int currentHour;

    private final Handler handler = new Handler();
    private final Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            updateCurrentTime();
            handler.postDelayed(this, 1000); // Update every 1 second
        }
    };

    public WeatherFragment() {
        // Required empty public constructor
    }

    public static WeatherFragment newInstance(WeatherData weatherData) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_WEATHER, weatherData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weatherData = getArguments().getParcelable(ARG_WEATHER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        if (weatherData.updated == 1){
            loading = view.findViewById(R.id.loading_weather);
            loading.setVisibility(View.GONE);

            LinearLayout weatherMainLayout = view.findViewById(R.id.weatherMainLayout);
            weatherMainLayout.setVisibility(View.VISIBLE);

            setUpWeatherViews(view);
            handler.post(updateTimeRunnable);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(updateTimeRunnable); // Start updating time
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(updateTimeRunnable); // Stop updating time
    }

    public static int getWeatherImage(Weather weather){
        //<a href="https://www.vecteezy.com/free-vector/weather">Weather Vectors by Vecteezy</a>
        switch (weather) {
            case SUN:
                return R.drawable.sun;
            case NIGHT:
                return R.drawable.moon;
            case CLOUDS:
                return R.drawable.clouds;
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
        recyclerViewHours = view.findViewById(R.id.weatherHoursRecyclerView);
        weatherHoursRVA = new WeatherHoursRVA(view.getContext(), weatherData.weatherDays[0].weatherHours);
        recyclerViewHours.setAdapter(weatherHoursRVA);
        recyclerViewHours.setLayoutManager(new LinearLayoutManager(view.getContext()));

        recyclerViewDays = view.findViewById(R.id.weatherDaysRecyclerView);
        weatherDaysRVA = new WeatherDaysRVA(view.getContext(), weatherData.weatherDays, this);
        recyclerViewDays.setAdapter(weatherDaysRVA);
        recyclerViewDays.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        tempNow = view.findViewById(R.id.textTempNow);
        timeNow = view.findViewById(R.id.textTimeNow);
        dayNow = view.findViewById(R.id.textDayNow);
        weatherNow = view.findViewById(R.id.imageWeatherNow);
        updateCurrentTime();
        updateCardNow();

        recyclerViewHours.scrollToPosition(currentHour);
    }

    private void updateCurrentTime() {
        ZoneId veniceTimeZone = ZoneId.of("Europe/Rome"); // Venice time zone
        ZonedDateTime zonedDateTime = ZonedDateTime.now(veniceTimeZone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        if (currentHour != zonedDateTime.getHour()){
            currentHour = zonedDateTime.getHour();
            updateCardNow();
        }

        String currentTime = zonedDateTime.format(formatter);
        timeNow.setText(currentTime);
    }

    private void updateCardNow(){
        tempNow.setText(weatherData.weatherDays[0].weatherHours[currentHour].getTempAsString());
        dayNow.setText(weatherData.weatherDays[0].getDayOfWeek());
        weatherNow.setImageResource(getWeatherImage(weatherData.weatherDays[0].weatherHours[currentHour].weather));
    }

    @Override
    public void onDayClick(int position) {
        weatherHoursRVA.updateData(weatherData.weatherDays[position].weatherHours);
        recyclerViewHours.setAdapter(weatherHoursRVA);
        //Set layout manager?
        weatherDaysRVA.updateSelectedPos(position);
    }
}