package com.example.lifetrackerplus;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Handle which Tab to open / create when it is slid onto
 */
public class PageAdapter extends FragmentPagerAdapter {

    private int numTabs;

    // Constructor for each TabLayout
    public PageAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new DashboardTab();
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
