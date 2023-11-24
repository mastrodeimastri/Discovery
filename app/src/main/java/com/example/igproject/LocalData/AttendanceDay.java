package com.example.igproject.LocalData;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.format.TextStyle;

import java.util.Arrays;
import java.util.Locale;

public class AttendanceDay implements Parcelable {
    public String dayOfWeek;
    public final int[] attendanceValue = new int[4];
    //public static final int[] startTime = {6, 10, 14, 18};
    //public static final int[] endTime = {9, 13, 17, 21};
    public static final int[] time = {8, 12, 16, 20};

    public AttendanceDay(int dayInt, int[] dayRaw){
        DayOfWeek name = DayOfWeek.of(dayInt+1);
        dayOfWeek = name.getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeek = dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1);

        attendanceValue[0] = (dayRaw[1] + dayRaw[2] + dayRaw[3]) / 3;
        attendanceValue[1] = (dayRaw[4] + dayRaw[5] + dayRaw[6] + dayRaw[7]) / 4;
        attendanceValue[2] = (dayRaw[8] + dayRaw[9] + dayRaw[10] + dayRaw[11]) / 4;
        attendanceValue[3] = (dayRaw[12] + dayRaw[13] + dayRaw[14]) / 3;
    }

    //Parcelable constructor to send an object of this class to other fragments or activities
    protected AttendanceDay(Parcel in) {
        dayOfWeek = in.readString();
        in.readIntArray(attendanceValue);
    }

    public static final Creator<AttendanceDay> CREATOR = new Creator<AttendanceDay>() {
        @Override
        public AttendanceDay createFromParcel(Parcel in) {
            return new AttendanceDay(in);
        }

        @Override
        public AttendanceDay[] newArray(int size) {
            return new AttendanceDay[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(dayOfWeek);
        parcel.writeIntArray(attendanceValue);
    }

    public int getAttendanceBarHeight(int i){
        return attendanceValue[i] / 2; //from 0dp to 50dp
    }
}
