package com.example.igproject.ViewModels;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;

import androidx.lifecycle.ViewModel;

import com.example.igproject.Data.AppDatabase;
import com.example.igproject.Models.Calendar;
import com.example.igproject.Models.CalendarDates;
import com.example.igproject.Models.Departure;
import com.example.igproject.Models.Route;
import com.example.igproject.Models.Stop;
import com.example.igproject.Models.StopGroup;
import com.example.igproject.Models.StopTime;
import com.example.igproject.Models.Trip;
import com.example.igproject.Models.TripStop;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import kotlin.text.Regex;

public class MapViewModel extends ViewModel{

    private static final String _targetUrl = "https://actv.avmspa.it/sites/default/files/attachments/opendata/navigazione/actv_nav.zip";
    private static final String _fileName = "actv_nav.zip";
    private static HttpURLConnection connection;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private static AppDatabase db;

    public MapViewModel(Context context) {
        db = AppDatabase.getInstance(context);
    }

    public Date getTodayDate() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("CET"));
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    private void parseData() {

        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();


        // costruisco un oggetto URl per andare a fare una chiamata HTTP
        try {

            URL url = new URL(_targetUrl);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // TODO: cambiare il tempo di timeout
            connection.setConnectTimeout(Integer.MAX_VALUE);
            connection.connect();

            int status = connection.getResponseCode();

            if(status > 299){
                // qua vado a gestire il caso in cui ricevo un errore dall'endpoint
                throw new RuntimeException("La connessione all'endpoint non è andata a buon fine");

            } else {

                //qua vado a costruire uno stream di input per file zip da quello della connessione
                ZipInputStream zipInputStream = new ZipInputStream(connection.getInputStream());

                // vado a pulire il db per prepararlo ai nuovi dati
                db.clearAllTables();

                ZipEntry entry;
                // qua vado a parsare tutti le entry
                while((entry = zipInputStream.getNextEntry()) != null) {

                    CSVParser csv = CSVFormat.DEFAULT.parse(new InputStreamReader(zipInputStream));
                    Integer counter = 0;
                    for (CSVRecord record : csv) {
                        if (counter > 0) {
                            switch (entry.getName()) {
                                case "routes.txt":
                                    db.routeDAO().insert(new Route(
                                            record.get(0),
                                            record.get(2),
                                            record.get(7),
                                            record.get(8))
                                    );
                                    break;
                                case "trips.txt":
                                    db.tripDAO().insert(new Trip(
                                            record.get(0),
                                            record.get(2),
                                            record.get(1))
                                    );
                                    break;
                                case "calendar.txt":
                                    db.calendarDAO().insert(new Calendar(
                                            record.get(0),
                                            record.get(1),
                                            record.get(2),
                                            record.get(3),
                                            record.get(4),
                                            record.get(5),
                                            record.get(6),
                                            record.get(7),
                                            record.get(8),
                                            record.get(9)
                                    ));
                                    break;
                                case "calendar_dates.txt":
                                    db.calendarDatesDAO().insert(new CalendarDates(
                                            record.get(0),
                                            record.get(1),
                                            record.get(2)));
                                    break;
                                case "stops.txt":

                                    String stopGroupName = record.get(2).replaceAll("\"(.*?)\"","").replaceAll(" A RICHIESTA", "");

                                    String stopLat = record.get(4);
                                    String stopLon = record.get(5);

                                    StopGroup sG = db.stopGroupDAO().getEntity(stopGroupName);

                                    if(sG == null) {
                                        sG = new StopGroup(stopGroupName, stopLat, stopLon);
                                        db.stopGroupDAO().insert(sG);
                                    }

                                    db.stopDAO().insert(new Stop(
                                            record.get(0),
                                            record.get(2),
                                            stopLat,
                                            stopLon,
                                            stopGroupName
                                    ));


                                    break;
                                case "stop_times.txt":
                                    db.stopTimeDAO().insert(new StopTime(
                                            record.get(0),
                                            record.get(1),
                                            record.get(2),
                                            record.get(3),
                                            record.get(4)
                                    ));
                                    break;
                            }

                        }

                        counter++;
                    }

                    // chiudo l'entry appena parsata
                    zipInputStream.closeEntry();
                }

                // devo chiudere l'ultima entry
                zipInputStream.closeEntry();
                zipInputStream.close();
                db.flag = 1;
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void importData(){
        db.runInTransaction(() -> {
            parseData();
        });
    }

    public List<StopGroup> renderStops() {
         try {

             if(db.flag == 0) {
                 importData();
             } else {

                 Date endDate = dateFormat.parse(String.valueOf(db.calendarDAO().getEndDate().get(0)));

                 if(endDate.compareTo(getTodayDate()) < 0) {
                     importData();
                 }
             }

             return db.stopGroupDAO().getAll();

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Departure> getDepartures(String groupName, Date date){

        String service = null;

        if(isSpecialDate(date)){
            // ottengo i servizi relativi al giorno speciale in input
            service = db.calendarDatesDAO().getServices(Integer.parseInt(dateFormat.format(date)));
        } else {

            switch(date.getDay()){
                case 1:
                    service = db.calendarDAO().getSundayService();
                    break;
                case 2:
                    service = db.calendarDAO().getMondayService();
                    break;
                case 3:
                    service = db.calendarDAO().getTuesdayService();
                    break;
                case 4:
                    service = db.calendarDAO().getWednesdayService();
                    break;
                case 5:
                    service = db.calendarDAO().getThursdayService();
                    break;
                case 6:
                    service = db.calendarDAO().getFridayService();
                    break;
                case 7:
                    service = db.calendarDAO().getSaturdayService();
                    break;
            }
            //services = db.calendarDAO().getServices(date);
        }
        return db.stopTimeDAO().getDepartures(groupName, service);
    }

    public List<TripStop> getTripStops(Integer tripId, Integer stopSequence ) {
        return db.stopTimeDAO().getTripStops(tripId, stopSequence);
    }

    private Boolean isSpecialDate(Date date) {

        List<Integer> specialDates = db.calendarDatesDAO().getDates();
        for(Integer d : specialDates){
            try {
                if(date.compareTo(dateFormat.parse(String.valueOf(d))) == 0){
                    return true;
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
