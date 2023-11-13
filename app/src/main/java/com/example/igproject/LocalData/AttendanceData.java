package com.example.igproject.LocalData;

import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class AttendanceData implements Parcelable {
    private MainActivityListener mainActivityListener;
    private int updated;
    public AttendanceDay[] attendanceDays;

    public AttendanceData(){
        updated = 0;
    }

    protected AttendanceData(Parcel in) {
        updated = in.readInt();
        attendanceDays = in.createTypedArray(AttendanceDay.CREATOR);
    }

    public static final Creator<AttendanceData> CREATOR = new Creator<AttendanceData>() {
        @Override
        public AttendanceData createFromParcel(Parcel in) {
            return new AttendanceData(in);
        }

        @Override
        public AttendanceData[] newArray(int size) {
            return new AttendanceData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeInt(updated);
        parcel.writeTypedArray(attendanceDays, flags);
    }

    public void loadData(){
        //Get stuff form API without blocking ui
        Thread thread = new Thread(new LoadData());
        thread.start();
    }

    private class LoadData implements Runnable{
        @Override
        public void run() {
            //String data = requestData();
            String data = "{\"analysis\":{\"week_raw\":[{\"day_info\":{\"day_int\":0,\"day_max\":81,\"day_mean\":47,\"day_rank_max\":5,\"day_rank_mean\":5,\"day_text\":\"Monday\",\"venue_closed\":20,\"venue_open\":7},\"day_int\":0,\"day_raw\":[0,10,10,20,30,45,55,65,75,80,75,65,45,30,15,0,0,0,0,0,0,0,0,0]},{\"day_info\":{\"day_int\":1,\"day_max\":70,\"day_mean\":46,\"day_rank_max\":7,\"day_rank_mean\":6,\"day_text\":\"Tuesday\",\"venue_closed\":20,\"venue_open\":7},\"day_int\":1,\"day_raw\":[0,10,15,25,35,45,55,60,70,70,70,65,50,35,20,0,0,0,0,0,0,0,0,0]},{\"day_info\":{\"day_int\":2,\"day_max\":74,\"day_mean\":45,\"day_rank_max\":6,\"day_rank_mean\":7,\"day_text\":\"Wednesday\",\"venue_closed\":20,\"venue_open\":7},\"day_int\":2,\"day_raw\":[0,5,10,15,30,40,55,65,75,75,70,60,45,35,20,0,0,0,0,0,0,0,0,0]},{\"day_info\":{\"day_int\":3,\"day_max\":82,\"day_mean\":54,\"day_rank_max\":4,\"day_rank_mean\":3,\"day_text\":\"Thursday\",\"venue_closed\":20,\"venue_open\":7},\"day_int\":3,\"day_raw\":[0,10,15,25,35,50,65,75,80,80,80,75,60,45,25,0,0,0,0,0,0,0,0,0]},{\"day_info\":{\"day_int\":4,\"day_max\":84,\"day_mean\":53,\"day_rank_max\":3,\"day_rank_mean\":4,\"day_text\":\"Friday\",\"venue_closed\":20,\"venue_open\":7},\"day_int\":4,\"day_raw\":[0,10,20,25,35,50,60,70,80,85,80,70,60,40,25,0,0,0,0,0,0,0,0,0]},{\"day_info\":{\"day_int\":5,\"day_max\":100,\"day_mean\":59,\"day_rank_max\":1,\"day_rank_mean\":2,\"day_text\":\"Saturday\",\"venue_closed\":20,\"venue_open\":7},\"day_int\":5,\"day_raw\":[0,5,15,25,35,45,65,80,95,100,100,90,70,50,30,0,0,0,0,0,0,0,0,0]},{\"day_info\":{\"day_int\":6,\"day_max\":98,\"day_mean\":61,\"day_rank_max\":2,\"day_rank_mean\":1,\"day_text\":\"Sunday\",\"venue_closed\":20,\"venue_open\":7},\"day_int\":6,\"day_raw\":[0,5,15,25,40,60,75,85,90,95,100,85,70,45,25,0,0,0,0,0,0,0,0,0]}]},\"status\":\"OK\",\"venue_address\":\"30135 Sestriere Santa Croce, Venezia VE Italy\",\"venue_id\":\"ven_49787456746876306a523452636b667865736c693666524a496843\",\"venue_name\":\"Giardini Papadopoli\",\"window\":{\"day_window_end_int\":6,\"day_window_end_txt\":\"Monday\",\"day_window_start_int\":0,\"day_window_start_txt\":\"Monday\",\"time_window_end\":5,\"time_window_end_12h\":\"5AM\",\"time_window_start\":6,\"time_window_start_12h\":\"6AM\",\"week_window\":\"Monday 6AM until Monday 5AM next week\"}}";
            parseData(data);

            //UI update needs to be handled be the main thread so its given to handler
            //Synchronized to avoid thread issues
            synchronized (this){
                updated = 1;
            }

            Handler threadHandler = new Handler(Looper.getMainLooper());
            threadHandler.post(AttendanceData.this::notifyDataUpdated);
        }

        private String requestData(){
            try {
                String apiUrl = "https://besttime.app/api/v1/forecasts/week/raw2?api_key_public=pub_fc98537a97de4df9878e5f5cdb0cda89&venue_id=ven_49787456746876306a523452636b667865736c693666524a496843";

                URL url = new URL(apiUrl);

                StringBuilder json = new StringBuilder();

                //Try-with-resources statement, ensures that even with exceptions resources are closed properly
                try (InputStream input = url.openStream();
                     InputStreamReader isr = new InputStreamReader(input);
                     BufferedReader reader = new BufferedReader(isr)) {

                    int c;
                    while ((c = reader.read()) != -1) {
                        json.append((char) c);
                    }

                    return json.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private void parseData(String data){
            try {
                JSONObject attendanceData = new JSONObject(data);

                JSONObject analysis = attendanceData.getJSONObject("analysis");
                JSONArray weekRawArray = analysis.getJSONArray("week_raw");

                attendanceDays = new AttendanceDay[weekRawArray.length()];

                for (int i = 0; i < weekRawArray.length(); i++) {
                    JSONObject dayInfo = weekRawArray.getJSONObject(i).getJSONObject("day_info");
                    int dayInt = dayInfo.getInt("day_int");

                    JSONArray dayRawArray = weekRawArray.getJSONObject(i).getJSONArray("day_raw");
                    int[] dayRaw = new int[dayRawArray.length()]; //From 6am to next day 5am
                    for (int j = 0; j < dayRawArray.length(); j++) {
                        dayRaw[j] = dayRawArray.getInt(j);
                    }

                    attendanceDays[i] = new AttendanceDay(dayInt, dayRaw);
                }
            } catch (
            JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //Update news ui by notifying the listener
    private void notifyDataUpdated() {
        if (mainActivityListener != null) {
            mainActivityListener.onDataUpdates("news");
        }
    }

    //Set the listener for ui updating
    public void setMainActivityListener(MainActivityListener listener){
        this.mainActivityListener = listener;
    }

    //Check if the async load has ended
    public boolean isDataLoaded(){
        //Synchronized to avoid thread issues
        synchronized (this) {
            return updated == 1;
        }
    }
}
