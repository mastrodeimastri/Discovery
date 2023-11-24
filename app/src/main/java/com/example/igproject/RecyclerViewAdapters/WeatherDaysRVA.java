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

//Adapter for recyclerview to display clickable list with general weather info about every day
public class WeatherDaysRVA extends RecyclerView.Adapter<WeatherDaysRVA.MyViewHolder> {
    private final Context context;
    private final WeatherDay[] weatherDays;
    //'mOnDayListener' exists only to be passed to the view holder
    private final OnClickRVAListener mOnClickRVAListener;
    //This is the last clicked item in the view
    private int selectedPosition = 0;

    public WeatherDaysRVA(Context context, WeatherDay[] weatherDays, OnClickRVAListener onClickRVAListener){
        this.context = context;
        this.weatherDays = weatherDays;
        this.mOnClickRVAListener = onClickRVAListener;
    }

    //Called by the OnClickRVAListener when an item is clicked
    public void updateSelectedPos(int selectedPosition){
        //Both the last clicked item and the clicked item are reloaded to change their background
        // color accordingly and show which one is selected
        int oldSelectedPosition = this.selectedPosition;
        this.selectedPosition = selectedPosition;
        notifyItemChanged(oldSelectedPosition);
        notifyItemChanged(selectedPosition);
    }

    //Sets the view holder
    @NonNull
    @Override
    public WeatherDaysRVA.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_weather_days, parent, false);
        return new WeatherDaysRVA.MyViewHolder(view, mOnClickRVAListener);
    }

    //Loads the item at the given position by modifying its views
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

    //Returns how many items are in the recyclerview
    @Override
    public int getItemCount() {
        return weatherDays.length;
    }

    //Holder that stores all the views of every item in the layout,
    // has a click listener to get which item was clicked
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView day, tempMin, tempMax, date;
        CardView cardView;
        //Listener that is notified when an item is clicked
        OnClickRVAListener onClickRVAListener;

        public MyViewHolder(@NonNull View itemView, OnClickRVAListener onClickRVAListener) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardViewDay);
            image = itemView.findViewById(R.id.imageWeatherDay);
            day = itemView.findViewById(R.id.textDay);
            tempMin = itemView.findViewById(R.id.textTempMinDay);
            tempMax = itemView.findViewById(R.id.textTempMaxDay);
            date = itemView.findViewById(R.id.textDateDay);
            this.onClickRVAListener = onClickRVAListener;

            //'this' here is the WeatherDaysRVA object, not the holder
            itemView.setOnClickListener(this);
        }

        //When an item gets clicked notify the listener to act accordingly
        @Override
        public void onClick(View view) {
            onClickRVAListener.onClick(getAdapterPosition());
        }
    }
}
