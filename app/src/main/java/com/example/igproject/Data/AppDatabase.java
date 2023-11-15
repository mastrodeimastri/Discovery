package com.example.igproject.Data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.igproject.DAO.CalendarDAO;
import com.example.igproject.DAO.CalendarDatesDAO;
import com.example.igproject.DAO.RouteDAO;
import com.example.igproject.DAO.StopDAO;
import com.example.igproject.DAO.StopGroupDAO;
import com.example.igproject.DAO.StopTimeDAO;
import com.example.igproject.DAO.TripDAO;
import com.example.igproject.Models.*;

/*
* Questa classe serve per andare a definire il database locale e metodi con i quali interagirci
* */
@TypeConverters(com.example.igproject.Models.TypeConverters.class)
@Database(entities = {Route.class, Stop.class, StopTime.class, StopGroup.class,Trip.class, Calendar.class, CalendarDates.class}, version = 7)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RouteDAO routeDAO();

    public abstract StopDAO stopDAO();

    public abstract StopTimeDAO stopTimeDAO();

    public abstract TripDAO tripDAO();

    public abstract StopGroupDAO stopGroupDAO();

    public abstract CalendarDAO calendarDAO();

    public abstract CalendarDatesDAO calendarDatesDAO();

    //flag per capire se il db era preesistente o viene creato da zero
    public static Integer flag = 0;

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "applicationDb")
                    .fallbackToDestructiveMigration() // Puoi personalizzare questo comportamento
                    .build();

            if(context.getDatabasePath("applicationDb").exists()){
                // se il db esiste già, significa che ha già dei dati dentro.
                // Quindi vado a settare il flag a 1
                flag = 1;
            }
        }

        return instance;
    }
}
