package com.example.igproject.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.igproject.Models.Departure;
import com.example.igproject.Models.Route;
import com.example.igproject.Models.Stop;
import com.example.igproject.Models.StopTime;
import com.example.igproject.Models.TripStop;

import java.util.List;

@Dao
public interface StopTimeDAO {

    @Query("SELECT t.tripId, st1.stopSequence, st1.departureTime, r.routeName, r.routeColor, r.routeTextColor, s2.stopName AS destination " +
            "FROM `Stop Times` st1 " +
            "JOIN Stops s1 ON s1.stopId = st1.stopId " +
            "JOIN Trips t ON t.tripId = st1.tripId " +
            "JOIN `Stop Times`st2 ON st2.tripId = t.tripId " +
            "JOIN stops s2 ON s2.stopId = st2.stopId " +
            "JOIN Routes r ON r.routeId = t.routeId " +
            "WHERE s1.stopGroupName = :groupName AND " +
            "((strftime('%H', st1.departureTime) = strftime('%H', 'now', 'localtime') AND strftime('%M', st1.departureTime) >= strftime('%M', 'now', '+1 hour' , 'localtime')) OR " +
            "(strftime('%H', st1.departureTime) = strftime('%H', 'now', '+1 hour', 'localtime') AND strftime('%M', st1.departureTime) < strftime('%M', 'now'))) AND " +
            "t.serviceId = :service AND st2.stopSequence = " +
            "(SELECT MAX(stopSequence) " +
            "FROM `Stop Times` st3 " +
            "WHERE st3.tripId = t.tripId) AND st2.stopSequence != st1.stopSequence " +
            "ORDER BY st1.departureTime")
    List<Departure> getDepartures(String groupName, String service);

    @Query("SELECT stopName, departureTime " +
            "FROM `Stop Times`" +
            "NATURAL JOIN Stops " +
            "WHERE tripId = :tripId AND stopSequence > :stopSequence " +
            "ORDER BY stopSequence ")
    List<TripStop> getTripStops(Integer tripId, Integer stopSequence);

    @Insert
    void insert(StopTime stopTime);
}
