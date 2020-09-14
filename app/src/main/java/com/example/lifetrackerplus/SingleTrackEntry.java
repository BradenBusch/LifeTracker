package com.example.lifetrackerplus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;

/*
 * TODO
 *  [x] Implement a popup (allow disabling) telling the user that this is separate from "Check-In", and that this is
 *     for one single Track-Able they just want to make an entry for. Use CheckIn to be prompted for all TrackAbles.
 *  [] Finish the XML for the SingleEntry screen to make it look cleaner
 *  [x] Determine how writing to a file for a single entry will be done (as in the pattern)
 *   -> I think I should be putting the attribute values in the file too, then can use split or something for displaying them
 *  [x] Find way to handle dates for each file
 *  [x] Debug counter for each txt file
 *  [x] Handle getting the Date/Time for each file
 *  [] Implement a file parser for reading a file back
 *  [x] Make method for writing to the file
 *  [x] PRIORITY: Debug writing to the file
 *  [x] Debug: On AddTrack, not scrolling anymore.
 *  [x] Make the Title of the SingleEntry screen be the name of the track
 *  [x] Make the ListView get cleared after clicking confirm, and take the user back to the other screen
 *  [x] Add Radio Buttons to the XML view for Yes or No
 *  [] Make the TabHandler screen
 *  [x] Make the attributes be hidden when "No" is selected
 *  [x] Write Yes or No to the file that is created
 */

/**
 * This Activity handles a user entering a single check-in, and is accessible from the ViewTracks (from pressing the + button)
 * page. This is separate and isolated from the Check-In button (which will prompt the user for all TrackAbles)
 */
public class SingleTrackEntry extends AppCompatActivity {

    ListView listView;
    TrackEntryListAdapter adapter;
    Button confirmBtn;
    TextView header;
    RadioButton yesRadioBtn, noRadioBtn;
    ListView attributeList;
    boolean trackableDone = true; //this will be used for counting purposes for analytics

    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_track_entry);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        // Hooks
        listView = findViewById(R.id.singleentry_listview_attList);
        confirmBtn = findViewById(R.id.singleentry_btn_confirmBtn);
        header = findViewById(R.id.singleentry_textview_header);
        yesRadioBtn = findViewById(R.id.singleentry_radiobtn_yes);
        noRadioBtn = findViewById(R.id.singleentry_radiobtn_no);
        listView = findViewById(R.id.singleentry_listview_attList);

        // Get the name of the Track-Able that is getting an entry.
        Intent intent = getIntent();
        String entryTrackName = intent.getStringExtra("Name");

        // Set the Header for the screen (as the name of the selected trackable)
        header.setText(entryTrackName + " Check-In");

        // Load the HashMap
        HashMap<String, ArrayList<String>> tracks = readTrackableFile();
        // Get the ArrayList of attributes from the HashMap
        ArrayList<String> attributes = tracks.get(entryTrackName);
        // Get & set adapter
        adapter = new TrackEntryListAdapter(attributes, this);
        listView.setAdapter(adapter);

        setOnClicks(entryTrackName, attributes);

        // Check if the dialog should be shown
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        boolean showDialog = preferences.getBoolean("showDialog", true);
        if (showDialog) showDialog();
    }

    /*
     * Set any necessary click buttons / items. This will also handle the Radio Buttons, which will
     * show or not show the attributes
     */
    public void setOnClicks(final String entryTrackName, final ArrayList<String> atts) {
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeTrackEntry(entryTrackName, atts);
                clearAttributeAnswers();
                Toast.makeText(getWindow().getContext(), "Success!", Toast.LENGTH_SHORT).show();
                // Delay showing the new screen by 1 second to emphasize "Success"
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
            }
        });
        yesRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setVisibility(View.VISIBLE);
                trackableDone = true;
            }
        });
        noRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setVisibility(View.INVISIBLE);
                trackableDone = false;
            }
        });
    }

    /*
     * Store the users answers for the attributes in internal storage.
     * dirName: The directory where the users answers will be stored.
     * attNames: The name of each attribute
     */
    public void writeTrackEntry(String dirName, ArrayList<String> attNames) {
        int entryNum = getMySharedPreference(dirName);
        String fileEntryName = dirName + entryNum + ".txt";
        ArrayList<String> answers = getUsersEntries();
        Date currTime = Calendar.getInstance().getTime();
        File dir = new File(getWindow().getContext().getFilesDir(), dirName);
        try {
            File entry = new File(dir, fileEntryName);
            entry.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(entry));
            bw.write(currTime.toString());
            bw.newLine();

            if(trackableDone) {
                bw.write("Yes");
            }
            else {
                bw.write("No");
            }
            bw.newLine();
            for (int i = 0; i < answers.size(); i++) {
                bw.write(attNames.get(i) + ": " + answers.get(i)); // TODO when using this later on, can check for arraysize = 1, use split, etc to see if an attribute was entered
                bw.newLine();
            }
            incrementSharedPreferences(dirName);
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Increment the counter for the directory getting an entry. This is used for naming of the .txt
     * files being saved.
     */
    public void incrementSharedPreferences(String directoryName) {
        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit();
        int value = getMySharedPreference(directoryName);
        value++;
        editor.putInt(directoryName, value).commit();
    }

    /*
     * Return the counter value for directoryName
     * This is used for naming of each .txt file entry
     */
    public int getMySharedPreference(String directoryName) {
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

     * Returns: The answers to each attribute as an ArrayList
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

    /*
     * Clear all the users answers in the ListView (set each ones text to empty)
     */
    public void clearAttributeAnswers() {
        for (int i = 0; i < listView.getCount(); i++) {
            View v = listView.getChildAt(i);
            EditText et = v.findViewById(R.id.singleentry_edittext_userEntry);
            et.setText("");
        }
    }

    /*
     * Build the Dialog box letting the user know that this part of the app is only for a single entry
     */
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SingleTrackEntry.this);
        builder
                .setCancelable(false)
                .setMessage("This is just for a single entry. If you would like to complete an entry for all track-ables, head to the TabHandler.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setNeutralButton("Don't Show Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("showDialog", false);
                        editor.apply();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
