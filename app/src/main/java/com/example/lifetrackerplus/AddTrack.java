package com.example.lifetrackerplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/*
 * This class handles the form creation, validation, writing, etc. for a track-able item.
 */
/*
TODO
 - Make a first pass form for creating a task.
 - Add this data to internal storage. Find a way to write to the file for each trackable without stepping over the other (probs just a file for each trackable)
     -> Check if the filename exists before creating
 - Write the users information for each trackable to a file in internal memory (one file for each trackable)
 - I'm not sure how entering the trackable information daily will go yet.
 */
public class AddTrack extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        //Hooks
        radioGroup = findViewById(R.id.addtrack_radiogroup_yesNo);
    }

    public void checkAddTrackRadioBtn(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(this, "Selected Button " + radioButton.getText(), Toast.LENGTH_SHORT).show();
    }
}
