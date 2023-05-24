package com.example.zenith.ui.country.newsCountry;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zenith.FeedAdapter;
import com.example.zenith.FeedArticle;
import com.example.zenith.R;
import com.example.zenith.ZenithAPIHelper;
//import com.example.zenith.databinding.ActivityDisplayCategoryNewsBinding;

import java.util.ArrayList;

public class DisplayCategoryNewsActivity extends AppCompatActivity {
    private int page = 1;
    private int count = 0;

    private ArrayList<FeedArticle> ArticleArrayList;
    private RecyclerView recyclerview;
    private FeedAdapter adapter;
    private ProgressBar loading;
    private NestedScrollView nestedSV;
    private TextView error;
    private ZenithAPIHelper apiHelper;
    private String category;
    private String country;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feed);

        Bundle extra = getIntent().getExtras();
        category = extra.getString("category");
        country = extra.getString("country");

        setSupportActionBar(findViewById(R.id.feed_bar));
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        actionBar.setTitle(category + " News");
        actionBar.setSubtitle(country);

        ArticleArrayList = new ArrayList();
        adapter = new FeedAdapter(this, ArticleArrayList);
        recyclerview = findViewById(R.id.feed);
        loading = findViewById(R.id.loading);
        nestedSV = findViewById(R.id.idNestedSV);

        error = findViewById(R.id.errorMsg);

        apiHelper = new ZenithAPIHelper();
        getData(page);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                count = 0;
                ArticleArrayList = new ArrayList();
                adapter = new FeedAdapter(getApplicationContext(), ArticleArrayList);
                recyclerview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                getData(page);
                swipeRefresh.setRefreshing(false);
                error.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
            }
        });

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    count++;
                    loading.setVisibility(View.VISIBLE);
                    if (count < 20) {
                        page++;
                        getData(page);
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData(int page) {
        category = category.substring(0, 1).toUpperCase() + category.substring(1).toLowerCase();
        apiHelper.getArticles(apiHelper.getCountryCode(country), null, category, 5, page, new ZenithAPIHelper.NewsApiCallback() {
            @Override
            public void getCountryNews(ArrayList<FeedArticle> articles) {
                if (articles == null) {
                    error.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                    return;
                }
                for (int i = 0; i < articles.size(); i++) {
                    ArticleArrayList.add(articles.get(i));
                    adapter.notifyItemChanged(ArticleArrayList.size() - 1);
                }

                recyclerview.setVisibility(View.VISIBLE);
                recyclerview.setAdapter(adapter);
                error.setVisibility(View.GONE);
            }
        });
    }
}