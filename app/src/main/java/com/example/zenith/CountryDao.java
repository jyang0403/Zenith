package com.example.zenith;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.zenith.Country;

import java.util.List;

/*
    Example usage:

    Create database:
    final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "country").fallbackToDestructiveMigration().build();

    the fallBackToDestructiveMigration() part just makes it so whenever we change
    the database, it will erase itself. This way, we don't have to worry about migrations
    but we do have to re-add all countries in the case that we change the Country class or DB in general


    You can't run a query on the main thread, so you have to do something like this:

    List<Country>[] list = new List[1];
    AsyncTask.execute(new Runnable() {
        @Override
        public void run() {
            // Insert Data
            db.countryDao().insertAll(new Country("United States", 0, "North America"));
            list[0] = db.countryDao().getAll();
            for (Country c : list[0]) {
                 System.out.println(c.name);
             }
                 list[0] = db.countryDao().getFavorites(1);
          }
     });

     beware of null references, you can put callback functions inside the
     .execute() if you need something to happen strictly after the data is loaded

 */

@Dao
public interface CountryDao {
    @Query("SELECT * FROM country")
    List<Country> getAll();

    @Query("SELECT * FROM country WHERE favorite = :favorite")
    List<Country> getFavorites(int favorite);

    @Query("SELECT * FROM country WHERE continent = :continent")
    List<Country> getContinent(String continent);

    @Query("SELECT * FROM country WHERE name = :name LIMIT 1")
    Country findByName(String name);

    @Query("SELECT favorite FROM country WHERE name = :name LIMIT 1")
    int isBookmarked(String name);
    //@Query("SELECT * FROM country WHERE name LIKE :search")
    //@Query("SELECT substr(name, 1, 10) FROM country")
    @Query("SELECT * FROM country WHERE name LIKE '%' || :search || '%'")
    List<Country> search(String search);

    @Query("UPDATE country SET favorite = :favorite WHERE name = :name")
    void updateFavorite(String name, int favorite);

    @Insert
    void insertAll(Country... countries);

    @Delete
    void delete(Country country);

    @Query("DELETE FROM country")
    void deleteAllCountries();

}