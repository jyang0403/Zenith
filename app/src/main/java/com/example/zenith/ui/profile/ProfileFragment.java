package com.example.zenith.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.zenith.CountryListActivity;
import com.example.zenith.NewsPreferencesActivity;
import com.example.zenith.R;
import com.example.zenith.databinding.FragmentProfileBinding;
import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.profileBar);
        ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        bar.setTitle("Profile");

        MaterialButton pref = root.findViewById(R.id.filterButton);
        MaterialButton myCountries = root.findViewById(R.id.myCountries);
        pref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), NewsPreferencesActivity.class);
                startActivity(intent);
            }
        });

        myCountries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), CountryListActivity.class);
                intent.putExtra("bookmarked", true);
                startActivity(intent);
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
