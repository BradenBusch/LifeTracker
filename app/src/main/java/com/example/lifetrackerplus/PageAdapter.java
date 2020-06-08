package com.example.lifetrackerplus;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    private int numTabs;

    // Constructor for each TabLayout
    public PageAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
    }

    @Override
    // TODO fix this code once the classes (fragments) are implemented. Using ViewTracks is just filler
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new ViewTracks();
            case 1: return new ViewTracks();
            case 2: return new AddTrackTab();
            default: return new ViewTracks();
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}