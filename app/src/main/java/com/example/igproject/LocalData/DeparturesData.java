package com.example.igproject.LocalData;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.igproject.Models.Departure;
import com.example.igproject.Models.TripStop;

import java.util.List;

public class DeparturesData implements Parcelable {

    public String stopName;

    public List<Departure> departures;

    public List<List<TripStop>> tripStops;

    public DeparturesData(List<Departure> departures, List<List<TripStop>> tripStops, String stopName){
        this.departures = departures;
        this.tripStops = tripStops;
        this.stopName  =stopName;
    }

    protected DeparturesData(Parcel in) {
        stopName = in.readString();
    }

    public static final Creator<DeparturesData> CREATOR = new Creator<DeparturesData>() {
        @Override
        public DeparturesData createFromParcel(Parcel in) {
            return new DeparturesData(in);
        }

        @Override
        public DeparturesData[] newArray(int size) {
            return new DeparturesData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(stopName);
    }
}
