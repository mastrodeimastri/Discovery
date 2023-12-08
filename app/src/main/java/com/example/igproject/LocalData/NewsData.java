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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsData implements Parcelable {
    private MainActivityListener mainActivityListener;
    private int updated;
    public NewsArticle[] newsArticles;

    public NewsData(){
        updated = 0;
    }

    protected NewsData(Parcel in) {
        updated = in.readInt();
        newsArticles = in.createTypedArray(NewsArticle.CREATOR);
    }

    public static final Creator<NewsData> CREATOR = new Creator<NewsData>() {
        @Override
        public NewsData createFromParcel(Parcel in) {
            return new NewsData(in);
        }

        @Override
        public NewsData[] newArray(int size) {
            return new NewsData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeInt(updated);
        parcel.writeTypedArray(newsArticles, flags);
    }

    public void loadData(){
        //Get stuff form API without blocking ui
        Thread thread = new Thread(new LoadData());
        thread.start();
    }

    private class LoadData implements Runnable{
        @Override
        public void run() {
            String data = requestData();
            parseData(data);

            synchronized (this){
                updated = 1;
            }

            Handler threadHandler = new Handler(Looper.getMainLooper());
            threadHandler.post(NewsData.this::notifyDataUpdated);
        }

        private String requestData(){
            try {
                String API_URL = "https://newsapi.org/v2/everything?q=venezia&language=it&apiKey=9ec209089c754d50b2c07af02c6f24c7";

                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(API_URL)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        return response.body().string();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private void parseData(String data){
            try {
                JSONObject newsData = new JSONObject(data);
                JSONArray articles = newsData.getJSONArray("articles");

                if (articles.length() > 20){
                    newsArticles = new NewsArticle[20];
                }
                else {
                    newsArticles = new NewsArticle[articles.length()];
                }

                for (int i = 0; i < articles.length() && i < 20; i++) {
                    JSONObject article = articles.getJSONObject(i);

                    String author = article.getJSONObject("source").getString("name");
                    String title = article.getString("title");
                    String url = article.getString("url");
                    String urlToImage = article.getString("urlToImage");

                    newsArticles[i] = new NewsArticle(author, title, url, urlToImage);
                }
            } catch (JSONException e) {
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
