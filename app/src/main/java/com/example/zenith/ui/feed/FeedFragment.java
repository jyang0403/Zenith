package com.example.zenith.ui.feed;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.zenith.AppDatabase;
import com.example.zenith.Country;
import com.example.zenith.FeedAdapter;
import com.example.zenith.FeedArticle;
import com.example.zenith.ZenithAPIHelper;
import com.example.zenith.databinding.FragmentFeedBinding;

import java.util.ArrayList;

public class FeedFragment extends Fragment {
    private FragmentFeedBinding binding;

    private int page = 1;
    private int count = 0;

    private ArrayList<FeedArticle> feedArticleArrayList;
    private RecyclerView recyclerview;
    private FeedAdapter adapter;
    private ProgressBar loading;
    private NestedScrollView nestedSV;
    private SwipeRefreshLayout swipeRefresh;
    private SharedPreferences preferences;
    private String prefString;
    private ArrayList<Country> savedCountries = new ArrayList<>();

    private TextView error;

    ZenithAPIHelper apiHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFeedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ((AppCompatActivity)getActivity()).setSupportActionBar(binding.feedBar);
        ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        bar.setTitle("Feed");

        preferences = getActivity().getSharedPreferences("news_preferences", Context.MODE_PRIVATE);

        feedArticleArrayList = new ArrayList();

        adapter = new FeedAdapter(getContext(), feedArticleArrayList);
        recyclerview = binding.feed;
        recyclerview.setAdapter(adapter);
        loading = binding.loading;
        nestedSV = binding.idNestedSV;

        error = binding.errorMsg;


        prefString = preferences.getString("news_preferences", "");

        apiHelper = new ZenithAPIHelper();

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(manager);

        new DataLoader().execute(page);


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

