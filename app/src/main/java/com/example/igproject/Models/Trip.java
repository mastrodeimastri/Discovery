package com.example.igproject.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "Trips", foreignKeys = {
        @ForeignKey(entity = Route.class,
        parentColumns = "routeId",
        childColumns = "routeId"
        )
})
public class Trip {

    @PrimaryKey
    @ColumnInfo(name = "tripId")
    @NotNull
    public Integer tripId;

    @ColumnInfo(name = "routeId")
    @NotNull
    public Integer routeId;

    @NotNull
    public String serviceId;

    public Trip(){}

    public Trip(String rId, String tId, String sId) {
        this.routeId = Integer.parseInt(rId);
        this.tripId = Integer.parseInt(tId);
        this.serviceId = sId;
    }
}
