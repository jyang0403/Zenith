package com.example.zenith.ui.country.culture;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.zenith.R;
import com.example.zenith.ZenithAPIHelper;
import com.example.zenith.databinding.FragmentCultureBinding;
import com.example.zenith.ui.country.HistoryCultureAdapter;

import java.util.LinkedHashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CultureCountryFragment} factory method to
 * create an instance of this fragment.
 */
public class CultureCountryFragment extends Fragment {
    private FragmentCultureBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCultureBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // get country Name as an extra
        String countryName = this.getArguments().getString("country");
        RecyclerView mRecyclerView = binding.cultureRecyclerView;

        if(countryName.equals("Czechia")) countryName = "Czech Republic";
        String finalCountryName = countryName;

        ProgressBar loading = root.findViewById(R.id.culture_loading);

        new ZenithAPIHelper().getCountryBackground(countryName, getContext(), "Culture",new ZenithAPIHelper.CountryApiCallback() {

            @Override
            public void getDetails(LinkedHashMap<String, String> sections) {
                if(sections == null) {
                    loading.setVisibility(View.GONE);
                    binding.countryErrorMsg.setVisibility(View.VISIBLE);
                } else {
                    loading.setVisibility(View.GONE);
                    HistoryCultureAdapter buttonAdapter = new HistoryCultureAdapter(sections, R.color.cult_blue, finalCountryName, getContext());
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