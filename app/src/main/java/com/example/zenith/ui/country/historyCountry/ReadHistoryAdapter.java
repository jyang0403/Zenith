package com.example.zenith.ui.country.historyCountry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zenith.R;

import java.util.LinkedHashMap;

public class ReadHistoryAdapter extends RecyclerView.Adapter<ReadHistoryAdapter.ViewHolder> {

    LinkedHashMap<String, String> sections;

    public Context context;
    public String [] keys;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView content;
        private final TextView header;

        private final TextView subtitle;

        @SuppressLint("ResourceAsColor")
        public ViewHolder(View v) {
            super(v);

            header = v.findViewById(R.id.hist_section_head);
            content = v.findViewById(R.id.his_section_content);
            subtitle = v.findViewById(R.id.hits_section_subtitle);
        }
        public TextView getTextViewHeader() {
            header.setVisibility(View.VISIBLE);
            return header;
        }
        public TextView getTextViewContent() {
            return content;
        }
        public TextView getTextViewSubtitle() {
            subtitle.setVisibility(View.VISIBLE);
            return subtitle;
        }
    }

    public ReadHistoryAdapter(LinkedHashMap<String, String> sec) {
        sections = sec;
        keys = sections.keySet().toArray(new String[0]);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.history_item, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        String header = keys[position];

        if(!header.equals("")){
            String[] subtitleArr = header.replace(" (", "\n(").split("\n");
            if(subtitleArr.length == 1)
                viewHolder.getTextViewHeader().setText(header);
            else {
                viewHolder.getTextViewHeader().setText(subtitleArr[0]);
                viewHolder.getTextViewSubtitle().setText(subtitleArr[1]);
            }
        }

        viewHolder.getTextViewContent().setText(sections.get(header));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return sections.size();
    }
}


