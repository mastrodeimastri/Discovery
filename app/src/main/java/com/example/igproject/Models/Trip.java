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
    public Integer tripId;

    @ColumnInfo(name = "routeId")
    public Integer routeId;
}
