package com.example.igproject.RecyclerViewAdapters;

import static com.example.igproject.Fragments.WeatherFragment.getWeatherImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.igproject.LocalData.WeatherHour;
import com.example.igproject.R;

public class WeatherHoursRVA extends RecyclerView.Adapter<WeatherHoursRVA.MyViewHolder> {
    private final Context context;
    private WeatherHour[] weatherHours;

    public WeatherHoursRVA(Context context, WeatherHour[] weatherHours){
        this.context = context;
        this.weatherHours = weatherHours;
    }

    public void updateData(WeatherHour[] weatherHours){
        this.weatherHours = weatherHours;
    }

    @NonNull
    @Override
    public WeatherHoursRVA.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_weather_hours, parent, false);
        return new WeatherHoursRVA.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHoursRVA.MyViewHolder holder, int position) {
        holder.time.setText(weatherHours[position].getTimeAsString());
        holder.temp.setText(weatherHours[position].getTempAsString());
        holder.rain.setText(weatherHours[position].getMmRainAsString());
        holder.wind.setText(weatherHours[position].getWindSpeedAsString());
        holder.image.setImageResource(getWeatherImage(weatherHours[position].weather));
    }

    @Override
    public int getItemCount() {
        return weatherHours.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView time, temp, rain, wind;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imageWeatherHour);
            time = itemView.findViewById(R.id.textTimeHour);
            temp = itemView.findViewById(R.id.textTempHour);
            rain = itemView.findViewById(R.id.textRainHour);
            wind = itemView.findViewById(R.id.textWindHour);
        }
    }
}
