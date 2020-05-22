package com.example.lifetrackerplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/*
 * This class handles the form creation, validation, writing, etc. for a track-able item.
 */
/*
TODO
 - Make a first pass form for creating a task.
 - Add this data to internal storage. Find a way to write to the file for each trackable without stepping over the other (probs just a file for each trackable)
     -> Check if the filename exists before creating
 -
 */
public class AddTrack extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);
    }
}
