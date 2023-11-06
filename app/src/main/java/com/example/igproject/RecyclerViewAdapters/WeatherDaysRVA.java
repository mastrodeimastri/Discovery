package com.example.igproject.RecyclerViewAdapters;

import static com.example.igproject.Fragments.WeatherFragment.getWeatherImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.igproject.LocalData.WeatherDay;
import com.example.igproject.R;

public class WeatherDaysRVA extends RecyclerView.Adapter<WeatherDaysRVA.MyViewHolder> {
    private final Context context;
    private WeatherDay[] weatherDays;
    private OnDayListener mOnDayListener;
    protected int selectedPosition = 0;

    public WeatherDaysRVA(Context context, WeatherDay[] weatherDays, OnDayListener onDayListener){
        this.context = context;
        this.weatherDays = weatherDays;
        this.mOnDayListener = onDayListener;
    }

    public void updateSelectedPos(int selectedPosition){
        notifyItemChanged(this.selectedPosition);
        this.selectedPosition = selectedPosition;
        notifyItemChanged(selectedPosition);
    }

    @NonNull
    @Override
    public WeatherDaysRVA.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_weather_days, parent, false);
        return new WeatherDaysRVA.MyViewHolder(view, mOnDayListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherDaysRVA.MyViewHolder holder, int position) {
        if (position == selectedPosition)
            holder.cardView.setCardBackgroundColor(context.getColor(R.color.medium_gray));
        else
            holder.cardView.setCardBackgroundColor(context.getColor(R.color.light_gray));

        holder.day.setText(weatherDays[position].getDayOfWeek());
        holder.tempMin.setText(weatherDays[position].getMinTemp());
        holder.tempMax.setText(weatherDays[position].getMaxTemp());
        holder.date.setText(weatherDays[position].getDate());
        holder.image.setImageResource(getWeatherImage(weatherDays[position].weather));
    }

    @Override
    public int getItemCount() {
        return weatherDays.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView day, tempMin, tempMax, date;
        CardView cardView;
        OnDayListener onDayListener;

        public MyViewHolder(@NonNull View itemView, OnDayListener onDayListener) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardViewDay);
            image = itemView.findViewById(R.id.imageWeatherDay);
            day = itemView.findViewById(R.id.textDay);
            tempMin = itemView.findViewById(R.id.textTempMinDay);
            tempMax = itemView.findViewById(R.id.textTempMaxDay);
            date = itemView.findViewById(R.id.textDateDay);
            this.onDayListener = onDayListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onDayListener.onDayClick(getAdapterPosition());
        }
    }

    public interface OnDayListener{
        void onDayClick(int position);
    }
}
