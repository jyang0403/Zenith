package com.example.zenith;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.zenith.ui.CountryListAdapter;
import com.example.zenith.ui.FilterListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CountryListActivity extends AppCompatActivity {

    Intent curIntent;
    List<Country> countryList;
    List<String> filterList;
    TextView no_results;
    RecyclerView countryRecyclerView;
    RecyclerView filterRecyclerView;
    FilterListAdapter filterAdapter;

    String continent;

    private List<String> items;

    CountryListAdapter countryAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new ArrayList<>();

        setContentView(R.layout.activity_country_list);
        countryRecyclerView = findViewById(R.id.mRecyclerView);
        filterRecyclerView = findViewById(R.id.filterRecyclerView);
        curIntent = getIntent();

        //no results if there are no countries that match search
        no_results = findViewById(R.id.no_result);
        no_results.setVisibility(TextView.INVISIBLE);

        // set action bar title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Countries");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);

        setupCountries();
        setupFilters();

        countryAdapter = new CountryListAdapter(this, countryList);
        countryRecyclerView.setAdapter(countryAdapter);
        countryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        filterAdapter = new FilterListAdapter(this, filterList, continent);
        filterRecyclerView.setAdapter(filterAdapter);
        filterRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

    }

    @Override
    public void onResume(){
        super.onResume();
        countryAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_country_list, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // Clear the list
                // Clear the list
                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "Countries").allowMainThreadQueries().fallbackToDestructiveMigration().build();
                CountryDao dao = db.countryDao();
                items.clear();
                no_results.setVisibility(View.INVISIBLE);

                // Add the search results to the list
                for (int i = 0; i < 10; i++) {
                    items.add(query + " " + i);
                }

                countryList = dao.search(query);
                setupFilters();
                countryAdapter = new CountryListAdapter(getApplicationContext(), countryList);
                countryRecyclerView.setAdapter(countryAdapter);

                db.close();

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupCountries(){
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "Countries").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        CountryDao dao = db.countryDao();

        continent = curIntent.getStringExtra("continent");
        boolean bookmarked = curIntent.getBooleanExtra("bookmarked", false);
        String search = curIntent.getStringExtra("search");
        if (continent != null)
        {
            if (continent.equals("All"))
            {
                countryList = dao.getAll();
            } else
            {
                countryList = dao.getContinent(continent);
            }
        } else if (bookmarked)
        {
            countryList = dao.getFavorites(1);
            if (countryList.isEmpty()) {
                countryRecyclerView.setVisibility(View.INVISIBLE);
                no_results.setVisibility(TextView.VISIBLE);
            }
        } else if (search != null)
        {
            countryList = dao.search(search);
            if (countryList.isEmpty()) {
                countryRecyclerView.setVisibility(View.INVISIBLE);
                no_results.setVisibility(TextView.VISIBLE);
            }
        } else
        {
            countryList = dao.getAll();
        }
    }

    public void setupCountries(String filter)
    {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "Countries").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        CountryDao dao = db.countryDao();

        no_results.setVisibility(TextView.INVISIBLE);

        if(filter.equals("All"))
        {
            countryList = dao.getAll();
        } else {
            countryList = dao.getContinent(filter);
        }
        countryAdapter = new CountryListAdapter(this, countryList);
        countryRecyclerView.setAdapter(countryAdapter);

        for (int childCount = filterRecyclerView.getChildCount(), i = 0; i < childCount; i++)
        {
            final FilterListAdapter.MyViewHolder holder = (FilterListAdapter.MyViewHolder) filterRecyclerView.getChildViewHolder(filterRecyclerView.getChildAt(i));
            holder.filterButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.screen_background));
        }

        countryAdapter.notifyDataSetChanged();
        no_results.setVisibility(TextView.INVISIBLE);
        countryRecyclerView.setVisibility(View.VISIBLE);
        db.close();
    }

    private void setupFilters(){
        filterList = new ArrayList<String>();
        filterList.add("All");
        filterList.addAll(Arrays.asList(ZenithAPIHelper.getContinentList()));
    }

}