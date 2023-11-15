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

    @Query("SELECT Id, stopId, tripId, arrivalTime, departureTime, stopSequence " +
            "FROM `Stop Times` " +
            "NATURAL JOIN Stops " +
            "NATURAL JOIN Trips " +
            "WHERE stopGroupName = :groupName AND " +
            "(strftime('%H', departureTime) = strftime('%H', 'now') AND strftime('%M', departureTime) >= strftime('%M', 'now')) OR " +
            "(strftime('%H', departureTime) = strftime('%H', 'now', '+1 hour') AND strftime('%M', departureTime) < strftime('%M', 'now')) AND " +
            "serviceId IN (:services)")
    List<StopTime> getDepartures(String groupName, List<String> services);

/*    @Query("SELECT s.stopName, st.arrivalTime FROM `Stop Times` st " +
            "JOIN Stops s ON st.stopId = s.stopId " +
            "WHERE st.tripId = :tripId AND st.stopSequence >= :stopSequence")
    List<StopTime> getNextStops(Integer tripId, Integer stopSequence);*/

    @Insert
    void insert(StopTime stopTime);
}
