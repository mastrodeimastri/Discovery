package com.example.igproject.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.example.igproject.Data.AppDatabase;
import com.example.igproject.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "applicationDb").build();

        setContentView(R.layout.activity_main);
    }
}