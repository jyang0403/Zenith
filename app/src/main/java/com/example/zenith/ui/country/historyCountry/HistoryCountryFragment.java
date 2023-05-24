package com.example.zenith.ui.country.historyCountry;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.zenith.R;
import com.example.zenith.ZenithAPIHelper;
//import com.example.zenith.ZenithAPIHelper.WikiApiCallback;
import com.example.zenith.databinding.FragmentHistoryCountryBinding;
import com.example.zenith.ui.country.HistoryCultureAdapter;

import java.util.LinkedHashMap;

public class HistoryCountryFragment extends Fragment {
    private FragmentHistoryCountryBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryCountryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // get country Name as an extra
        String countryName = this.getArguments().getString("country");
        RecyclerView mRecyclerView = binding.historyRecyclerView;

        ProgressBar loading = root.findViewById(R.id.history_loading);

        if(countryName.equals("Czechia")) countryName = "Czech Republic";
        String finalCountryName = countryName;

        new ZenithAPIHelper().getCountryBackground(countryName, getContext(), "History", new ZenithAPIHelper.CountryApiCallback() {

            @Override
            public void getDetails (LinkedHashMap<String, String> sections){
                if(sections == null) {
                    loading.setVisibility(View.GONE);
                    binding.countryErrorMsg.setVisibility(View.VISIBLE);
                } else {
                    loading.setVisibility(View.GONE);
                    HistoryCultureAdapter buttonAdapter = new HistoryCultureAdapter(sections, R.color.earth , finalCountryName, getContext());
                    mRecyclerView.setAdapter(buttonAdapter);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}