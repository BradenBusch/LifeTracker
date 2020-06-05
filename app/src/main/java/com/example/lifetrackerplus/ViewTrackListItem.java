package com.example.lifetrackerplus;

import java.util.ArrayList;

/*
 * Class that holds name and other necessary information in each card in 'ViewTracks'
 */
public class ViewTrackListItem {

    private String mName;
    private ArrayList<String> attributes;

    // TODO not sure where to put attributes, can always retrieve it again later
    public ViewTrackListItem(String name) {
        mName = name;
        //this.attributes = attributes;
    }

    public String getItemName() {
        return mName;
    }

//    public ArrayList<String> getItemAttributes() {
//        return attributes;
//    }
}
