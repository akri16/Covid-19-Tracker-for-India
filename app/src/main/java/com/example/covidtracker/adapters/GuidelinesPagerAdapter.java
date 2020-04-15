package com.example.covidtracker.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.covidtracker.utils.DataUtils;
import com.example.covidtracker.view.HelpPagerFragment;
import com.example.covidtracker.view.ImageFragment;
import com.example.covidtracker.models.dataModels.Guideline;

public class GuidelinesPagerAdapter extends FragmentPagerAdapter {

    public static final int SIZE = DataUtils.GUIDELINES.size() + 1;

    public GuidelinesPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        if(position==0){
            return new ImageFragment();
        }
        return HelpPagerFragment.newInstance((Guideline) DataUtils.GUIDELINES.get(position-1));
    }

    @Override
    public int getCount() {
        return SIZE;
    }
}
