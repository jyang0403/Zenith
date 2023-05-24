package com.example.zenith.ui.country.homeCountry;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.zenith.R;
import com.example.zenith.ZenithAPIHelper;
import com.example.zenith.databinding.FragmentHomeCountryBinding;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;


public class HomeCountryFragment extends Fragment {
    private FragmentHomeCountryBinding binding;
    public String countryName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeCountryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // get country Name as an extra
        countryName = this.getArguments().getString("country");

        String idName = "map_" + countryName.toLowerCase().replace(" ", "_");

        try {
            int id = R.drawable.class.getField(idName).getInt(null);
            binding.homeCountryFlag.setImageResource(id);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        ProgressBar loading = root.findViewById(R.id.home_loading);

        RecyclerView mRecyclerView = binding.homeRecycler;

        new ZenithAPIHelper().getCountryDetails(countryName, getContext(), new ZenithAPIHelper.CountryApiCallback() {
            @Override
            public void getDetails(LinkedHashMap<String, String> sections) {
                if (sections == null) {
                    loading.setVisibility(View.GONE);
                    binding.homeErrorMsg.setVisibility(View.VISIBLE);
                } else {
                    loading.setVisibility(View.GONE);
                    HomeAdapter homeAdapter = new HomeAdapter(cleanSections(sections));
                    mRecyclerView.setAdapter(homeAdapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }
        });

        return root;
    }


    private LinkedHashMap<String, String> cleanSections(LinkedHashMap<String, String> sections) {
        LinkedHashMap<String, String> newSections = new LinkedHashMap<>();
        Set<String> keys = sections.keySet();

        for (String k: keys) {
            String v = sections.get(k);
            if(k.equals("name")) break;
            if(k.equals("currency")) {
                assert v != null;
                String[] arr = v.split("\",\"");
                String code = arr[0].split(":\"",2)[1];
                String name = arr[1].split(":\"",2)[1].split("\"")[0];
                v = name + " (" + code + ")";
            }
            newSections.put(cleanString(k), v);
        }

        return newSections;
    }

    private String cleanString(String str) {

        String [] str_arr = str.split("_");
        StringBuilder sb = new StringBuilder();
        for (String s: str_arr) {
            String c = Character.toString(s.charAt(0));
            String sNew = c.toUpperCase() + s.split(c, 2)[1];
            sb.append(sNew).append(" ");
        }

        return sb.toString().trim();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}