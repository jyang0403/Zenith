package com.example.zenith.ui.country;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zenith.R;
import com.example.zenith.ui.country.historyCountry.ReadHistoryActivity;

import java.util.LinkedHashMap;

public class HistoryCultureAdapter extends RecyclerView.Adapter<HistoryCultureAdapter.ViewHolder> {

    LinkedHashMap<String, String> sections;
    public Context context;
    public String [] keys;
    public int color;
    public String country;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView text;
        private String content;
        private String title;
        private Context context;
        private final CardView card;
        private String country;


        @SuppressLint("ResourceAsColor")
        public ViewHolder(View v) {
            super(v);

            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // do something when button clicked
                    Intent intent= new Intent(context, ReadHistoryActivity.class);
                    intent.putExtra("title",title);
                    intent.putExtra("content", content);
                    intent.putExtra("country", country);
                    context.startActivity(intent);

                }
            });
            text = v.findViewById(R.id.textView7);
            card = v.findViewById(R.id.news_frame);

        }
    }

    public HistoryCultureAdapter(LinkedHashMap<String, String> sec, int color, String country, Context context) {
        sections = sec;
        this.context = context;
        this.color = color;
        keys = sections.keySet().toArray(new String[0]);
        this.country = country;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.country_button_item, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String curr = keys[position];
        String [] currArr = curr.split("\\(");
        if(currArr.length == 1) currArr = curr.split(":");
        viewHolder.text.setText(currArr[0].toUpperCase());
        viewHolder.content = sections.get(curr);
        viewHolder.title = curr;
        viewHolder.context = context;
        viewHolder.country = country;
        viewHolder.card.setCardBackgroundColor(ContextCompat.getColor(context, color));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return sections.size();
    }
}

