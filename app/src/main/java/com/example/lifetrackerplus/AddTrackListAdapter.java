package com.example.lifetrackerplus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Class for handling the effects of clicking on a list element of the list in AddTracks
 */
public class AddTrackListAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> listValues;
    TextView liString;
    Button delBtn;

    public AddTrackListAdapter(ArrayList<String> listValues, Context context) {
        this.context = context;
        this.listValues = listValues;
    }

    @Override
    public int getCount() {
        return listValues.size();
    }

    @Override
    public String getItem(int i) {
        return listValues.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View conView, ViewGroup viewGroup) {
        View v = conView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.addtrack_listview_listlayout, viewGroup, false);
        }
        String currentAttribute = getItem(position);
        liString = v.findViewById(R.id.addtrack_li_value);
        liString.setText(currentAttribute);

        delBtn = v.findViewById(R.id.addtrack_listviewBtn_delBtn);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listValues.remove(position);
                notifyDataSetChanged();
            }
        });
        return v;
    }
}
