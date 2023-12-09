package com.example.igproject.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.igproject.Models.CalendarDates;

import java.util.List;

@Dao
public interface CalendarDatesDAO {
    @Insert
    void insertAll(List<CalendarDates> calendarDates);

    @Query("SELECT date " +
            "FROM CalendarDates")
    List<Integer> getDates();

    @Query("SELECT serviceId " +
            "FROM CalendarDates " +
            "WHERE date = :date AND ExceptionType = 1")
    String getServices(Integer date);
}
