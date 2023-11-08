package com.example.igproject.ViewModels;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.igproject.Data.AppDatabase;
import com.example.igproject.Models.Route;
import com.example.igproject.Models.Stop;
import com.example.igproject.Models.StopGroup;
import com.example.igproject.Models.StopTime;
import com.example.igproject.Models.Trip;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MapViewModel extends ViewModel{

    private static final String _targetUrl = "https://actv.avmspa.it/sites/default/files/attachments/opendata/navigazione/actv_nav.zip";
    private static final String _fileName = "actv_nav.zip";
    private static HttpURLConnection connection;

    private static AppDatabase db;

    public MapViewModel(Context context) {

        db = AppDatabase.getInstance(context);


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
                                            record.get(2))
                                    );
                                    break;
                                case "stops.txt":

                                    String stopGroupName = record.get(2).replaceAll("\"(.*?)\"","");
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
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void importData(){
        db.runInTransaction(() -> {parseData();});
    }

    public List<StopGroup> renderStops() {

        return db.stopGroupDAO().getAll();
    }
}
