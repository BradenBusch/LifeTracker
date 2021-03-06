package com.example.lifetrackerplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

/**
 * This class handles the back-end work needed to change the page for the Tab Layout.
 */

// TODO add another tab for check-ins
public class TabHandler extends AppCompatActivity {

    private Button addEventBtn;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem dashTab, viewTrackTab, addTrackTab;
    public PageAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        // Make the keyboard not move and views on any of the TabLayout Fragments
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        // Hooks
        tabLayout = findViewById(R.id.dashboard_tablayout_tabs);
        dashTab = findViewById(R.id.dashbaord_tablayout_tab1);
        viewTrackTab = findViewById(R.id.dashbaord_tablayout_tab2);
        addTrackTab = findViewById(R.id.dashbaord_tablayout_tab3);
        viewPager = findViewById(R.id.viewpager);

        pagerAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    pagerAdapter.notifyDataSetChanged();
                }
                else if (tab.getPosition() == 1) {
                    pagerAdapter.notifyDataSetChanged();
                }
                else if (tab.getPosition() == 2) {
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}
