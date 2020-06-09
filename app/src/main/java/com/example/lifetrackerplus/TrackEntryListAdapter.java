package com.example.lifetrackerplus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrackEntryListAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> trackAttributes;
    TextView attributeName;
    EditText attributeEntry;

    public TrackEntryListAdapter(ArrayList<String> trackAttributes, Context context) {
        this.trackAttributes = trackAttributes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return trackAttributes.size();
    }

    @Override
    public String getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.singleentry_listview_lilayout, viewGroup, false);
        }
        attributeName = v.findViewById(R.id.singleentry_textview_attValue);
        attributeName.setText(trackAttributes.get(position));
        attributeEntry = v.findViewById(R.id.singleentry_edittext_userEntry);

        return v;
    }
}
