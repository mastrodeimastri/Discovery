package com.example.igproject.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.igproject.DAO.CalendarDAO;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "CalendarDates")
public class CalendarDates {

    @PrimaryKey(autoGenerate = true)
    public Integer Id;

    @NotNull
    public String ServiceId;

    @NotNull
    public Integer Date;

    @NotNull
    public Integer ExceptionType;

    public CalendarDates(){

    }

    public CalendarDates(String serviceId, String date, String exType){
        this.ServiceId = serviceId;
        this.Date = Integer.parseInt(date);
        this.ExceptionType = Integer.parseInt(exType);
    }
}
