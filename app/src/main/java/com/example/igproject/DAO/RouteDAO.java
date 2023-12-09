package com.example.igproject.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.example.igproject.Models.Route;

import java.util.List;

@Dao
public interface RouteDAO {

    @Insert
    void insertAll(List<Route> routes);
}
