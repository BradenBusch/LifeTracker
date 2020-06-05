package com.example.lifetrackerplus;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Fragment class that shows the users Track-Ables. Allows inspection, check-ins, and deletion of
 * each Track-able
 */
public class ViewTracks extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public ViewTracks() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_tracks, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView(); // get the current Fragment View

        // Get the internally stored HashMap which contains all the Trackable names & attributes
        HashMap<String, ArrayList<String>> userTracks = readTrackableFile(v);

        // Populate the viewTrackListItemArrayList with the ViewTrackListItems
        ArrayList<ViewTrackListItem> viewTrackListItemArrayList = populateCardView(userTracks);

        // Build Recycler View, Adapter, and Layout Manager.
        recyclerView = v.findViewById(R.id.viewtracks_recview_recview);
        layoutManager = new LinearLayoutManager(v.getContext());
        adapter = new ViewTrackListAdapter(viewTrackListItemArrayList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public ArrayList<ViewTrackListItem> populateCardView(HashMap<String, ArrayList<String>> map) {
        // TODO add in the symbols here and in ViewTrackListItem
        ArrayList<ViewTrackListItem> list = new ArrayList<>();
        for (HashMap.Entry<String, ArrayList<String>> entry : map.entrySet()) {
            String name = entry.getKey();
            //ArrayList<String> attributes = entry.getValue();
            ViewTrackListItem vtli = new ViewTrackListItem(name);
            list.add(vtli);
        }
        return list;
    }

    // Return the stored internal HashMap
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
