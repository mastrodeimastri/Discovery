package com.example.igproject.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.igproject.Models.Calendar;

import java.util.Date;
import java.util.List;

@Dao
public interface CalendarDAO {

    @Query("SELECT DISTINCT EndDate " +
            "FROM 'Calendar'")
    List<Integer> getEndDate();

/*    @Query("")
    List<String> getServices(Date date);*/

    @Query("SELECT * " +
            "FROM Calendar")
    List<Calendar> getAll();

    @Insert
    void insert(Calendar calendarEntry);
}
