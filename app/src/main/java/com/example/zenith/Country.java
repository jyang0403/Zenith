package com.example.zenith;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Country {

    public Country(String name, int favorite, String continent) {
        this.name = name;
        this.favorite = favorite;
        this.continent = continent;
    }

    @PrimaryKey(autoGenerate = true)
    public int cid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "favorite")
    public int favorite;

    @ColumnInfo(name = "continent")
    public String continent;

    public String image;

    public Country() {

    }
}