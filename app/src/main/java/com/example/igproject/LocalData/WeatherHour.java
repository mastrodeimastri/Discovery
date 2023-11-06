package com.example.igproject.LocalData;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

public class WeatherHour implements Parcelable {
    public LocalTime time;
    public int temp;
    public Weather weather;
    public boolean isDay;
    public double mmRain;
    public double windSpeed;

    public WeatherHour(LocalDateTime time, int temperature, double precipitation, int cloudCover,
                       int isDay, double windSpeed){
        this.time = time.toLocalTime();
        temp = temperature;
        this.windSpeed = windSpeed;
        mmRain = precipitation;
        this.isDay = isDay != 0;

        weather = Weather.SUN;
        if (!this.isDay)
            weather = Weather.NIGHT;
        if (cloudCover > 50)
            weather = Weather.CLOUDS;
        if (precipitation > 0.1){
            weather = Weather.RAIN;
            if (precipitation > 4)
                weather = Weather.HEAVY_RAIN;
            if (temperature < 0)
                weather = Weather.SNOW;
        }
    }

    protected WeatherHour(Parcel in) {
        time = LocalTime.parse(in.readString()); // Convert the String back to LocalDate
        temp = in.readInt();
        weather = Weather.values()[in.readInt()]; // Convert the int back to enum
        isDay = in.readByte() != 0;
        mmRain = in.readDouble();
        windSpeed = in.readDouble();
    }

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

    @NonNull
    @Override
    public String toString(){
        return "Time: " + time + " Temp: " + temp + " Weather: " + weather.name() + (isDay ? "Day" : "Night") + " Rain: " + mmRain + " Wind: " + windSpeed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(time.toString()); // Convert LocalDate to a String
        parcel.writeInt(temp);
        parcel.writeInt(weather.ordinal()); // Convert the enum to an int
        parcel.writeByte((byte) (isDay ? 1 : 0));
        parcel.writeDouble(mmRain);
        parcel.writeDouble(windSpeed);
    }

    public String getTimeAsString() {
        return time.toString();
    }

    public String getTempAsString() {
        return String.valueOf(temp) + "°";
    }

    public String getMmRainAsString() {
        return String.valueOf(mmRain) + "mm";
    }

    public String getWindSpeedAsString() {
        return String.valueOf(windSpeed) + "km/h";
    }


}
