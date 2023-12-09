package com.example.igproject.ViewModels;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;

import androidx.lifecycle.ViewModel;

import com.example.igproject.Data.AppDatabase;
import com.example.igproject.Models.CalendarDates;
import com.example.igproject.Models.Departure;
import com.example.igproject.Models.Route;
import com.example.igproject.Models.Stop;
import com.example.igproject.Models.StopGroup;
import com.example.igproject.Models.StopTime;
import com.example.igproject.Models.Trip;
import com.example.igproject.Models.TripStop;
import com.google.android.gms.maps.StreetViewPanoramaOptions;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import kotlin.text.Regex;

public class MapViewModel extends ViewModel{

    private static final String _targetUrl = "https://actv.avmspa.it/sites/default/files/attachments/opendata/navigazione/actv_nav.zip";

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private AppDatabase db;

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

    private HttpURLConnection setUpConnection() throws IOException {
            HttpURLConnection result = (HttpURLConnection) new URL(_targetUrl).openConnection();
            result.setRequestMethod("GET");
            result.setConnectTimeout(10000);

            return result;
    }

    private void doBulkImport(
            List<Route> routes,
            List<Stop> stops,
            List<StopGroup> stopGroups,
            List<StopTime> stopTimes,
            List<Trip> trips,
            List<com.example.igproject.Models.Calendar> calendars,
            List<CalendarDates> calendarDates) {

        db.runInTransaction(() -> {
            db.calendarDatesDAO().insertAll(calendarDates);
            db.calendarDAO().insertAll(calendars);
            db.routeDAO().insertAll(routes);
            db.tripDAO().insertAll(trips);
            db.stopDAO().insertAll(stops);
            db.stopGroupDAO().insertAll(stopGroups);
            db.stopTimeDAO().insertAll(stopTimes);
        });

    }

    private void parseData() throws IOException {

        String line;
        Integer counter = 0;
        BufferedReader reader;
        StringBuffer responseContent;
        ZipInputStream zipInputStream;
        ZipEntry entry;
        CSVParser csvParser;
        HttpURLConnection connection = setUpConnection();
        List<com.example.igproject.Models.Calendar  > calendars = new ArrayList<>();
        List<CalendarDates> calendarDates = new ArrayList<>();
        List<Route> routes = new ArrayList<>();
        List<Stop> stops = new ArrayList<>();
        List<StopGroup> stopGroups = new ArrayList<>();
        List<StopTime> stopTimes = new ArrayList<>();
        List<Trip> trips = new ArrayList<>();

        if(connection.getResponseCode() > 299) {

        } else {

            zipInputStream = new ZipInputStream(connection.getInputStream());

            while((entry = zipInputStream.getNextEntry()) != null) {

                csvParser = CSVFormat.DEFAULT.parse(new InputStreamReader(zipInputStream));

                counter = 0;

                for (CSVRecord record : csvParser) {

                    if(counter > 0) {

                        switch (entry.getName()) {
                            case "routes.txt":
                                routes.add(new Route(
                                        record.get(0),
                                        record.get(2),
                                        record.get(7),
                                        record.get(8))
                                );
                                break;
                            case "trips.txt":
                                trips.add(new Trip(
                                        record.get(0),
                                        record.get(2),
                                        record.get(1))
                                );
                                break;
                            case "calendar.txt":
                                calendars.add(new com.example.igproject.Models.Calendar(
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
                                calendarDates.add(new CalendarDates(
                                        record.get(0),
                                        record.get(1),
                                        record.get(2)));
                                break;
                            case "stops.txt":

                                String stopGroupName = record.get(2)
                                        .replaceAll("\"(.*?)\"","")
                                        .replaceAll(" A RICHIESTA", "");


                                String stopLat = record.get(4);
                                String stopLon = record.get(5);

                                if(stopGroups.stream().noneMatch(
                                        e -> e.stopGroupName.equals(stopGroupName)
                                )) {
                                    StopGroup sG = db.stopGroupDAO().getEntity(stopGroupName);

                                    stopGroups.add(new StopGroup(stopGroupName, stopLat, stopLon));
                                  }

                                stops.add(new Stop(
                                        record.get(0),
                                        record.get(2),
                                        stopLat,
                                        stopLon,
                                      stopGroupName
                                ));


                                break;
                            case "stop_times.txt":
                                stopTimes.add(new StopTime(
                                        record.get(0),
                                        record.get(1),
                                        record.get(2),
                                        record.get(3),
                                        record.get(4)
                                ));
                                break;
                        }
                    }
                    // aumento il contatore dei record
                    counter++;
                }
                // chiudo l'entry appena parsata
                zipInputStream.closeEntry();
            }

            // chiudo l'entry nulla e lo stream
            zipInputStream.closeEntry();
            zipInputStream.close();

            doBulkImport(routes, stops, stopGroups, stopTimes, trips, calendars, calendarDates);

            // setto il flag a 1 per far capire che il db è popolato
            db.flag = 1;
        }
    }

    public List<StopGroup> renderStops() {
         try {

             if(db.flag == 0)
                 parseData();
             else {
                if(db.calendarDAO().getEndDate() != null) {
                    Date endDate = dateFormat.parse(String.valueOf(db.calendarDAO().getEndDate().get(0)));

                    if(endDate.compareTo(getTodayDate()) < 0)
                        parseData();
                }
             }

             return db.stopGroupDAO().getAll();

        } catch (IOException e) {
            throw new RuntimeException(e);
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
            int day = date.getDay();
            switch(day){
                case 0:
                    service = db.calendarDAO().getSundayService();
                    break;
                case 1:
                    service = db.calendarDAO().getMondayService();
                    break;
                case 2:
                    service = db.calendarDAO().getTuesdayService();
                    break;
                case 3:
                    service = db.calendarDAO().getWednesdayService();
                    break;
                case 4:
                    service = db.calendarDAO().getThursdayService();
                    break;
                case 5:
                    service = db.calendarDAO().getFridayService();
                    break;
                case 6:
                    service = db.calendarDAO().getSaturdayService();
                    break;
            }
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
