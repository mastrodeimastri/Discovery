package com.example.igproject.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.igproject.Models.StopGroup;

import java.util.List;

@Dao
public interface StopGroupDAO{

    @Query("SELECT * FROM StopGroups")
    List<StopGroup> getAll();

    @Insert
    void insertAll(List<StopGroup> stopGroups);

    @Query("SELECT * FROM StopGroups WHERE stopGroupName = :name")
    StopGroup getEntity(String name);
}
