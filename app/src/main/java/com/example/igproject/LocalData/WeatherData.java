package com.example.igproject.LocalData;

import static java.lang.Math.round;

import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

//Parcelable to communicate with fragment
public class WeatherData implements Parcelable {
    private MainActivityListener mainActivityListener;
    public int updated;
    public WeatherDay[] weatherDays;

    public WeatherData(){
        updated = 0;
    }

    public void loadData(){
        //Get stuff form API
        Thread thread = new Thread(new LoadData());
        thread.start();
    }

    //Async thread to load the data
    private class LoadData implements Runnable {
        //0 good - 1 error connection - 2 error parsing

        @Override
        public void run() {
            parseWeatherData();

            //UI update needs to be handled be the main thread
            updated = 1;
            Handler threadHandler = new Handler(Looper.getMainLooper());
            threadHandler.post(WeatherData.this::notifyDataUpdated);
        }

        private void parseWeatherData(){
            try {
                //https://open-meteo.com/en/docs#minutely_15=&hourly=temperature_2m,precipitation_probability,precipitation,cloudcover,windspeed_10m,uv_index&timezone=Europe%2FBerlin&past_days=1
                String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=45.438759&longitude=12.327145&hourly=temperature_2m,precipitation,cloudcover,windspeed_10m,is_day&timezone=Europe%2FBerlin";

                URL url = new URL(apiUrl);

                StringBuilder json = new StringBuilder();
                InputStream input = url.openStream();
                InputStreamReader isr = new InputStreamReader(input);
                BufferedReader reader = new BufferedReader(isr);
                int c;
                while ((c = reader.read()) != -1) {
                    json.append((char) c);
                }

                parse(json.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void parse(String json){
            try {
                JSONObject weatherData = new JSONObject(json);

                // Extract hourly data
                JSONObject hourlyData = weatherData.getJSONObject("hourly");
                JSONArray timeArray = hourlyData.getJSONArray("time");
                JSONArray temperatureArray = hourlyData.getJSONArray("temperature_2m");
                JSONArray precipitationArray = hourlyData.getJSONArray("precipitation");
                JSONArray cloudCoverArray = hourlyData.getJSONArray("cloudcover");
                JSONArray isDayArray = hourlyData.getJSONArray("is_day");
                JSONArray windSpeedArray = hourlyData.getJSONArray("windspeed_10m");

                // Convert arrays to strings
                LocalDateTime[] time = new LocalDateTime[timeArray.length()];
                int[] temperature = new int[temperatureArray.length()];
                double[] precipitation = new double[precipitationArray.length()];
                int[] cloudCover = new int[cloudCoverArray.length()];
                int[] isDay = new int[isDayArray.length()];
                double[] windSpeed = new double[windSpeedArray.length()];

                for (int i = 0; i < timeArray.length(); i++) {
                    time[i] = formatDateTime(timeArray.getString(i));
                    temperature[i] = (int) round(temperatureArray.getDouble(i));
                    precipitation[i] = precipitationArray.getDouble(i);
                    cloudCover[i] = cloudCoverArray.getInt(i);
                    isDay[i] = isDayArray.getInt(i);
                    windSpeed[i] = windSpeedArray.getDouble(i);
                }

                int numOfDays = time.length / 24;
                weatherDays = new WeatherDay[numOfDays];
                for (int day = 0; day < numOfDays; ++day){
                    weatherDays[day] = new WeatherDay(
                            Arrays.copyOfRange(time, day * 24, 24 + (day * 24)),
                            Arrays.copyOfRange(temperature, day * 24, 24 + (day * 24)),
                            Arrays.copyOfRange(precipitation, day * 24, 24 + (day * 24)),
                            Arrays.copyOfRange(cloudCover, day * 24, 24 + (day * 24)),
                            Arrays.copyOfRange(isDay, day * 24, 24 + (day * 24)),
                            Arrays.copyOfRange(windSpeed, day * 24, 24 + (day * 24))
                    );
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private LocalDateTime formatDateTime(String dateString){
            // Formato della data
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

            // Parsa la data in un oggetto LocalDateTime
            return LocalDateTime.parse(dateString, formatter);
        }
    }

    protected WeatherData(Parcel in) {
        updated = in.readInt();
        weatherDays = in.createTypedArray(WeatherDay.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(updated);
        dest.writeTypedArray(weatherDays, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WeatherData> CREATOR = new Creator<WeatherData>() {
        @Override
        public WeatherData createFromParcel(Parcel in) {
            return new WeatherData(in);
        }

        @Override
        public WeatherData[] newArray(int size) {
            return new WeatherData[size];
        }
    };

    //Update weather ui
    private void notifyDataUpdated() {
        if (mainActivityListener != null) {
            mainActivityListener.onDataUpdates("weather");
        }
    }

    public void setMainActivityListener(MainActivityListener listener){
        this.mainActivityListener = listener;
    }
}
