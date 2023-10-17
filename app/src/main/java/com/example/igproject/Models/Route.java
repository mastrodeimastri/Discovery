package com.example.igproject.Models;
import androidx.room.*;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "Routes")
public class Route {

    @PrimaryKey
    @ColumnInfo(name = "routeId")
    private Integer routeId;

    @ColumnInfo(name = "routeName")
    private String routeName;
}
