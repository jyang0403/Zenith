package com.example.zenith.ui.country.historyCountry;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zenith.Country;
import com.example.zenith.R;
import com.example.zenith.ZenithAPIHelper;
import com.example.zenith.databinding.ActivityReadHistoryBinding;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.LinkedHashMap;


public class ReadHistoryActivity extends AppCompatActivity {
    private ActivityReadHistoryBinding binding;

    LinkedHashMap<String, String> sections;

    String country;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReadHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extra = getIntent().getExtras();
        String title = extra.getString("title");
        String content = extra.getString("content");
        country = extra.getString("country");
        String subtitle = "";

        ZenithAPIHelper api = new ZenithAPIHelper();
        code = api.getCountryCode(country).toUpperCase();

        String[] titles = title.split("\\(", 2);
        if(titles.length > 1) {
            title = titles[0];
            subtitle = titles[1].split("\\)")[0].trim();
        } else {
            titles = title.split(":", 2);
            if(titles.length > 1) {
                title = titles[0];
                subtitle = titles[1].trim();
            }
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(country);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        binding.histTitle.setText(capitalizeFirstChar(title));

        if(!subtitle.equals("")){
            binding.histSubtitle.setVisibility(View.VISIBLE);
            binding.histSubtitle.setText(capitalizeFirstChar(subtitle));
        }

        RecyclerView mRecyclerView = binding.readHistRecycler;
        getSections(content);

        ReadHistoryAdapter buttonAdapter = new ReadHistoryAdapter(sections);
        mRecyclerView.setAdapter(buttonAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.menu_flag);
        ImageView flag = (ImageView) alertMenuItem.getActionView();

        String flag_link = "https://flagsapi.com/"+code+"/shiny/64.png";
        Picasso.get().load(flag_link).into(flag);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_flag:
                return true;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String capitalizeFirstChar(String str) {
        String[] words = str.split("\\s");
        StringBuilder capitalizeWord= new StringBuilder();
        for(String w:words){
            if (w.equals("of") || w.equals("and")) {
                capitalizeWord.append(w).append(" ");
                continue;
            }
            String first = w.substring(0,1);
            String afterFirst = w.substring(1);
            capitalizeWord.append(first.toUpperCase()).append(afterFirst).append(" ");
        }

        return capitalizeWord.toString().trim();
    }

    public void getSections(String str) {
        sections = new LinkedHashMap<>();
        String[] sectionArr = str.split("\n==== ");
        for (int i=1; i < sectionArr.length; i++) {
            String[] section = sectionArr[i].split(" ====\n", 2);
            if (section.length == 1)
                sections.put("", section[0]);
            else
                sections.put(capitalizeFirstChar(section[0]), section[1].trim());
        }

        if(sections.size() == 0) sections.put("", str);
    }

}