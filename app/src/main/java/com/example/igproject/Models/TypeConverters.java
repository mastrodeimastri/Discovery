package com.example.igproject.Models;

import androidx.room.TypeConverter;

import java.time.LocalTime;

public class TypeConverters {

    @TypeConverter
    public static LocalTime fromString(String value) {
        return value == null ? null : LocalTime.parse(value);
    }

    @TypeConverter
    public static String localTimeToString(LocalTime value) {
        return value == null ? null : value.toString();
    }
}
