package com.example.zenith.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zenith.CountryListActivity;
import com.example.zenith.R;
import com.example.zenith.ui.country.CountryActivity;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class FilterListAdapter extends RecyclerView.Adapter<FilterListAdapter.MyViewHolder> {

    Context context;
    List<String> filterList;
    String continent;

    public FilterListAdapter(Context context, List<String> filterList, String continent)
    {
        this.context = context;
        this.filterList = filterList;
        this.continent = continent;
    }

    @NonNull
    @Override
    public FilterListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //This is where you inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.filter_button, parent, false);
        return new FilterListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterListAdapter.MyViewHolder holder, int position) {
        //Assign values to the views created in filter_button layout file
        // based on the position of the recycler view

        holder.filterButton.setText(filterList.get(position));

        if(filterList.get(position).equals(continent))
        {
            holder.filterButton.setBackgroundColor(ContextCompat.getColor(context, R.color.earth));
        } else
        {
            holder.filterButton.setBackgroundColor(ContextCompat.getColor(context, R.color.screen_background));
        }

        holder.filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof CountryListActivity)
                {
                    ((CountryListActivity)context).setupCountries(holder.filterButton.getText().toString());
                }

                holder.filterButton.setBackgroundColor(ContextCompat.getColor(context, R.color.earth));
                continent = holder.filterButton.getText().toString();

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public MaterialButton filterButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            filterButton = itemView.findViewById(R.id.filterButton);
        }
    }
}
