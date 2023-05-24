package com.example.zenith;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zenith.ui.profile.PreferenceAdapter;
import com.google.android.material.button.MaterialButton;

public class NewsPreferencesActivity extends AppCompatActivity {

    private String[] categories = {"Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology"};

    private RecyclerView recyclerview;
    private PreferenceAdapter adapter;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_preferences);

        preferences = getSharedPreferences("news_preferences", Context.MODE_PRIVATE);
        editor = preferences.edit();

        setTitle(getString(R.string.news_preferences));

        try {
            recyclerview = findViewById(R.id.recyclerview);
            adapter = new PreferenceAdapter(categories, getApplicationContext());

            LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
            recyclerview.setLayoutManager(manager);

            recyclerview.setAdapter(adapter);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        MaterialButton save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String savedPreferences = "";
                for (int x = recyclerview.getChildCount(), i = 0; i < x; ++i) {
                    PreferenceAdapter.ViewHolder holder = (PreferenceAdapter.ViewHolder) recyclerview.getChildViewHolder(recyclerview.getChildAt(i));
                    if (holder.getSavedState()) {
                        savedPreferences += holder.getCategory();
                        savedPreferences += "&";
                    }
                }
                editor.putString("news_preferences", savedPreferences);
                editor.apply();
                finish();
            }
        });

    }
}
