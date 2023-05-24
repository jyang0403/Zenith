package com.example.zenith.ui.explore;



import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.zenith.AppDatabase;
import com.example.zenith.Country;
import com.example.zenith.CountryDao;
import com.example.zenith.CountryListActivity;
import com.example.zenith.databinding.FragmentExploreBinding;
import com.example.zenith.ui.country.CountryActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExploreFragment extends Fragment implements SearchView.OnQueryTextListener {

    private FragmentExploreBinding binding;
    private List<String> continents;

    private List<Country> smallCountryList;

    private List<String> items;

    AppDatabase db;

    CountryDao dao;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentExploreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set up the search view
        items = new ArrayList<>();
        binding.searchExplore.setOnQueryTextListener(this);
        binding.searchExplore.setIconified(false);
        binding.searchExplore.clearFocus();

        db = Room.databaseBuilder(getContext().getApplicationContext(), AppDatabase.class, "Countries").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        dao = db.countryDao();

        binding.slidingLayout.setAnchorPoint(0.5F);

        RecyclerView continentRecycler = binding.recyclerContinents;
        RecyclerView countriesRecycler = binding.recyclerCountries;

        setContinents();
        setupCountries();

        ExploreContinentAdapter continentAdapter = new ExploreContinentAdapter(continents, getContext());
        continentRecycler.setAdapter(continentAdapter);
        continentRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        ExploreCountryAdapter countryAdapter = new ExploreCountryAdapter(smallCountryList, getContext());
        countriesRecycler.setAdapter(countryAdapter);
        countriesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        binding.seeAllButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CountryListActivity.class);
                intent.putExtra("continent", "All");
                startActivity(intent);
            }
        });

        binding.randomCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Country country = getRandomCountry();

                Intent intent = new Intent(getActivity(), CountryActivity.class);
                intent.putExtra("country", country.name);
                startActivity(intent);
            }
        });

        return root;
    }

    private Country getRandomCountry() {
        Random rand = new Random();
        List<Country> unexplored = dao.getFavorites(0);
        if(unexplored.size() == 0) {
            unexplored = dao.getAll();
        }

        db.close();
        return unexplored.get(rand.nextInt(unexplored.size()));
    }

    private void setContinents() {
        continents = new ArrayList<>();
        continents.add("Asia");
        continents.add("North America");
        continents.add("Africa");
        continents.add("Europe");
        continents.add("Oceania");
        continents.add("South America");
    }

    private void setupCountries(){
        List<Country> countries = dao.getAll();
        smallCountryList = new ArrayList<>();

        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            int idx = rand.nextInt(countries.size());
            smallCountryList.add(countries.remove(idx));
        }

        db.close();
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.searchExplore.clearFocus();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // Clear the list
        AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, "Countries").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        CountryDao dao = db.countryDao();
        items.clear();

        // Add the search results to the list
        for (int i = 0; i < 10; i++) {
            items.add(query + " " + i);
        }

        binding.searchExplore.clearFocus();

        List<Country> countryList = dao.search(query);
        db.close();

        if(countryList.size() == 1) {
            Intent intent = new Intent(getContext(), CountryActivity.class);
            intent.putExtra("country", countryList.get(0).name);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), CountryListActivity.class);
            intent.putExtra("search", query);
            startActivity(intent);
        }

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // Clear the list
        items.clear();

        return true;
    }
}
