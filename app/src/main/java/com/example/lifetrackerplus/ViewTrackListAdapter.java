package com.example.lifetrackerplus;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Adapter class to implement the RecyclerView that holds all the users Track-Ables.
 */
public class ViewTrackListAdapter extends RecyclerView.Adapter<ViewTrackListAdapter.ViewTrackViewHolder> {

    private ArrayList<ViewTrackListItem> tracksList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    // Implement the interface for each button
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        listener = onItemClickListener;
    }

    public static class ViewTrackViewHolder extends RecyclerView.ViewHolder {
        public ImageView moreInfoImgBtn, checkInImgBtn, deleteImgBtn;
        public TextView trackName;

        public ViewTrackViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);

            // Hooks
            trackName = itemView.findViewById(R.id.viewtracks_cardview_nameOfTrack);
            deleteImgBtn = itemView.findViewById(R.id.viewtracks_imgbtn_del);
            moreInfoImgBtn = itemView.findViewById(R.id.viewtracks_imgbtn_moreInfo);
            checkInImgBtn = itemView.findViewById(R.id.viewtracks_imgbtn_checkIn);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
            deleteImgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    public ViewTrackListAdapter(ArrayList<ViewTrackListItem> items) {
        tracksList = items;
    }

    @NonNull
    @Override
    public ViewTrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewtracks_cardview_listlayout, parent, false);
        ViewTrackViewHolder vtvh = new ViewTrackViewHolder(v, listener);
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
