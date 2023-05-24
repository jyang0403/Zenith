package com.example.zenith.ui.explore;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zenith.CountryListActivity;
import com.example.zenith.R;

import java.util.List;

public class ExploreContinentAdapter extends RecyclerView.Adapter<ExploreContinentAdapter.ViewHolder>{

    private final List<String> continents;
    private final Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        private String continent;
        private Context context;

        private ImageView image;

        public ViewHolder(View v) {
            super(v);

            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context, CountryListActivity.class);
                    intent.putExtra("continent",continent);
                    context.startActivity(intent);
                }
            });
            text = v.findViewById(R.id.name_view);
            image = v.findViewById(R.id.flag_view);
        }
        public TextView getTextView() {
            return text;
        }
        public void setImageView() throws NoSuchFieldException, IllegalAccessException {
            image.setVisibility(View.VISIBLE);
            String idName = continent.toLowerCase().replace(' ', '_');
            int id = R.drawable.class.getField(idName).getInt(null);
            image.setImageResource(id);
        }
    }

    public ExploreContinentAdapter(List<String> continents, Context context) {
        this.continents = continents;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.explore_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String continent = continents.get(position);
        holder.context = context;
        holder.continent = continent;
        holder.getTextView().setText(continent);

        try {
            holder.setImageView();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return continents.size();
    }
}
