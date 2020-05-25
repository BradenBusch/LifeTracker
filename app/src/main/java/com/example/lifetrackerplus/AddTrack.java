package com.example.lifetrackerplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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
 - Possibly make the first add track button hidden, then when the add button with it is clicked,
 - another pops up.
 - Also could make it a list view, adding a new value to it each time (then adding the values in the list view to the arraylist).

 - I think i can use a hashmap instead of a class for storing the values (String, ArrayList<String>)
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

        // Setters
        attributeList = new ArrayList<>();
        adapter = new AddTrackListAdapter(attributeList, this);
        listView.setAdapter(adapter);

        // On Click Methods
        addAttributeBtnClick();
        confirmAndCancelClick();

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

    // Handle the 'add' button being clicked
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
    public void confirmAndCancelClick() {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Cancel btn", Toast.LENGTH_SHORT).show();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verify all the fields, add the value to the database
            }
        });
    }
}
