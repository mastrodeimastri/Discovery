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

    @Query("SELECT serviceId " +
            "FROM Calendar " +
            "WHERE Sunday = 1")
    String getSundayService();

    @Query("SELECT serviceId " +
            "FROM Calendar " +
            "WHERE Monday = 1")
    String getMondayService();

    @Query("SELECT serviceId " +
            "FROM Calendar " +
            "WHERE Tuesday = 1")
    String getTuesdayService();

    @Query("SELECT serviceId " +
            "FROM Calendar " +
            "WHERE Wednesday = 1")
    String getWednesdayService();

    @Query("SELECT serviceId " +
            "FROM Calendar " +
            "WHERE Thursday = 1")
    String getThursdayService();

    @Query("SELECT serviceId " +
            "FROM Calendar " +
            "WHERE Friday = 1")
    String getFridayService();

    @Query("SELECT serviceId " +
            "FROM Calendar " +
            "WHERE Saturday = 1")
    String getSaturdayService();


    @Insert
    void insert(Calendar calendarEntry);
}
