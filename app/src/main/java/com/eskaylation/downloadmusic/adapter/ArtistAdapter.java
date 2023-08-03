package com.eskaylation.downloadmusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.adapter.ArtistAdapter.RecyclerViewHolder;
import com.eskaylation.downloadmusic.model.Artist;
import java.util.ArrayList;

public class ArtistAdapter extends Adapter<RecyclerViewHolder> {
    public Context context;
    public ArrayList<Artist> lstAudio = new ArrayList<>();
    public OnItemArtistClickListener mOnItemArtistClickListener;

    public interface OnItemArtistClickListener {
        void onArtistClick(int i, Artist artist, ImageView imageView, TextView textView, TextView textView2);
    }

    public class RecyclerViewHolder extends ViewHolder {
        public ImageView imgThumb;
        public TextView tvName;
        public TextView tv_count_song;

        public RecyclerViewHolder(ArtistAdapter artistAdapter, View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.tv_name);
            this.tv_count_song = (TextView) view.findViewById(R.id.tv_count_song);
            this.imgThumb = (ImageView) view.findViewById(R.id.img_thumb);
        }
    }

    public ArtistAdapter(Context context2, OnItemArtistClickListener onItemArtistClickListener) {
        this.mOnItemArtistClickListener = onItemArtistClickListener;
        this.context = context2;
    }

    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecyclerViewHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_artist, viewGroup, false));
    }

    @SuppressLint({"ResourceAsColor"})
    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, int i) {
        Artist artist = (Artist) this.lstAudio.get(i);
        recyclerViewHolder.tvName.setText(artist.getName());
        recyclerViewHolder.tvName.setSelected(true);
        TextView textView = recyclerViewHolder.tv_count_song;
        StringBuilder sb = new StringBuilder();
        sb.append(artist.getTrackCount());
        sb.append(" ");
        sb.append(this.context.getString(R.string.txt_songs));
        textView.setText(sb.toString());

        recyclerViewHolder.itemView.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                ArtistAdapter.this.lambda$onBindViewHolder$0$ArtistAdapter(i, artist, recyclerViewHolder, view);
            }
        });
    }

    public  void lambda$onBindViewHolder$0$ArtistAdapter(int i, Artist artist, RecyclerViewHolder recyclerViewHolder, View view) {
        OnItemArtistClickListener onItemArtistClickListener = this.mOnItemArtistClickListener;
        if (onItemArtistClickListener != null) {
            onItemArtistClickListener.onArtistClick(i, artist, recyclerViewHolder.imgThumb, recyclerViewHolder.tvName, recyclerViewHolder.tv_count_song);
        }
    }
    public int getItemCount() {
        return this.lstAudio.size();
    }

    public void setData(ArrayList<Artist> arrayList) {
        this.lstAudio.clear();
        this.lstAudio.addAll(arrayList);
        notifyDataSetChanged();
    }

}