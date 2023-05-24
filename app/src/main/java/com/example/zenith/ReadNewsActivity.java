package com.example.zenith;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.zenith.databinding.ActivityReadHistoryBinding;
import com.example.zenith.databinding.ActivityReadNewsBinding;
import com.squareup.picasso.Picasso;

public class ReadNewsActivity extends AppCompatActivity {
    private ActivityReadNewsBinding binding;

    private FeedArticle article;

    private String link;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReadNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        actionBar.setElevation(0);

        Bundle extra = getIntent().getExtras();

        link = extra.getString("link");
        binding.newsTitle.setText(extra.getString("title"));
        binding.newsSource.setText("News Source: " + extra.getString("source"));

        String summary = extra.getString("summary");
        if (summary != null) {
            binding.textView.setVisibility(View.VISIBLE);
            binding.newsSummary.setVisibility(View.VISIBLE);
            binding.newsSummary.setText(summary.trim());
        }

        String image = extra.getString("image");
        if (image!=null) {
            binding.newsImage.setVisibility(View.VISIBLE);
            Picasso.get().load(image).into(binding.newsImage);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read_news, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_link:
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(link));
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
