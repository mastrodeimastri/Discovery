package com.example.igproject.LocalData;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.Objects;

public class WeatherHour implements Parcelable {
    public LocalTime time;
    public int temp;
    public Weather weather;
    public boolean isDay;
    public double mmRain;
    public double windSpeed;

    public WeatherHour(LocalDateTime time, int temperature, double precipitation, int cloudCover,
                       int isDay, double windSpeed){
        //Getting relevant data about the hour
        this.time = time.toLocalTime();
        temp = temperature;
        this.windSpeed = windSpeed;
        mmRain = precipitation;
        this.isDay = isDay != 0;

        //Algorithm to decide this hour's weather
        weather = Weather.SUN;
        if (!this.isDay)
            weather = Weather.NIGHT;
        if (cloudCover > 40){
            if (this.isDay)
                weather = Weather.PARTIAL_CLOUDS_SUN;
            else
                weather = Weather.PARTIAL_CLOUDS_MOON;
        }
        if (cloudCover > 80)
            weather = Weather.CLOUDS;
        if (precipitation > 0.1){
            weather = Weather.RAIN;
            if (precipitation > 4)
                weather = Weather.HEAVY_RAIN;
            if (temperature < 0)
                weather = Weather.SNOW;
        }
    }

    //Parcelable constructor to send an object of this class to other fragments or activities
    protected WeatherHour(Parcel in) {
        time = LocalTime.parse(Objects.requireNonNull(in.readString())); // Convert the String back to LocalDate
        temp = in.readInt();
        weather = Weather.values()[in.readInt()]; // Convert the int back to enum
        isDay = in.readByte() != 0;
        mmRain = in.readDouble();
        windSpeed = in.readDouble();
    }

    //Parcelable function to send an object of this class to other fragments or activities
    public static final Creator<WeatherHour> CREATOR = new Creator<WeatherHour>() {
        @Override
        public WeatherHour createFromParcel(Parcel in) {
            return new WeatherHour(in);
        }

        @Override
        public WeatherHour[] newArray(int size) {
            return new WeatherHour[size];
        }
    };

    //Parcelable function to send an object of this class to other fragments or activities
    @Override
    public int describeContents() {
        return 0;
    }

    //Parcelable function to send an object of this class to other fragments or activities
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(time.toString()); // Convert LocalDate to a String
        parcel.writeInt(temp);
        parcel.writeInt(weather.ordinal()); // Convert the enum to an int
        parcel.writeByte((byte) (isDay ? 1 : 0));
        parcel.writeDouble(mmRain);
        parcel.writeDouble(windSpeed);
    }

    //Function for debug, unused
    @NonNull
    @Override
    public String toString(){
        return "Time: " + time + " Temp: " + temp + " Weather: " + weather.name() + (isDay ? "Day" : "Night") + " Rain: " + mmRain + " Wind: " + windSpeed;
    }

    //Gets this hour time in the format HH:mm, mm is always '00'
    public String getTimeAsString() {
        return time.toString();
    }

    public String getTempAsString() {
        return temp + "°";
    }

    public String getMmRainAsString() {
        return mmRain + "mm";
    }

    public String getWindSpeedAsString() {
        return windSpeed + "km/h";
    }


}
