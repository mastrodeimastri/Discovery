package com.example.igproject.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.igproject.Utils.DateConverter;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;

import kotlin.jvm.internal.Ref;

@Entity(tableName = "Stop Times", foreignKeys = {
        @ForeignKey(entity = Stop.class,
        parentColumns = "stopId",
        childColumns = "stopId"),
        @ForeignKey(entity = Trip.class,
        parentColumns = "tripId",
        childColumns = "tripId")
})
@TypeConverters(DateConverter.class)
public class StopTime {

    @PrimaryKey
    @ColumnInfo(name = "Id")
    public Integer Id;

    @ColumnInfo(name = "stopId")
    public Integer stopId;

    @ColumnInfo(name = "tripId")
    public Integer tripId;

    @ColumnInfo(name = "arrivalTime")
    public Date arrivalTime;

    @ColumnInfo(name = "departureTime")
    public Date departureTile;

    @ColumnInfo(name = "stopSequence")
    public Integer stopSequence;

}
