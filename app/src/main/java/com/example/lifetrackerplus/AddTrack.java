package com.example.lifetrackerplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

TODO
 - Make a directory for each trackable, store the information for their answers in there. (This is bad for aggregating the data)
 - Could store the counter for this in shared preferences
 */

public class AddTrack extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton;
    EditText addAttributeEditText, nameEditText;
    Button addAttributeBtn, cancelBtn, confirmBtn;
    View addAttributeBarSep;
    TextView addedAttributesLabel;
    ListView listView;
    ArrayList<String> attributeList;
    AddTrackListAdapter adapter;
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        // Hooks
        nameEditText = findViewById(R.id.addtrack_edittext_nameEntry);
        radioGroup = findViewById(R.id.addtrack_radiogroup_yesNo);
        addAttributeEditText = findViewById(R.id.addtrack_edittext_addValText);
        addAttributeBtn = findViewById(R.id.addtrack_btn_addVal);
        addAttributeBarSep = findViewById(R.id.addtrack_view_specBar);
        addedAttributesLabel = findViewById(R.id.addtrack_textview_addedAttributesLabel);
        listView = findViewById(R.id.addtrack_listview_addedVals);
        cancelBtn = findViewById(R.id.addtrack_btn_cancel);
        confirmBtn = findViewById(R.id.addtrack_btn_confirm);

        // Set the ListView adapter
        attributeList = new ArrayList<>();
        adapter = new AddTrackListAdapter(attributeList, this);
        listView.setAdapter(adapter);

        // Get the persistent HashMap
        HashMap<String, ArrayList<String>> tracks = readTrackableFile();

        // On Click Methods
        addAttributeBtnClick();
        confirmAndCancelClick(tracks);

        // Make the keyboard not push the cancel and confirm button up
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    /*
     * Handles the clicking of the radio button on this page. When a radio button is clicked, this
     * method will either hide or show the EditText and Button.
     */
    public void radioBtnShowEditText(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        if (radioButton.getText().equals("Yes")) {
            addAttributeEditText.setVisibility(View.VISIBLE);
            addAttributeBtn.setVisibility(View.VISIBLE);
            addAttributeBarSep.setVisibility(View.VISIBLE);
            addedAttributesLabel.setVisibility(View.VISIBLE);
        }
        else {
            addAttributeEditText.setVisibility(View.INVISIBLE);
            addAttributeBtn.setVisibility(View.INVISIBLE);
            addAttributeBarSep.setVisibility(View.INVISIBLE);
            addedAttributesLabel.setVisibility(View.INVISIBLE);
        }
    }

    // Handle the 'add' button being clicked. This will add the users value to their attribute list.
    public void addAttributeBtnClick() {
        addAttributeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredAttribute = addAttributeEditText.getText().toString();
                // Check if this attribute name is already in the list
                if (attributeList.contains(enteredAttribute)) {
                    Toast.makeText(getApplicationContext(), "You already added this attribute!", Toast.LENGTH_SHORT).show();
                }
                else if (enteredAttribute.equals("")) {
                    Toast.makeText(getApplicationContext(), "You must enter some text", Toast.LENGTH_SHORT).show();
                }
                else {
                    attributeList.add(enteredAttribute);
                }
                adapter.notifyDataSetChanged();
                addAttributeEditText.getText().clear();
            }
        });
    }

    // Handle the cancel and confirm buttons being clicked
    public void confirmAndCancelClick(final HashMap<String, ArrayList<String>> tracks) {
        // Clear the information and go back a page
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // go back to the dashboard
            }
        });
        // Clicking the confirm button will write the information to internal storage and clear the fields
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the user entered an empty string for name
                String nameVal = nameEditText.getText().toString();
                if (nameVal.equals("")) {
                    Toast.makeText(getApplicationContext(), "You must enter a name for your trackable.", Toast.LENGTH_SHORT).show();
                    nameEditText.getText().clear();
                }
                // Check if this trackable is already in the hashmap
                else if (tracks.containsKey(nameVal)) {
                    Toast.makeText(getApplicationContext(), "You already are tracking that! Try another name.", Toast.LENGTH_SHORT).show();
                    nameEditText.getText().clear();
                }
                // Add the trackables value to the hashmap, create a directory for the check-ins
                else {
                    // Get all the values from the ListView
                    ArrayList<String> trackableList = new ArrayList<>();
                    for (int i = 0; i < adapter.getCount(); i++) {
                        trackableList.add(adapter.getItem(i));
                    }
                    tracks.put(nameVal, trackableList); // add the <name, trackableList> to HashMap
                    writeTrackableFile(tracks); // update the HashMap in the file
                    createTrackableDirectory(nameVal); // create a directory of name nameVal to store check-ins
                }
            }
        });
    }

    // Create the directory for the Users entries to be stored. This method will also create the
    // shared preferences for a counter variable.
    public void createTrackableDirectory(String directoryName) {
        File file = new File(getApplicationContext().getFilesDir(), directoryName);
        if (!file.exists()) {
            file.mkdir();
        }
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(directoryName, 1);
        editor.commit();
    }

    // TODO move this to where it must be moved
    public void incrementSharedPreferences(String directoryName) {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        int value = getPreferences(MODE_PRIVATE).getInt(directoryName, 0);
        value++;
        getPreferences(MODE_PRIVATE).edit().putInt(directoryName, value);
    }

    // Write the HashMap to the file
    public void writeTrackableFile(HashMap<String, ArrayList<String>> tracks) {
        File file = new File(getApplicationContext().getFilesDir(), "trackablesdir");
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

    // Read the HashMap file and return the HashMap stored in it.
    public HashMap<String, ArrayList<String>> readTrackableFile() {
        String path = getApplicationContext().getFilesDir() + "/trackablesdir/" + "trackables.txt";
        File file = new File(path);
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            HashMap<String, ArrayList<String>> map = (HashMap) ois.readObject();
            ois.close();
            printHashMap(map);
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

    // Helper method to output the contents of the HashMap.
    public void printHashMap(HashMap<String, ArrayList<String>> map) {
        for (HashMap.Entry<String, ArrayList<String>> entry : map.entrySet()) {
            Log.i("map", entry.getKey() + ": " + entry.getValue());
        }
    }
}
