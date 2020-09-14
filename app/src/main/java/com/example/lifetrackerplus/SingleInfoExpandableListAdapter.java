package com.example.lifetrackerplus;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class SingleInfoExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> trackableEntryNames; //this is like ["track1.txt", track2.txt"]
    // TODO going to need nested loops of file reading to populate this hashmap
    private HashMap<String, ArrayList<String>> trackableAttributeAnswers; //this is like ["track1.txt" -> [Yes: I did ___, No: I did ___], "track2.txt" -> [xxxx]]

    public SingleInfoExpandableListAdapter(Context context, ArrayList<String> trackableEntryNames, HashMap<String, ArrayList<String>> trackableAttributeAnswers) {
        this.context = context;
        this.trackableEntryNames = trackableEntryNames;
        this.trackableAttributeAnswers = trackableAttributeAnswers;
    }

    @Override
    public int getGroupCount() {
        return trackableEntryNames.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return trackableAttributeAnswers.get(trackableEntryNames.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return trackableEntryNames.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return trackableAttributeAnswers.get(trackableEntryNames.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerText = (String)getGroup(i);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.singleinfo_exview, null);
        }
        TextView entryListHeader = (TextView) view.findViewById(R.id.singleinfo_textview_entryName);
        entryListHeader.setTypeface(null, Typeface.BOLD);
        entryListHeader.setText(headerText);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String childText = (String) getChild(i, i1);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.singleinfo_exview_dropdown, null);
        }
        TextView attributeAnswer = (TextView)view.findViewById(R.id.singleinfo_li_trackableAttributeAnswer);
        attributeAnswer.setText(childText);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
