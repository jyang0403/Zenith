package com.example.zenith.ui.country;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.zenith.R;
import com.example.zenith.ui.country.culture.CultureCountryFragment;
import com.example.zenith.ui.country.historyCountry.HistoryCountryFragment;
import com.example.zenith.ui.country.homeCountry.HomeCountryFragment;
import com.example.zenith.ui.country.newsCountry.NewsCountryFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_home_fragment, R.string.tab_history_fragment, R.string.tab_culture_fragment, R.string.tab_news_fragment};
    private final Context mContext;

    private final String countryName;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String cName) {
        super(fm);
        mContext = context;
        countryName = cName;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment;

        Bundle args = new Bundle();
        args.putString("country", countryName);

        switch (position) {
            case 1:
                fragment = new HistoryCountryFragment();
                fragment.setArguments(args);
                break;
            case 2:
                fragment = new CultureCountryFragment();
                fragment.setArguments(args);
                break;
            case 3:
                fragment = new NewsCountryFragment();
                fragment.setArguments(args);
                break;
            default:
                fragment = new HomeCountryFragment();
                fragment.setArguments(args);
                break;
        }

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}