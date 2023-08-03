package com.eskaylation.downloadmusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.listener.OnAddFavoriteClickListener;
import com.eskaylation.downloadmusic.model.Favorite;
import java.util.ArrayList;

public class AddPlaylistSongAdapter extends Adapter<AddPlaylistSongAdapter.RecyclerViewHolder> {
    public Context context;
    public ArrayList<Favorite> lstFavorite;
    public OnAddFavoriteClickListener mOnItemClickListener;

    public class RecyclerViewHolder extends ViewHolder {
        public ImageView btnMore;
        public ImageButton imgThumb;
        public TextView tvName;

        public RecyclerViewHolder(AddPlaylistSongAdapter addPlaylistSongAdapter, View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.tv_name);
            this.btnMore = (ImageView) view.findViewById(R.id.btn_more);
            this.imgThumb = (ImageButton) view.findViewById(R.id.img_thumb);
        }
    }

    public AddPlaylistSongAdapter(Context context2, ArrayList<Favorite> arrayList, OnAddFavoriteClickListener onAddFavoriteClickListener) {
        this.mOnItemClickListener = onAddFavoriteClickListener;
        this.lstFavorite = arrayList;
        this.context = context2;
    }

    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecyclerViewHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_playlist_add, viewGroup, false));
    }

    @SuppressLint({"ResourceAsColor"})
    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, final int i) {
        String str = ((Favorite) this.lstFavorite.get(i)).name;
        recyclerViewHolder.btnMore.setVisibility(View.GONE);
        recyclerViewHolder.tvName.setText(str);
        if (str.equals("DEFAULT_FAVORITEDOWNLOAD")) {
            recyclerViewHolder.tvName.setText(this.context.getString(R.string.favorite_song));
            recyclerViewHolder.imgThumb.setImageResource(R.drawable.ic_love_song);
            recyclerViewHolder.tvName.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            recyclerViewHolder.tvName.setText(str);
            recyclerViewHolder.imgThumb.setImageResource(R.drawable.ic_playlist_black);
            recyclerViewHolder.tvName.setTypeface(Typeface.DEFAULT);
        }
        recyclerViewHolder.itemView.setOnClickListener(new OnClickListener() {
       
            public final void onClick(View view) {
                AddPlaylistSongAdapter.this.AddPlaylistSongAdapter0(i, view);
            }
        });
    }

    public  void AddPlaylistSongAdapter0(int i, View view) {
        OnAddFavoriteClickListener onAddFavoriteClickListener = this.mOnItemClickListener;
        if (onAddFavoriteClickListener != null) {
            onAddFavoriteClickListener.onItemFavoriteClick(i);
        }
    }

    public int getItemCount() {
        return this.lstFavorite.size();
    }
}
