package com.example.lifetrackerplus;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class that handles the "TabHandler" Fragment. This screen will contain some information like
 * total number of entries
 */
public class DashboardTab extends Fragment {

    private HashMap<String, ArrayList<String>> hashMap;
    private int numTrackables, numEntries;
    private TextView numTrackablesHeader, numEntriesHeader;
    private Button addTrackBtn, viewEntriesBtn;

    public DashboardTab() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView(); // get the current Fragment View

        // Hooks
        numEntriesHeader = v.findViewById(R.id.dashboard_textview_numentries);
        numTrackablesHeader = v.findViewById(R.id.dashboard_textview_numtracks);
        addTrackBtn = v.findViewById(R.id.dashboard_btn_addTaskBtn);
        viewEntriesBtn = v.findViewById(R.id.dashboard_btn_addEntryBtn);

        // Get the universal HashMap
        hashMap = readTrackableFile(v);
        // Get size of the HashMap
        numTrackables = hashMap.size();

        numEntries = getNumJournalEntries(hashMap);

        // Change placeholders to real values
        numEntriesHeader.setText(numEntries + "");
        numTrackablesHeader.setText(numTrackables + "");
        initButtons();
    }

    /*
     * Initialize and apply listeners to the buttons
     */
    public void initButtons() {
        addTrackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        });
        viewEntriesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        });
    }

    /*
     * Return the total number of journal entries in the system
     */
    // FIXME this is not calculating the right number at all lol
    public int getNumJournalEntries(HashMap<String, ArrayList<String>> map) {
        int count = 0;
        for (Map.Entry<String, ArrayList<String>> set : map.entrySet()) {
            count += set.getValue().size();
        }
        return count;
    }

    /*
     * Read the internal HashMap into a HashMap
     */
    public HashMap<String, ArrayList<String>> readTrackableFile(View v) {
        String path = v.getContext().getFilesDir() + "/trackablesdir/" + "trackables.txt";
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
}
