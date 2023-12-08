package com.example.igproject.LocalData;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class NewsArticle implements Parcelable {
    public String author, title, articleLink, imageLink;

    public NewsArticle(String author, String title, String articleLink, String imageLink){
        this.author = author;
        this.title = title;
        this.articleLink = articleLink;
        this.imageLink = imageLink;
    }

    protected NewsArticle(Parcel in) {
        author = in.readString();
        title = in.readString();
        articleLink = in.readString();
        imageLink = in.readString();
    }

    public static final Creator<NewsArticle> CREATOR = new Creator<NewsArticle>() {
        @Override
        public NewsArticle createFromParcel(Parcel in) {
            return new NewsArticle(in);
        }

        @Override
        public NewsArticle[] newArray(int size) {
            return new NewsArticle[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(articleLink);
        dest.writeString(imageLink);
    }
}
