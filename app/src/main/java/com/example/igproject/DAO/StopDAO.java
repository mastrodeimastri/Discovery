package com.example.igproject.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.igproject.Models.Route;
import com.example.igproject.Models.Stop;

import java.util.List;

@Dao
public interface StopDAO {

    @Query("SELECT * FROM Stops")
    List<Stop> getAll();

    @Insert
    void insert(Stop stop);
}
