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
import com.eskaylation.downloadmusic.adapter.PlaylistAdapter.RecyclerViewHolder;
import com.eskaylation.downloadmusic.listener.OnFavoriteClickListener;
import com.eskaylation.downloadmusic.model.Favorite;
import java.util.ArrayList;

public class PlaylistAdapter extends Adapter<RecyclerViewHolder> {
    public Context context;
    public ArrayList<Favorite> lstFavorite;
    public OnFavoriteClickListener mOnItemClickListener;

    public class RecyclerViewHolder extends ViewHolder {
        public ImageView btnMore;
        public ImageView imgThumb;
        public TextView tvName;

        public RecyclerViewHolder(PlaylistAdapter playlistAdapter, View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.tv_name);
            this.btnMore = (ImageView) view.findViewById(R.id.btn_more);
            this.imgThumb = (ImageView) view.findViewById(R.id.img_thumb);
        }
    }

    public PlaylistAdapter(Context context2, ArrayList<Favorite> arrayList, OnFavoriteClickListener onFavoriteClickListener) {
        this.mOnItemClickListener = onFavoriteClickListener;
        this.lstFavorite = arrayList;
        this.context = context2;
    }

    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecyclerViewHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_playlist, viewGroup, false));
    }

    @SuppressLint({"ResourceAsColor"})
    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, @SuppressLint("RecyclerView") int i) {
        Favorite favorite = (Favorite) this.lstFavorite.get(i);
        if (favorite.name.equals("DEFAULT_FAVORITEDOWNLOAD")) {
            recyclerViewHolder.tvName.setText(this.context.getString(R.string.favorite_song));
            recyclerViewHolder.btnMore.setVisibility(View.GONE);
        } else {
            recyclerViewHolder.tvName.setText(favorite.name);
            recyclerViewHolder.btnMore.setVisibility(View.VISIBLE);
        }
        recyclerViewHolder.itemView.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                PlaylistAdapter.this.PlaylistAdapter0(i, favorite, recyclerViewHolder, view);
            }
        });

        recyclerViewHolder.btnMore.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                PlaylistAdapter.this.PlaylistAdapter1(i, favorite, view);
            }
        });
    }

    public  void PlaylistAdapter0(int i, Favorite favorite, RecyclerViewHolder recyclerViewHolder, View view) {
        OnFavoriteClickListener onFavoriteClickListener = this.mOnItemClickListener;
        if (onFavoriteClickListener != null) {
            onFavoriteClickListener.onItemFavoriteClick(i, favorite, recyclerViewHolder.imgThumb, recyclerViewHolder.tvName);
        }
    }

    public /* synthetic */ void PlaylistAdapter1(int i, Favorite favorite, View view) {
        OnFavoriteClickListener onFavoriteClickListener = this.mOnItemClickListener;
        if (onFavoriteClickListener != null) {
            onFavoriteClickListener.onMoreClick(i, favorite);
        }
    }

    public void updateItem(int i, Favorite favorite) {
        this.lstFavorite.set(i, favorite);
        notifyItemChanged(i, Integer.valueOf(this.lstFavorite.size() - 1));
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return this.lstFavorite.size();
    }

    public void deleteItem(int i) {
        this.lstFavorite.remove(i);
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, this.lstFavorite.size());
    }
}
