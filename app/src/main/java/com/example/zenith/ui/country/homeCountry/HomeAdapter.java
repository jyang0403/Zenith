package com.example.zenith.ui.country.homeCountry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zenith.R;

import java.util.LinkedHashMap;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{
    LinkedHashMap<String, String> sections;
    public String [] keys;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView text1;
        private final TextView text2;

        public ViewHolder(View v) {
            super(v);
            text1 = v.findViewById(R.id.home_layout_1);
            text2 = v.findViewById(R.id.home_layout_2);
        }
    }

    public HomeAdapter(LinkedHashMap<String, String> sec) {
        sections = sec;
        keys = sections.keySet().toArray(new String[0]);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.home_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String curr = keys[position];
        viewHolder.text1.setText(curr);
        viewHolder.text2.setText(sections.get(curr));
    }

    @Override
    public int getItemCount() {
        return sections.size();
    }
}
