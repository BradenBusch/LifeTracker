package com.example.lifetrackerplus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that will show the user information related to just ONE trackable. Accessible from
 * "ViewTracks" by tapping on the more information icon. There will be a page similar to this that
 * will have a calendar and show all entries
 */

public class SingleTrackInfo extends AppCompatActivity {

    private String trackableName;
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private ArrayList<String> trackableEntryNames;
    private HashMap<String, ArrayList<String>> trackableEntryData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_track_info);

        // Hooks
        TextView header = findViewById(R.id.singleinfo_textview_header);
        listView = findViewById(R.id.singleinfo_layout_exlistview);

        // Get the name of the current Trackable being viewed and set the screen title from it
        Intent intent = getIntent();
        trackableName = intent.getStringExtra("Name2");
        header.setText(trackableName + " Entries");

        // Load the information from the files and set the adapter
        readTrackableEntries(trackableName); // get the information for each ExpandableView
        listAdapter = new SingleInfoExpandableListAdapter(this, trackableEntryNames, trackableEntryData);
        listView.setAdapter(listAdapter);
    }

    /*
     * Add the information from each entry to the ExpandableList.
     * This method initiates trackableEntryNames and trackableEntryData
     */
    public void readTrackableEntries(String dirName) {
        trackableEntryNames = new ArrayList<>();
        trackableEntryData = new HashMap<>();
        File dir = new File(getWindow().getContext().getFilesDir(), dirName);
        if (dir.isDirectory()) {
            File[] trackEntries = dir.listFiles(); // Get each file in this trackables directory (i.e. get each entry for selected trackable)
            int fileNum = 1;
            for (File trackEntry : trackEntries) { // Loop through each file
                try {
                    String expandableEntryName = trackEntry.getName();
                    ArrayList<String> ivdlTrackData = new ArrayList<>();
                    BufferedReader br = new BufferedReader(new FileReader(trackEntry));
                    int lineCount = 0;
                    String line = br.readLine();
                    while (line != null) {
                        // Sun Sep 13 21:20:18 CDT 2020
                        // Format the name of the expandable tab based
                        if (lineCount == 0) {
                            String[] data = line.split(" ");
                            expandableEntryName = "Entry " + fileNum + ": " + " " + data[0] + " " + data[1] + " " + data[2] + " " + data[5] + " " + data[3];
                        }
                        // Skip this entry in the file
                        else if (lineCount == 1) {

                        }
                        // Add the information to the HashMap
                        else {
                            ivdlTrackData.add(line);
                        }
                        lineCount++;
                        line = br.readLine();
                    }
                    fileNum++;
                    trackableEntryNames.add(expandableEntryName);
                    trackableEntryData.put(expandableEntryName, ivdlTrackData);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
