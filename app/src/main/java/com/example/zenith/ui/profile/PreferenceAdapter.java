package com.example.zenith.ui.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zenith.R;

import java.util.Locale;

public class PreferenceAdapter extends RecyclerView.Adapter<PreferenceAdapter.ViewHolder> {
    public String[] newsCategories;

    public Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView text;
        private Context context;
        private String category;

        private final ImageView image;
        private final CardView background;
        private final ImageView bookmark;
        private String[] savedPreferences;
        private Boolean saved = false;

        private int[] drawables = {R.drawable.news_business, R.drawable.news_entertainment, R.drawable.news_general,
                                    R.drawable.news_health, R.drawable.news_science, R.drawable.news_sports,
                                    R.drawable.news_technology, R.drawable.news_political};

        private int[] colors = {R.color.blue, R.color.earth, R.color.white, R.color.red, R.color.green,
                                R.color.teal_200, R.color.grey, R.color.purple_200};

        public boolean getSavedState() {
            return saved;
        }

        public String getCategory() {
            return category;
        }

        @SuppressLint("ResourceAsColor")
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (saved) {
                        bookmark.setImageResource(R.drawable.bookmark_border);
                        saved = false;
                    } else {
                        bookmark.setImageResource(R.drawable.bookmark_filled);
                        saved = true;
                    }
                }
            });
            text = v.findViewById(R.id.preferenceName);
            image = v.findViewById(R.id.icon);
            background = v.findViewById(R.id.background);
            bookmark = v.findViewById(R.id.bookmark);

            context = v.getContext();
            savedPreferences = getSaved();
        }

        public TextView getTextView() {
            return text;
        }

        private String[] getSaved() {
            SharedPreferences pref = context.getSharedPreferences("news_preferences", Context.MODE_PRIVATE);
            return pref.getString("news_preferences", "").split("&");
        }

        public void setImageView(int position) {
            image.setImageResource(drawables[position]);
            background.setBackgroundColor(context.getResources().getColor(colors[position]));
            for (String s : savedPreferences) {
                if (s.equals(category)) {
                    bookmark.setImageResource(R.drawable.bookmark_filled);
                    saved = true;
                    break;
                }
            }
        }
    }

    public PreferenceAdapter(String[] categories, Context context) {
       newsCategories = categories;
       this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.preference_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String curr = newsCategories[position];
        viewHolder.text.setText(curr);
        viewHolder.category = curr;
        viewHolder.context = context;
        viewHolder.setImageView(position);
    }

    @Override
    public int getItemCount() {
        return newsCategories.length;
    }
}

