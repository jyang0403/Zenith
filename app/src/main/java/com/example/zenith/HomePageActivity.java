package com.example.zenith;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "Countries").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        CountryDao dao = db.countryDao();
//        dao.deleteAllCountries();
        String[] countryNames = ZenithAPIHelper.getCountryFullNames();
        String[] countryContinents = ZenithAPIHelper.getCountryContinents();


        //Initialize country database if it does not yet exist
        for (int i = 0; i < countryNames.length; i++) {
            if (dao.findByName(countryNames[i]) == null) {
                Country country = new Country(countryNames[i], 0, countryContinents[i]);
                dao.insertAll(country);
            }
        }
        TextClock clock = findViewById(R.id.clock);
        clock.setFormat12Hour("hh:mm:ss A");

        Button beginButton = findViewById(R.id.beginButton);
        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}