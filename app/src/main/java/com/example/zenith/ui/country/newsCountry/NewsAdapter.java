package com.example.zenith.ui.country.newsCountry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zenith.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    public String[] newsCategories;

    public Context context;

    public String countryName;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView text;
        private Context context;
        private String category;
        private String countryName;

        private final ImageView image;

        @SuppressLint("ResourceAsColor")
        public ViewHolder(View v) {
            super(v);

            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // do something when button clicked
                    Intent intent= new Intent(context, DisplayCategoryNewsActivity.class);
                    intent.putExtra("category",category);
                    intent.putExtra("country", countryName);
                    context.startActivity(intent);

                }
            });

            text = v.findViewById(R.id.textView7);
            image = v.findViewById(R.id.news_category_image);
            image.setVisibility(View.VISIBLE);
        }

        public TextView getTextView() {
            return text;
        }

        public void setImageView() throws NoSuchFieldException, IllegalAccessException {
            image.setVisibility(View.VISIBLE);
            String idName = "news_" + category.toLowerCase();
            int id = R.drawable.class.getField(idName).getInt(null);
            image.setImageResource(id);
        }
    }

    public NewsAdapter(String[] categories, Context context, String countryName) {
       newsCategories = categories;
       this.context = context;
       this.countryName = countryName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.country_button_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String curr = newsCategories[position];
        viewHolder.getTextView().setText(curr.toUpperCase());
        viewHolder.context = context;
        viewHolder.category = curr;
        viewHolder.countryName = countryName;

        try {
            viewHolder.setImageView();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return newsCategories.length;
    }
}

