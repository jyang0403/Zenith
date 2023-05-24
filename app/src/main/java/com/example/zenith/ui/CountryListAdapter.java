package com.example.zenith.ui;


import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static androidx.appcompat.content.res.AppCompatResources.getDrawable;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.zenith.AppDatabase;
import com.example.zenith.Country;
import com.example.zenith.CountryDao;
import com.example.zenith.HomePageActivity;
import com.example.zenith.MainActivity;
import com.example.zenith.R;
import com.example.zenith.ZenithAPIHelper;
import com.example.zenith.ui.country.CountryActivity;
import com.example.zenith.ui.country.homeCountry.HomeCountryFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.MyViewHolder> {

    Context context;
    List<Country> countryList;

    public CountryListAdapter(Context context, List<Country> countryList)
    {
        this.context = context;
        this.countryList = countryList;
    }

    @NonNull
    @Override
    public CountryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //This is where you inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.country_row, parent, false);
        return new CountryListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryListAdapter.MyViewHolder holder, int position) {
        //Assign values to the views created in country_row layout file
        // based on the position of the recycler view

        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "Countries").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        CountryDao dao = db.countryDao();

        holder.countryName.setText(countryList.get(position).name);

        if (dao.isBookmarked(holder.countryName.getText().toString()) == 0)
        {
            holder.bookmarkImage.setImageResource(R.drawable.bookmark_border);
        } else
        {
            holder.bookmarkImage.setImageResource(R.drawable.bookmark_filled);
        }

        ZenithAPIHelper api = new ZenithAPIHelper();
        String code = api.getCountryCode(countryList.get(position).name).toUpperCase();
        String flag_link = "https://flagsapi.com/"+code+"/shiny/64.png";
        Picasso.get().load(flag_link).placeholder(ContextCompat.getDrawable(context, R.drawable.news_general)).into(holder.flagImage);


        holder.countryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(context, CountryActivity.class);
                    intent.putExtra("country", holder.countryName.getText().toString());
                    context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });

        holder.flagImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CountryActivity.class);
                intent.putExtra("country", holder.countryName.getText().toString());
                context.startActivity(intent);
            }
        });

        holder.bookmarkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dao.isBookmarked(holder.countryName.getText().toString()) == 0)
                {
                    holder.bookmarkImage.setImageResource(R.drawable.bookmark_filled);
                    dao.updateFavorite(holder.countryName.getText().toString(), 1);
                } else
                {
                    holder.bookmarkImage.setImageResource(R.drawable.bookmark_border);
                    dao.updateFavorite(holder.countryName.getText().toString(), 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView flagImage;
        TextView countryName;
        ImageView bookmarkImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            flagImage = itemView.findViewById(R.id.CountryFlag);
            countryName = itemView.findViewById(R.id.CountryName);
            bookmarkImage = itemView.findViewById(R.id.CountryBookmarked);
        }
    }
}
