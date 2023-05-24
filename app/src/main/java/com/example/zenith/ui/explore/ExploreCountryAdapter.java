package com.example.zenith.ui.explore;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zenith.Country;
import com.example.zenith.R;
import com.example.zenith.ZenithAPIHelper;
import com.example.zenith.ui.country.CountryActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ExploreCountryAdapter extends RecyclerView.Adapter<ExploreCountryAdapter.ViewHolder>{
    private final List<Country> countries;
    private final Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        private Country country;
        private Context context;

        private final ImageView flag;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // do something when button clicked
                    Intent intent= new Intent(context, CountryActivity.class);
                    intent.putExtra("country", country.name);
                    context.startActivity(intent);
                }
            });

            text = v.findViewById(R.id.name_view);
            flag = v.findViewById(R.id.flag_view);
        }
    }

    public ExploreCountryAdapter (List<Country> countries, Context context) {
        this.countries = countries;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.explore_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Country country = countries.get(position);
        holder.text.setText(country.name);
        holder.context = context;
        holder.country = country;

        ZenithAPIHelper api = new ZenithAPIHelper();
        String code = api.getCountryCode(country.name).toUpperCase();
        String flag_link = "https://flagsapi.com/"+code+"/shiny/64.png";
        Picasso.get().load(flag_link).placeholder(ContextCompat.getDrawable(context, R.drawable.news_general)).into(holder.flag);
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }
}
