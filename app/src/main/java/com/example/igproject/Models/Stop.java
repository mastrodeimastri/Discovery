package com.example.igproject.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "Stops")
public class Stop {

    @PrimaryKey
    @ColumnInfo(name = "stopId")
    public Integer stopId;

    @ColumnInfo(name = "stopName")
    public String stopName;

    @ColumnInfo(name = "stopLat")
    public Integer stopLat;

    @ColumnInfo(name = "stopLon")
    public Integer stopLon;

}
