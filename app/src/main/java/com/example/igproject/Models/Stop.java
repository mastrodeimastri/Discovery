package com.example.igproject.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "Stops")
public class Stop {

    @PrimaryKey
    @ColumnInfo(name = "stopId")
    @NotNull
    public Integer stopId;

    @ColumnInfo(name = "stopName")
    @NotNull
    public String stopName;

    @ColumnInfo(name = "stopLat")
    @NotNull
    public String stopLat;

    @ColumnInfo(name = "stopLon")
    @NotNull
    public String stopLon;

    public Stop(){}

    public Stop(String sId, String sN, String sLat, String sLon){
        this.stopId = Integer.parseInt(sId);
        this.stopName = sN;
        this.stopLat = sLat;
        this.stopLon = sLon;
    }

}
