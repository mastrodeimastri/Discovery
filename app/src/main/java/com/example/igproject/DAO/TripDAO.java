package com.example.igproject.DAO;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.igproject.Models.Route;
import com.example.igproject.Models.Trip;

@Dao
public interface TripDAO {
    @Insert
    void insert(Trip trip);
}
