package com.example.igproject.Data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.igproject.DAO.RouteDAO;
import com.example.igproject.DAO.StopDAO;
import com.example.igproject.DAO.StopTimeDAO;
import com.example.igproject.DAO.TripDAO;
import com.example.igproject.Models.*;

/*
* Questa classe serve per andare a definire il database locale e metodi con i quali interagirci
* */
@TypeConverters(com.example.igproject.Models.TypeConverters.class)
@Database(entities = {Route.class, Stop.class, StopTime.class, Trip.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RouteDAO routeDAO();

    public abstract StopDAO stopDAO();

    public abstract StopTimeDAO stopTimeDAO();

    public abstract TripDAO tripDAO();
}
