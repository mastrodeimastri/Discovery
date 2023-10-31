package com.example.igproject.Models;
import androidx.room.*;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "Routes")
public class Route {

    @PrimaryKey
    @ColumnInfo(name = "routeId")
    @NotNull
    public Integer routeId;

    @ColumnInfo(name = "routeName")
    @NotNull
    public String routeName;

    @ColumnInfo(name = "routeColor")
    @NotNull
    public String routeColor;

    @ColumnInfo(name = "routeTextColor")
    @NotNull
    public String routeTextColor;

    public Route(){}

    public Route(String rId, String rName, String rC, String rTC) {
        this.routeId = Integer.parseInt(rId);
        this.routeName = rName;
        this.routeColor = rC;
        this.routeTextColor = rTC;
    }
}
