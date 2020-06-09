package com.example.lifetrackerplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * TODO implement a popup (allow disabling) telling the user that this is separate from "Check-In", and that this is
 *  for one single Track-Able they just want to make an entry for. Use CheckIn to be prompted for all TrackAbles.
 */

/**
 * This Activity handles a user entering a single check-in, and is accessible from the ViewTracks
 * page. This is separate and isolated from the Check-In button (which will prompt the user for all
 * TrackAbles)
 */
public class SingleTrackEntry extends AppCompatActivity {

    ListView listView;
    TrackEntryListAdapter adapter;
    Button confirmBtn;
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_track_entry);

        // Hooks
        listView = findViewById(R.id.singleentry_listview_attList);
        confirmBtn = findViewById(R.id.singleentry_btn_confirmBtn);

        // Get the name of the Track-Able that is getting an entry.
        Intent intent = getIntent();
        String entryTrackName = intent.getStringExtra("Name");

        // Load the HashMap
        HashMap<String, ArrayList<String>> tracks = readTrackableFile();
        // Get the ArrayList of attributes from the HashMap
        ArrayList<String> attributes = tracks.get(entryTrackName);
        // Get & set adapter
        adapter = new TrackEntryListAdapter(attributes, this);
        listView.setAdapter(adapter);

        setOnClicks(entryTrackName);
    }

    /*
     * Set any necessary click buttons / items
     */
    public void setOnClicks(final String entryTrackName) {
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> answers = getUsersEntries();
                int entryNum = getSharedPreference(entryTrackName);
                String fileEntryName = entryTrackName + entryNum + ".txt";


            }
        });
    }

    /*
     * Increment the counter for the directory getting an entry. This is used for naming of the .txt
     * files being saved.
     */
    public void incrementSharedPreferences(String directoryName) {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        int value = getPreferences(MODE_PRIVATE).getInt(directoryName, 0);
        value++;
        editor.putInt(directoryName, value).apply();
    }

    /*
     * Return the counter value for directoryName
     */
    public int getSharedPreference(String directoryName) {
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, 0);
        return preferences.getInt(directoryName, 0);
    }

    /*
     * Read the HashMap file and return the HashMap stored in it.
     * Returns: The app-wide HashMap
     */
    public HashMap<String, ArrayList<String>> readTrackableFile() {
        String path = this.getFilesDir() + "/trackablesdir/" + "trackables.txt";
        File file = new File(path);
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            HashMap<String, ArrayList<String>> map = (HashMap) ois.readObject();
            ois.close();
            return map;
        }
        catch (FileNotFoundException x) {
            x.printStackTrace();
            HashMap<String, ArrayList<String>> tracks = new HashMap<>();
            return tracks;
        }
        catch (IOException f) {
            f.printStackTrace();
            HashMap<String, ArrayList<String>> tracks = new HashMap<>();
            return tracks;
        }
        catch (ClassNotFoundException c){
            c.printStackTrace();
            HashMap<String, ArrayList<String>> tracks = new HashMap<>();
            return tracks;
        }
    }

    /*
     * Called when confirm is clicked. This will put the answers to each attribute in an ArrayList
     */
    public ArrayList<String> getUsersEntries() {
        ArrayList<String> ret = new ArrayList<>();
        for (int i = 0; i < listView.getCount(); i++) {
            View v = listView.getChildAt(i);
            EditText et = v.findViewById(R.id.singleentry_edittext_userEntry);
            ret.add(et.getText().toString());
        }
        return ret;
    }
}
