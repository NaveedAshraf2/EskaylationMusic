package com.eskaylation.downloadmusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.adapter.SongAdapter.RecyclerViewHolder;
import com.eskaylation.downloadmusic.base.GlideApp;
import com.eskaylation.downloadmusic.listener.OnItemSongClickListener;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.utils.AppUtils;
import com.eskaylation.downloadmusic.utils.ArtworkUtils;
import java.util.ArrayList;

public class SongAdapter extends Adapter<RecyclerViewHolder> {
    public Context context;
    public Song itemIndex;
    public ArrayList<Song> lstAudio;
    public OnItemSongClickListener mOnItemClickListener;

    public class RecyclerViewHolder extends ViewHolder {
        public ImageView btnMore;
        public ImageView imgThumb;
        public TextView tvName;
        public TextView tv_duration;

        public RecyclerViewHolder(SongAdapter songAdapter, View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.tv_name);
            this.tv_duration = (TextView) view.findViewById(R.id.tv_duration);
            this.imgThumb = (ImageView) view.findViewById(R.id.img_thumb);
            this.btnMore = (ImageView) view.findViewById(R.id.img_more);
        }
    }

    public SongAdapter(Context context2, ArrayList<Song> arrayList, OnItemSongClickListener onItemSongClickListener) {
        this.mOnItemClickListener = onItemSongClickListener;
        this.context = context2;
        this.lstAudio = arrayList;
    }

    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecyclerViewHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_song, viewGroup, false));
    }

    @SuppressLint({"ResourceAsColor"})
    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, int i) {
        Song song = (Song) this.lstAudio.get(i);
        recyclerViewHolder.tvName.setText(song.title);
        recyclerViewHolder.tvName.setSelected(true);
        recyclerViewHolder.tv_duration.setSelected(true);
        String convertDuration = ((Song) this.lstAudio.get(i)).duration != null ? AppUtils.convertDuration(Long.valueOf(((Song) this.lstAudio.get(i)).duration).longValue()) : this.context.getString(R.string.unknow);
        String string = ((Song) this.lstAudio.get(i)).artist != null ? ((Song) this.lstAudio.get(i)).artist : this.context.getString(R.string.unknow);
        TextView textView = recyclerViewHolder.tv_duration;
        StringBuilder sb = new StringBuilder();
        sb.append(convertDuration);
        sb.append(" - ");
        sb.append(string);
        textView.setText(sb.toString());
        Glide.with(this.context).load(ArtworkUtils.uri(song.albumId)).placeholder((int) R.drawable.song_not_found).into(recyclerViewHolder.imgThumb);
        recyclerViewHolder.itemView.setOnClickListener(new OnClickListener() {
          
            public final void onClick(View view) {
                SongAdapter.this.SongAdapter1(recyclerViewHolder, song, i, view);
            }
        });
        recyclerViewHolder.btnMore.setOnClickListener(new OnClickListener() {
        

            public final void onClick(View view) {
                SongAdapter.this.SongAdapter0(song, i, view);
            }
        });
        onClickItem(recyclerViewHolder, song);
    }

    public  void SongAdapter1(RecyclerViewHolder recyclerViewHolder, Song song, int i, View view) {
        try {
            if (this.mOnItemClickListener != null) {
                onClickItem(recyclerViewHolder, song);
                this.mOnItemClickListener.onItemSongClick(this.lstAudio, i);
            }
        } catch (Exception unused) {
        }
    }

    public /* synthetic */ void SongAdapter0(Song song, int i, View view) {
        OnItemSongClickListener onItemSongClickListener = this.mOnItemClickListener;
        if (onItemSongClickListener != null) {
            onItemSongClickListener.onMoreClick(song, i);
        }
    }

    public void onClickItem(RecyclerViewHolder recyclerViewHolder, Song song) {
        Song song2 = this.itemIndex;
        if (song2 != null) {
            String str = "#ffffff";
            if (song2.getmSongPath() == null) {
                recyclerViewHolder.tvName.setTextColor(Color.parseColor(str));
                recyclerViewHolder.tv_duration.setTextColor(Color.parseColor(str));
            } else if (this.itemIndex.getmSongPath().equals(song.getmSongPath())) {
                String str2 = "#1ed760";
                recyclerViewHolder.tvName.setTextColor(Color.parseColor(str2));
                recyclerViewHolder.tv_duration.setTextColor(Color.parseColor(str2));
            } else {
                recyclerViewHolder.tvName.setTextColor(Color.parseColor(str));
                recyclerViewHolder.tv_duration.setTextColor(Color.parseColor(str));
            }
        }
    }

    public void removeAt(int i) {
        this.lstAudio.remove(i);
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, this.lstAudio.size());
    }

    public void setListItem(ArrayList<Song> arrayList) {
        this.lstAudio = arrayList;
        notifyDataSetChanged();
    }

    public int getPositionFromSong(Song song) {
        return this.lstAudio.indexOf(song);
    }

    public ArrayList<Song> getListSong() {
        return this.lstAudio;
    }

    public int getItemCount() {
        return this.lstAudio.size();
    }

    public void setItemIndex(Song song) {
        this.itemIndex = song;
        notifyDataSetChanged();
    }
}
