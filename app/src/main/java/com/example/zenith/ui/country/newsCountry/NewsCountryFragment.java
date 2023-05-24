package com.example.zenith.ui.country.newsCountry;

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

import com.example.zenith.databinding.FragmentNewsCountryBinding;

public class NewsCountryFragment extends Fragment {
    private FragmentNewsCountryBinding binding;
    public String[] newsCategories;
    public String countryName;
    public static NewsCountryFragment newInstance() {
        return new NewsCountryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNewsCountryBinding.inflate(inflater, container, false);
        RecyclerView mRecyclerView = binding.newsRecyclerView;
        View root = binding.getRoot();

        // get country Name as an extra
        countryName = this.getArguments().getString("country");

        newsCategories = new String[]{"Business","Sports","Health", "Science", "Entertainment", "Technology","General"};
        NewsAdapter newsAdapter = new NewsAdapter(newsCategories, getContext(), countryName);
        mRecyclerView.setAdapter(newsAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
