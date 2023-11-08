package com.example.igproject.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.SortedMap;

@Entity(tableName = "StopGroups")
public class StopGroup {

    @PrimaryKey(autoGenerate = true)
    @NotNull
    public Integer stopGroupId;

    @NotNull
    public String stopGroupName;

    @ColumnInfo(name = "stopLat")
    @NotNull
    public String stopLat;

    @ColumnInfo(name = "stopLon")
    @NotNull
    public String stopLon;


    public StopGroup() {

    }

    public StopGroup(String name, String lat, String lon) {
        this.stopGroupName = name;
        this.stopLat = lat;
        this.stopLon = lon;
    }
}
