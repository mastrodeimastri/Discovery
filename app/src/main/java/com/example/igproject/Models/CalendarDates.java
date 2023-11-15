package com.example.igproject.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.igproject.DAO.CalendarDAO;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "CalendarDates")
public class CalendarDates {
    @PrimaryKey
    @NotNull
    public String ServiceId;

    @NotNull
    public Integer Date;

    public CalendarDates(){

    }

    public CalendarDates(String serviceId, String date){
        this.ServiceId = serviceId;
        this.Date = Integer.parseInt(date);
    }
}
