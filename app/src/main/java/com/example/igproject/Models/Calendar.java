package com.example.igproject.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.igproject.DAO.CalendarDAO;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "Calendar")
public class Calendar {
    @PrimaryKey
    @NotNull
    public String ServiceId;

    @NotNull
    public Integer Monday;

    @NotNull
    public Integer Tuesday;

    @NotNull
    public Integer Wednesday;

    @NotNull
    public Integer Thursday;

    @NotNull
    public Integer Friday;

    @NotNull
    public Integer Saturday;

    @NotNull
    public Integer Sunday;

    @NotNull
    public Integer StartDate;

    @NotNull
    public Integer EndDate;

    public Calendar(){}

    public Calendar(String sId,
             String monday,
             String tuesday,
             String wednesday,
             String thursday,
             String friday,
             String saturday,
             String sunday,
             String startDate,
             String endDate){
        this.ServiceId = sId;
        this.Monday = Integer.parseInt(monday);
        this.Tuesday = Integer.parseInt(tuesday);
        this.Wednesday = Integer.parseInt(wednesday);
        this.Thursday = Integer.parseInt(thursday);
        this.Friday = Integer.parseInt(friday);
        this.Saturday = Integer.parseInt(saturday);
        this.Sunday = Integer.parseInt(sunday);
        this.StartDate = Integer.parseInt(startDate);
        this.EndDate = Integer.parseInt(endDate);
    }
}
