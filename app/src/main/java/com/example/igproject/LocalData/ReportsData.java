package com.example.igproject.LocalData;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.igproject.Models.Report;

import java.util.List;

public class ReportsData implements Parcelable {

    public List<Report> reports;

    public ReportsData(List<Report> reports) {
        this.reports = reports;
    }

    protected ReportsData(Parcel in) {
    }

    public static final Creator<ReportsData> CREATOR = new Creator<ReportsData>() {
        @Override
        public ReportsData createFromParcel(Parcel in) {
            return new ReportsData(in);
        }

        @Override
        public ReportsData[] newArray(int size) {
            return new ReportsData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
    }
}
