package com.example.lifetrackerplus;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewTrackListAdapter extends RecyclerView.Adapter<ViewTrackListAdapter.ViewTrackViewHolder> {

    private ArrayList<ViewTrackListItem> tracksList;

    public static class ViewTrackViewHolder extends RecyclerView.ViewHolder {
        public ImageView moreInfoImgBtn, checkInImgBtn, deleteImgBtn;
        public TextView trackName;

        public ViewTrackViewHolder(@NonNull View itemView) {
            super(itemView);
            trackName = itemView.findViewById(R.id.viewtracks_cardview_nameOfTrack);
        }
    }

    public ViewTrackListAdapter(ArrayList<ViewTrackListItem> items) {
        tracksList = items;
    }

    @NonNull
    @Override
    public ViewTrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewtracks_cardview_listlayout, parent, false);
        ViewTrackViewHolder vtvh = new ViewTrackViewHolder(v);
        return vtvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewTrackViewHolder holder, int position) {
        ViewTrackListItem cItem = tracksList.get(position);
        holder.trackName.setText(cItem.getItemName()); // set the list items text to the value of the Track-able
    }

    @Override
    public int getItemCount() {
        return tracksList.size();
    }

}
