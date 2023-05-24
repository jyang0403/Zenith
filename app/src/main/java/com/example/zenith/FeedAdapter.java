package com.example.zenith;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zenith.ui.country.historyCountry.ReadHistoryActivity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FeedArticle> articleArrayList;
    private int[] colors = {R.color.blue, R.color.earth, R.color.white, R.color.red, R.color.green,
            R.color.teal_200, R.color.grey, R.color.purple_200};


    public FeedAdapter(Context context, ArrayList<FeedArticle> articleArrayList) {
        this.context = context;
        this.articleArrayList = articleArrayList;
    }

    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.ViewHolder holder, int position) {
        FeedArticle article = articleArrayList.get(position);

        holder.article = article;
        holder.articleTitle.setText(article.title);
        holder.articleSource.setText(article.source);
        if (article.country != null) {
            holder.country.setText(article.country.toUpperCase(Locale.ROOT));
        }
        holder.topic.setText(article.category.toUpperCase());

        Picasso.get().load(article.image).placeholder(ContextCompat.getDrawable(context, R.drawable.news_general)).into(holder.articleImage);
        if (article.category != "General") {
            holder.topic.setBackground(findIndex(article.category));

        }

    }

    private Drawable findIndex(String category) {
        switch(category) {
            case "Business":
                return context.getResources().getDrawable(colors[0]);
            case "Entertainment":
                return context.getResources().getDrawable(colors[1]);
            case "Health":
                return context.getResources().getDrawable(colors[3]);
            case "Science":
                return context.getResources().getDrawable(colors[4]);
            case "Sports":
                return context.getResources().getDrawable(colors[5]);
            case "Technology":
                return context.getResources().getDrawable(colors[6]);
            case "Political":
                return context.getResources().getDrawable(colors[7]);
        }
        return null;
    }


    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView articleTitle;
        private final TextView articleSource;
        private final TextView topic;
        private final TextView country;
        private final ImageView articleImage;
        private FeedArticle article;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent= new Intent(context, ReadNewsActivity.class);
                    intent.putExtra("category", article.category);
                    intent.putExtra("title", article.title);
                    intent.putExtra("country", article.country);
                    intent.putExtra("summary", article.text);
                    intent.putExtra("image", article.image);
                    intent.putExtra("link", article.link);
                    intent.putExtra("source", article.source);
                    context.startActivity(intent);
                }
            });

            articleTitle = itemView.findViewById(R.id.articleTitle);
            articleSource = itemView.findViewById(R.id.articleSource);
            articleImage = itemView.findViewById(R.id.articleImage);
            topic = itemView.findViewById(R.id.topic);
            country = itemView.findViewById(R.id.country);

        }
    }
}