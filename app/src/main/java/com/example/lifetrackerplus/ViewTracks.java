package com.example.lifetrackerplus;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Fragment class that shows the users Track-Ables. Allows inspection, check-ins, and deletion of
 * each Track-able. This will be the main view for seeing a list of all the users Track-Ables.
 */
public class ViewTracks extends Fragment {

    private ArrayList<ViewTrackListItem> vtlist;
    private RecyclerView recyclerView;
    private ViewTrackListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private HashMap<String, ArrayList<String>> map;

    public ViewTracks() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Check if the Trackable List is empty
        return inflater.inflate(R.layout.fragment_view_tracks, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView(); // get the current Fragment View

        // Get the internally stored HashMap which contains all the Trackable names & attributes
        HashMap<String, ArrayList<String>> userTracks = readTrackableFile(v);
        map = userTracks;
        // Populate the viewTrackListItemArrayList with the ViewTrackListItems
        ArrayList<ViewTrackListItem> viewTrackListItemArrayList = populateCardView(userTracks);

        // Build the recycler view
        buildRecyclerView(v, viewTrackListItemArrayList);
        //trackExistToast(userTracks, v);
        //onHiddenChanged(mVisible);
    }

    /*
     * Build Recycler View, Adapter, and Layout Manager.
     */
    public void buildRecyclerView(final View v, final ArrayList<ViewTrackListItem> list) {
        recyclerView = v.findViewById(R.id.viewtracks_recview_recview);
        layoutManager = new LinearLayoutManager(v.getContext());
        adapter = new ViewTrackListAdapter(list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // Implement all the interface methods for clicking on the recycler view
        adapter.setOnItemClickListener(new ViewTrackListAdapter.OnItemClickListener() {
            @Override
            // User taps on the Card Itself
            public void onItemClick(int position) {
                list.get(position);
            }

            @Override
            // User taps delete.
            public void onDeleteClick(int position) {
                removeItem(list, position);
            }

            @Override
            // User taps more info button
            public void onInfoClick(int position) {
                // This click will only bring up the info for one particular trackable, Dashboard
                // will contain the calendar with each trackable on it
                Intent intent = new Intent(getActivity(), SingleTrackInfo.class);
                intent.putExtra("Name2", list.get(position).getItemName());
                startActivity(intent);
            }

            @Override
            // User taps the add button
            public void onAddClick(int position) {
                HashMap<String, ArrayList<String>> map = readTrackableFile(getView());
                // Pass the name of the clicked TrackAble to the next activity
                Intent intent = new Intent(getActivity(), SingleTrackEntry.class);
                intent.putExtra("Name", list.get(position).getItemName());
                startActivity(intent);
            }
        });
    }

    /*
     * Remove the item from the RecyclerView. This will be done when the trash-can is tapped.
     * This method will prompt the user if they are sure they want to delete the trackable. If yes is tapped,
     * the method will also remove the value from the HashMap and internal storage.
     */
    public void removeItem(final ArrayList<ViewTrackListItem> list, final int position) {
        final ViewTrackListItem delName = list.get(position); // The object to be (potentially) deleted
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this Track-Able? You'll lose all saved information attached to it.");

        // Yes a.k.a. delete the Trackable click
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            // Handle deletion of the trackable
            public void onClick(DialogInterface dialogInterface, int i) {
                // Remove the value from the screen
                list.remove(position);
                adapter.notifyDataSetChanged();
                // Get the internal HashMap
                HashMap<String, ArrayList<String>> storedList = readTrackableFile(getView());
                // Remove the value from the internal HashMap
                storedList.remove(delName.getItemName());
                // Write the HashMap back
                writeTrackableFile(storedList);
                // Delete the created directory TODO test this!
                deleteTrackableDirectory(delName.getItemName());
            }
        });
        // Handle "No" tap (which is nothing)
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // Helper method that simply prints the contents of the HashMap
    public void printHashMap(HashMap<String, ArrayList<String>> map) {
        for (HashMap.Entry<String, ArrayList<String>> entry : map.entrySet()) {
            Log.i("map", entry.getKey() + ": " + entry.getValue());
        }
        Log.i("map", "------------");
    }

    /*
     * Populate the RecyclerView with the information from the HashMap(just the name).
     * Returns: A new ArrayList<ViewTrackListItem> with each trackable.
     */
    public ArrayList<ViewTrackListItem> populateCardView(HashMap<String, ArrayList<String>> map) {
        ArrayList<ViewTrackListItem> list = new ArrayList<>();
        for (HashMap.Entry<String, ArrayList<String>> entry : map.entrySet()) {
            String name = entry.getKey();
            //ArrayList<String> attributes = entry.getValue();
            ViewTrackListItem vtli = new ViewTrackListItem(name);
            list.add(vtli);
        }
        return list;
    }

    /*
     * Delete all files in a directory, and the directory itself.
     */
    public void deleteTrackableDirectory(String trackName) {
        File directory = getContext().getFilesDir();
        File trackFileDir = new File(directory, trackName);
        if (trackFileDir.isDirectory()) {
            String[] children = trackFileDir.list();
            for (int i = 0; i < children.length; i++) {
                new File(trackFileDir, children[i]).delete();
            }
        }
    }

    /*
     * TODO make this work
     *  Check if the HashMap is empty, meaning the user hasn't made any trackables yet. This will
     *  produce a toast message
     */
    public void trackExistToast(HashMap<String, ArrayList<String>> map, View v) {
        if (map.size() == 0) {
            Toast toast = Toast.makeText(v.getContext(), "You haven't added any trackables yet, they will show up here once you have.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER| Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }

    // Write the stored internal HashMap
    public void writeTrackableFile(HashMap<String, ArrayList<String>> tracks) {
        File file = new File(getContext().getFilesDir(), "trackablesdir");
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            File trackFile = new File(file, "trackables.txt");
            FileOutputStream fos = new FileOutputStream(trackFile, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(tracks);
            oos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
