package com.example.zenith.ui.country;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.zenith.AppDatabase;
import com.example.zenith.Country;
import com.example.zenith.CountryDao;
import com.example.zenith.R;
import com.example.zenith.ZenithAPIHelper;
//import com.example.zenith.ZenithAPIHelper.WikiApiCallback;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.zenith.databinding.ActivityCountryBinding;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class CountryActivity extends AppCompatActivity {

    private ActivityCountryBinding binding;

    public static String countryName;

    AppDatabase db;

    CountryDao dao;

    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding = ActivityCountryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // get country Name as an extra
        Bundle extra = getIntent().getExtras();
        if(extra == null) countryName = "[Country Name]"; else countryName = extra.getString("country", "[Country Name]");

        ZenithAPIHelper api = new ZenithAPIHelper();
        code = api.getCountryCode(countryName).toUpperCase();

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "Countries").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        dao = db.countryDao();

        // set custom action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(countryName);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);


        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager(), countryName);
        ViewPager viewPager = binding.viewPager;

        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.menu_country_flag);
        ImageView flag = (ImageView) alertMenuItem.getActionView();

        String flag_link = "https://flagsapi.com/"+code+"/shiny/64.png";
        Picasso.get().load(flag_link).into(flag);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_country, menu);
        MenuItem item = menu.findItem(R.id.bookmark);

        Country country = dao.findByName(countryName);
        if(country.favorite == 1)
            checkBookmark(item);
        else unCheckBookmark(item);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bookmark:
                updateBookmark(item);
                return true;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateBookmark (MenuItem item ) {
        if (item.isChecked()) {
            unCheckBookmark(item);
            dao.updateFavorite(countryName, 0);
        } else {
            checkBookmark(item);
            dao.updateFavorite(countryName, 1);
        }
    }

    private void checkBookmark(MenuItem item) {
        item.setChecked(true);
        item.setIcon(R.drawable.bookmark_filled);
    }

    private void unCheckBookmark(MenuItem item) {
        item.setChecked(false);
        item.setIcon(R.drawable.bookmark_border);
    }
}