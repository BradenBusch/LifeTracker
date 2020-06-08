package com.example.lifetrackerplus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class AddTrackTab extends Fragment {

    RadioGroup radioGroup;
    RadioButton radioButton, yesRadioBtn, noRadioBtn;
    EditText addAttributeEditText, nameEditText;
    Button addAttributeBtn, confirmBtn;
    View addAttributeBarSep;
    TextView addedAttributesLabel;
    ListView listView;
    ArrayList<String> attributeList;
    AddTrackListAdapter adapter;
    public static final String SHARED_PREFS = "sharedPrefs";

    /*
     * This method is just to make the keyboard not push buttons up when typing
     * Stack Overflow: https://stackoverflow.com/questions/4207880/android-how-do-i-prevent-the-soft-keyboard-from-pushing-my-view-up
     */
    public AddTrackTab() {
        // empty constructor
    }

    public void onResume() {
        super.onResume();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_add_track, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View v = getView(); // Just v for simplicity for hooks
        // Hooks
        nameEditText = v.findViewById(R.id.addtrack_edittext_nameEntry);
        radioGroup = v.findViewById(R.id.addtrack_radiogroup_yesNo);
        yesRadioBtn = v.findViewById(R.id.addtrack_radiobtn_yes);
        noRadioBtn = v.findViewById(R.id.addtrack_radiobtn_no);
        addAttributeEditText = v.findViewById(R.id.addtrack_edittext_addValText);
        addAttributeBtn = v.findViewById(R.id.addtrack_btn_addVal);
        addAttributeBarSep = v.findViewById(R.id.addtrack_view_specBar);
        addedAttributesLabel = v.findViewById(R.id.addtrack_textview_addedAttributesLabel);
        listView = v.findViewById(R.id.addtrack_listview_addedVals);
        confirmBtn = v.findViewById(R.id.addtrack_btn_confirm);

        // Set the ListView adapter
        attributeList = new ArrayList<>();
        adapter = new AddTrackListAdapter(attributeList, v.getContext()); // or just getContext()
        listView.setAdapter(adapter);

        // Get the persistent HashMap
        HashMap<String, ArrayList<String>> tracks = readTrackableFile();

        // On Click Methods
        addAttributeBtnClick();
        confirmAndCancelClick(tracks);
        radioBtnShowEditText(v);
    }

    /*
     * Handles the clicking of the radio button on this page. When a radio button is clicked, this
     * method will either hide or show the EditText and Button.
     */
    public void radioBtnShowEditText(View view) {

        yesRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Beep", "here");
                addAttributeEditText.setVisibility(View.VISIBLE);
                addAttributeBtn.setVisibility(View.VISIBLE);
                addAttributeBarSep.setVisibility(View.VISIBLE);
                addedAttributesLabel.setVisibility(View.VISIBLE);
            }

        });
        noRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Beep", "hre2");
                addAttributeEditText.setVisibility(View.INVISIBLE);
                addAttributeBtn.setVisibility(View.INVISIBLE);
                addAttributeBarSep.setVisibility(View.INVISIBLE);
                addedAttributesLabel.setVisibility(View.INVISIBLE);
            }
        });
    }

    // Handle the 'add' button being clicked. This will add the users value to their attribute list.
    public void addAttributeBtnClick() {
        addAttributeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredAttribute = addAttributeEditText.getText().toString();
                // Check if this attribute name is already in the list
                if (attributeList.contains(enteredAttribute)) {
                    Toast.makeText(view.getContext(), "You already added this attribute!", Toast.LENGTH_SHORT).show();
                }
                // Check if the user entered nothing for a name
                else if (enteredAttribute.equals("")) {
                    Toast.makeText(view.getContext(), "You must enter some text", Toast.LENGTH_SHORT).show();
                }
                // Add the entered attribute to the ArrayList
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
        // Clicking the confirm button will write the information to internal storage and clear the fields
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the user entered an empty string for name
                String nameVal = nameEditText.getText().toString();
                if (nameVal.equals("")) {
                    Toast.makeText(view.getContext(), "You must enter a name for your trackable.", Toast.LENGTH_SHORT).show();
                    nameEditText.getText().clear();
                }
                // Check if this trackable is already in the HashMap (tracks)
                else if (tracks.containsKey(nameVal)) {
                    Toast.makeText(view.getContext(), "You already are tracking that! Try another name.", Toast.LENGTH_SHORT).show();
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
                    // TODO make sure correct HashMap is being added to (reload page or whatever to trigger it)
                }
            }
        });
    }

    /*
     * Create the directory for the Users entries to be stored. This method will also create the
     * shared preferences for a counter variable.
     */
    public void createTrackableDirectory(String directoryName) {
        File file = new File(getActivity().getFilesDir(), directoryName);
        if (!file.exists()) {
            file.mkdir();
        }
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(directoryName, 1);
        editor.commit();
    }

    // TODO move this to where it must be moved
//    SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
//    int value = getPreferences(MODE_PRIVATE).getInt(directoryName, 0);
//    value++;
//    getPreferences(MODE_PRIVATE).edit().putInt(directoryName, value);
//}public void incrementSharedPreferences(String directoryName) {


    // Write the HashMap to the file
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

    // Read the HashMap file and return the HashMap stored in it.
    public HashMap<String, ArrayList<String>> readTrackableFile() {
        String path = getContext().getFilesDir() + "/trackablesdir/" + "trackables.txt";
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
