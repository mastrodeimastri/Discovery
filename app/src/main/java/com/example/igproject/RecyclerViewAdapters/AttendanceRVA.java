package com.example.igproject.RecyclerViewAdapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.igproject.LocalData.AttendanceDay;
import com.example.igproject.R;

public class AttendanceRVA extends RecyclerView.Adapter<AttendanceRVA.MyViewHolder> {
    Context context;
    AttendanceDay[] attendanceDays;
    //'mOnDayListener' exists only to be passed to the view holder
    private final OnClickRVAListener mOnClickRVAListener;
    //This is the last clicked item in the view
    private int selectedPosition = 0;

    public AttendanceRVA(Context context, AttendanceDay[] attendanceDays, OnClickRVAListener onClickRVAListener){
        this.context = context;
        this.attendanceDays = attendanceDays;
        this.mOnClickRVAListener = onClickRVAListener;
    }

    //Called by the OnClickRVAListener when an item is clicked
    public void updateSelectedPos(int selectedPosition){
        int oldSelectedPosition = this.selectedPosition;
        this.selectedPosition = selectedPosition;
        notifyItemChanged(oldSelectedPosition);
        notifyItemChanged(selectedPosition);
    }

    @NonNull
    @Override
    public AttendanceRVA.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_attendance, parent, false);
        return new AttendanceRVA.MyViewHolder(view, mOnClickRVAListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceRVA.MyViewHolder holder, int position) {
        if (position == selectedPosition){
            holder.time1.setVisibility(View.VISIBLE);
            holder.time2.setVisibility(View.VISIBLE);
            holder.time3.setVisibility(View.VISIBLE);
            holder.time4.setVisibility(View.VISIBLE);
        }
        else {
            holder.time1.setVisibility(View.GONE);
            holder.time2.setVisibility(View.GONE);
            holder.time3.setVisibility(View.GONE);
            holder.time4.setVisibility(View.GONE);
        }

        setBarHeightAndColor(holder, position, holder.bar1, 1);
        setBarHeightAndColor(holder, position, holder.bar2, 2);
        setBarHeightAndColor(holder, position, holder.bar3, 3);
        setBarHeightAndColor(holder, position, holder.bar4, 4);

        holder.textDay.setText(attendanceDays[position].dayOfWeek);
    }

    private void setBarHeightAndColor(@NonNull AttendanceRVA.MyViewHolder holder, int position, View bar, int iBar){
        //Set height
        ViewGroup.LayoutParams layoutParams = bar.getLayoutParams();
        int newHeight = attendanceDays[position].getAttendanceBarHeight(iBar-1);
        layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newHeight, holder.itemView.getResources().getDisplayMetrics());
        bar.setLayoutParams(layoutParams);

        //Set color
        if (newHeight <= 10)
            bar.setBackgroundColor(context.getColor(R.color.green));
        else if (newHeight <= 25)
            bar.setBackgroundColor(context.getColor(R.color.yellow));
        else if (newHeight <= 40)
            bar.setBackgroundColor(context.getColor(R.color.orange));
        else
            bar.setBackgroundColor(context.getColor(R.color.red));
    }

    @Override
    public int getItemCount() {
        return attendanceDays.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View bar1, bar2, bar3, bar4;
        TextView time1, time2, time3, time4, textDay;
        //Listener that is notified when an item is clicked
        OnClickRVAListener onClickRVAListener;

        public MyViewHolder(@NonNull View itemView, OnClickRVAListener onClickRVAListener) {
            super(itemView);

            bar1 = itemView.findViewById(R.id.bar1);
            bar2 = itemView.findViewById(R.id.bar2);
            bar3 = itemView.findViewById(R.id.bar3);
            bar4 = itemView.findViewById(R.id.bar4);
            time1 = itemView.findViewById(R.id.time1);
            time2 = itemView.findViewById(R.id.time2);
            time3 = itemView.findViewById(R.id.time3);
            time4 = itemView.findViewById(R.id.time4);
            textDay = itemView.findViewById(R.id.dayAttendance);

            this.onClickRVAListener = onClickRVAListener;

            //'this' here is the AttendanceRVA object, not the holder
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onClickRVAListener.onClick(getAdapterPosition());
        }
    }
}
