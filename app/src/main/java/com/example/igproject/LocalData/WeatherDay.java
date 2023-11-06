package com.example.igproject.LocalData;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.TextStyle;
import java.util.Locale;

public class WeatherDay implements Parcelable {
    public int minTemp, maxTemp;
    public LocalDate date;
    public String dayOfWeek;
    public Weather weather;

    public WeatherHour[] weatherHours;

    public WeatherDay(LocalDateTime[] time, int[] temperature, double[] precipitation, int[] cloudCover,
                      int[] isDay, double[] windSpeed){
        date = time[0].toLocalDate();
        dayOfWeek = time[0].getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeek = dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1);

        minTemp = temperature[0];
        maxTemp = temperature[0];
        for (int i = 0; i < time.length; ++i){
            if (minTemp > temperature[i])
                minTemp = temperature[i];
            if (maxTemp < temperature[i])
                maxTemp = temperature[i];
        }

        double totPrecipitation = 0, totCloudCover = 0;
        boolean snow = false;
        for (int i = 0; i < time.length; ++i){
            totPrecipitation += precipitation[i];
            totCloudCover += cloudCover[i];

            if (precipitation[i] > 0.2 && temperature[i] < 0)
                snow = true;
        }
        totCloudCover = totCloudCover / time.length;
        weather = Weather.SUN;
        if (totCloudCover > 50)
            weather = Weather.CLOUDS;
        if (totPrecipitation > 1){
            weather = Weather.RAIN;
            if (totPrecipitation > 10)
                weather = Weather.HEAVY_RAIN;
            if (snow)
                weather = Weather.SNOW;
        }

        weatherHours = new WeatherHour[time.length];
        for (int i = 0; i < weatherHours.length; ++i){
            weatherHours[i] = new WeatherHour(
                    time[i], temperature[i], precipitation[i], cloudCover[i], isDay[i], windSpeed[i]
            );
        }
    }

    protected WeatherDay(Parcel in) {
        minTemp = in.readInt();
        maxTemp = in.readInt();
        date = LocalDate.parse(in.readString()); // Convert the String back to LocalDate
        dayOfWeek = in.readString();
        weather = Weather.values()[in.readInt()]; // Convert the int back to enum
        weatherHours = in.createTypedArray(WeatherHour.CREATOR);
    }

    public static final Creator<WeatherDay> CREATOR = new Creator<WeatherDay>() {
        @Override
        public WeatherDay createFromParcel(Parcel in) {
            return new WeatherDay(in);
        }

        @Override
        public WeatherDay[] newArray(int size) {
            return new WeatherDay[size];
        }
    };

    @NonNull
    @Override
    public String toString(){
        return "Min: " + minTemp + " Max: " + maxTemp + " Date: " + date.toString() + " Day: " + dayOfWeek + "Weather: " + weather.name();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeInt(minTemp);
        parcel.writeInt(maxTemp);
        parcel.writeString(date.toString()); // Convert LocalDate to a String
        parcel.writeString(dayOfWeek);
        parcel.writeInt(weather.ordinal()); // Convert the enum to an int
        parcel.writeTypedArray(weatherHours, flags);
    }

    public String getMinTemp() {
        return String.valueOf(minTemp) + "°";
    }

    public String getMaxTemp() {
        return String.valueOf(maxTemp) + "°";
    }

    public String getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        return date.format(formatter);
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }
}