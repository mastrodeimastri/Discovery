package com.example.igproject.ViewModels;

import android.accounts.NetworkErrorException;
import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.igproject.Data.AppDatabase;
import com.example.igproject.Models.Route;
import com.example.igproject.Models.Stop;
import com.example.igproject.Models.StopTime;
import com.example.igproject.Models.Trip;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SettingsViewModel extends ViewModel{

    private static final String _targetUrl = "https://actv.avmspa.it/sites/default/files/attachments/opendata/navigazione/actv_nav.zip";
    private static final String _fileName = "actv_nav.zip";
    private static HttpURLConnection connection;

    private static AppDatabase db;

    public SettingsViewModel(Context context) {
        db = AppDatabase.getInstance(context);
    }

    public void parseData() throws NetworkErrorException {

        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();


        // costruisco un oggetto URl per andare a fare una chiamata HTTP
        try {

            URL url = new URL(_targetUrl);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.connect();

            int status = connection.getResponseCode();

            if(status > 299){
                // qua vado a gestire il caso in cui ricevo un errore dall'endpoint
                throw new NetworkErrorException("La connessione all'endpoint non è andata a buon fine");

            } else {

                //qua vado a costruire uno stream di input per file zip da quello della connessione
                ZipInputStream zipInputStream = new ZipInputStream(connection.getInputStream());

                // vado a pulire il db per prepararlo ai nuovi dati
                db.clearAllTables();

                ZipEntry entry;

                // qua vado a parsare tutti le entry
                while((entry = zipInputStream.getNextEntry()) != null) {
                    db.beginTransaction();
                    CSVParser csv = CSVFormat.DEFAULT.parse(new InputStreamReader(zipInputStream));
                    Integer counter = 0;
                    for(CSVRecord record : csv) {
                        if(counter > 0) {
                            switch (entry.getName()){
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
                                    db.stopDAO().insert(new Stop(
                                            record.get(0),
                                            record.get(2),
                                            record.get(4),
                                            record.get(5)
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
                db.endTransaction();
                // devo chiudere l'ultima entry
                zipInputStream.closeEntry();
                zipInputStream.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