        swipeRefresh = binding.swipeRefresh;
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                count = 0;
                feedArticleArrayList = new ArrayList();
                prefString = preferences.getString("news_preferences", "");
                adapter = new FeedAdapter(getContext(), feedArticleArrayList);
                recyclerview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                getData(page);
                swipeRefresh.setRefreshing(false);
                error.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                new DataLoader().execute(page);
            }
        });


        return root;
    }

    private void getData(int page) {
        error.setVisibility(View.GONE);
        if (prefString.equals("") && savedCountries.size() == 0) {
            loadNoPreferences(page);
        } else if (savedCountries.size() == 0) {
            loadNoSavedCountries(page);
        } else if (prefString.equals("")) {
            loadNoSavedPreferences(page);
        } else {
            loadBothPreferences(page);
        }
    }

    private void loadBothPreferences(int page) {
        String[] prefArray = prefString.split("&");
        ArrayList<FeedArticle> newArticles = new ArrayList();
        int numArticles = (savedCountries.size() > 5) ? savedCountries.size() / 3 : savedCountries.size();
        for (int i = 0; i < numArticles; i++) {
            // choose random country/topic
            int randomTopic = (int) Math.floor(Math.random() * prefArray.length);
            int randomCountry = (int) Math.floor(Math.random() * savedCountries.size());
            int currentI = i;
            int finalNumArticles = numArticles;
            if (prefArray[randomTopic] == "") {
                continue;
            }
            apiHelper.getArticles(apiHelper.getCountryCode(savedCountries.get(randomCountry).name), null, prefArray[randomTopic], 4, page, new ZenithAPIHelper.NewsApiCallback() {
                @Override
                public void getCountryNews(ArrayList<FeedArticle> articles) {
                    if (articles == null || (articles.size() == 0 && newArticles.size() == 0 && currentI == finalNumArticles - 1)) {
                        error.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.INVISIBLE);
                        return;
                    }
                    for (int i = 0; i < articles.size(); i++) {
                        newArticles.add(articles.get(i));
                    }
                    // scramble the articles so there is more randomness
                    if (currentI == finalNumArticles - 1) {
                        int size = newArticles.size();
                        while (size > 0) {
                            int random = (int) Math.floor(Math.random() * size);
                            FeedArticle add = newArticles.remove(random);
                            feedArticleArrayList.add(add);
                            adapter.notifyItemChanged(feedArticleArrayList.size() - 1);
                            size--;
                        }
                        recyclerview.setVisibility(View.VISIBLE);
                        recyclerview.setAdapter(adapter);
                    }
                }
            });
        }
    }

    private void loadNoSavedPreferences(int page) {
        ArrayList<FeedArticle> newArticles = new ArrayList();
        int numArticles = (savedCountries.size() > 5) ? savedCountries.size() / 3 : savedCountries.size();
        for (int i = 0; i < numArticles; i++) {
            int currentI = i;
            int randomCountry = (int) Math.floor(Math.random() * savedCountries.size());
            apiHelper.getArticles(apiHelper.getCountryCode(savedCountries.get(randomCountry).name), null, "general", 4, page, new ZenithAPIHelper.NewsApiCallback() {
                @Override
                public void getCountryNews(ArrayList<FeedArticle> articles) {
                    if (articles == null || (articles.size() == 0 && newArticles.size() == 0 && currentI == numArticles - 1)) {
                        error.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.INVISIBLE);
                        return;
                    }
                    for (int i = 0; i < articles.size(); i++) {
                        newArticles.add(articles.get(i));
                    }
                    // scramble the articles so there is more randomness
                    if (currentI == numArticles - 1) {
                        int size = newArticles.size();
                        while (size > 0) {
                            int random = (int) Math.floor(Math.random() * size);
                            FeedArticle add = newArticles.remove(random);
                            feedArticleArrayList.add(add);
                            adapter.notifyItemChanged(feedArticleArrayList.size() - 1);
                            size--;
                        }
                        recyclerview.setVisibility(View.VISIBLE);
                        recyclerview.setAdapter(adapter);
                    }
                }
            });
        }
    }

    private void loadNoSavedCountries(int page) {
        String[] prefArray = prefString.split("&");
        ArrayList<FeedArticle> newArticles = new ArrayList();
        int numArticles = (prefArray.length > 4) ? prefArray.length / 2 : prefArray.length;
        for (int i = 0; i < numArticles; i++) {
            int currentI = i;
            int randomTopic = (int) Math.floor(Math.random() * prefArray.length);
            if (!prefArray[randomTopic].equals("")) {
                apiHelper.getArticles(null, null, prefArray[randomTopic], 4, page, new ZenithAPIHelper.NewsApiCallback() {
                    @Override
                    public void getCountryNews(ArrayList<FeedArticle> articles) {
                        if (articles == null || (articles.size() == 0 && newArticles.size() == 0 && currentI == numArticles - 1)) {
                            error.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.INVISIBLE);
                            return;
                        }
                        for (int i = 0; i < articles.size(); i++) {
                            newArticles.add(articles.get(i));
                        }
                        // scramble the articles so there is more randomness
                        if (currentI == numArticles - 1) {
                            int size = newArticles.size();
                            while (size > 0) {
                                int random = (int) Math.floor(Math.random() * size);
                                FeedArticle add = newArticles.remove(random);
                                feedArticleArrayList.add(add);
                                adapter.notifyItemChanged(feedArticleArrayList.size() - 1);
                                size--;
                            }
                            recyclerview.setVisibility(View.VISIBLE);
                            recyclerview.setAdapter(adapter);
                        }
                    }
                });
            }
        }
    }

    private void loadNoPreferences(int page) {
        apiHelper.getArticles(null, null, "general", 5, page, new ZenithAPIHelper.NewsApiCallback() {
            @Override
            public void getCountryNews(ArrayList<FeedArticle> articles) {
                if (articles == null || articles.size() == 0) {
                    error.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                    return;
                }
                for (int i = 0; i < articles.size(); i++) {
                    feedArticleArrayList.add(articles.get(i));
                    adapter.notifyItemChanged(feedArticleArrayList.size() - 1);
                }
                recyclerview.setVisibility(View.VISIBLE);
                recyclerview.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class DataLoader extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            final AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, "Countries").fallbackToDestructiveMigration().build();
            savedCountries = (ArrayList<Country>) db.countryDao().getFavorites(1);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            getData(page);
        }
    }

}
