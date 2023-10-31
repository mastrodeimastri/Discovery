package com.example.igproject.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.igproject.Models.Route;
import com.example.igproject.Models.Stop;
import com.example.igproject.Models.StopTime;

import java.util.List;

@Dao
public interface StopTimeDAO {

    @Query("SELECT * FROM `Stop Times` WHERE stopId = :stopId")
    List<StopTime> getDepartures(Integer stopId);

/*    @Query("SELECT s.stopName, st.arrivalTime FROM `Stop Times` st " +
            "JOIN Stops s ON st.stopId = s.stopId " +
            "WHERE st.tripId = :tripId AND st.stopSequence >= :stopSequence")
    List<StopTime> getNextStops(Integer tripId, Integer stopSequence);*/

    @Insert
    void insert(StopTime stopTime);
}
