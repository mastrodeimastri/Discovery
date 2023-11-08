package com.example.igproject.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.igproject.Utils.DateConverter;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "Stop Times", foreignKeys = {
        @ForeignKey(entity = Stop.class,
        deferred = true,
        parentColumns = "stopId",
        childColumns = "stopId"),
        @ForeignKey(entity = Trip.class,
        deferred = true,
        parentColumns = "tripId",
        childColumns = "tripId")
})
@TypeConverters(DateConverter.class)
public class StopTime {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    @NotNull
    public Integer Id;

    @ColumnInfo(name = "stopId")
    @NotNull
    public Integer stopId;

    @ColumnInfo(name = "tripId")
    @NotNull
    public Integer tripId;

    @ColumnInfo(name = "arrivalTime")
    @NotNull
    public String arrivalTime;

    @ColumnInfo(name = "departureTime")
    @NotNull
    public String departureTime;

    @ColumnInfo(name = "stopSequence")
    @NotNull
    public Integer stopSequence;

    public StopTime() {

    }

    public StopTime(String tId, String aT, String dT, String sId, String sQ){
        this.stopId = Integer.parseInt(sId);
        this.tripId = Integer.parseInt(tId);
        this.arrivalTime = aT;
        this.departureTime = dT;
        this.stopSequence = Integer.parseInt(sQ);
    }

}
