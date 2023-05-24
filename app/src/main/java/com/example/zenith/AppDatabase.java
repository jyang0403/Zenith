package com.example.zenith;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Country.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CountryDao countryDao();
}