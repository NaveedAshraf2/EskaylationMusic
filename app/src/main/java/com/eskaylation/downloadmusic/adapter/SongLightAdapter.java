package com.eskaylation.downloadmusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.base.GlideApp;
import com.eskaylation.downloadmusic.listener.OnItemSongClickListener;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.utils.AppUtils;
import com.eskaylation.downloadmusic.utils.ArtworkUtils;
import java.util.ArrayList;

public class SongLightAdapter extends Adapter<SongLightAdapter.RecyclerViewHolder> {
    public Context context;
    public Song itemIndex;
    public ArrayList<Song> lstAudio = new ArrayList<>();
    public OnItemSongClickListener mOnItemClickListener;
    public RotateAnimation rotate;

    public class RecyclerViewHolder extends ViewHolder {
        public ImageView btnMore;
        public ImageView imgThumb;
        public TextView tvName;
        public TextView tv_duration;

        public RecyclerViewHolder(SongLightAdapter songLightAdapter, View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.tv_name);
            this.tv_duration = (TextView) view.findViewById(R.id.tv_duration);
            this.imgThumb = (ImageView) view.findViewById(R.id.img_thumb);
            this.btnMore = (ImageView) view.findViewById(R.id.img_more);
        }
    }

    public SongLightAdapter(Context context2, OnItemSongClickListener onItemSongClickListener) {
        this.mOnItemClickListener = onItemSongClickListener;
        this.context = context2;
    }

    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecyclerViewHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_song_light, viewGroup, false));
    }

    @SuppressLint({"ResourceAsColor"})
    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, int i) {
        Song song = (Song) this.lstAudio.get(i);
        recyclerViewHolder.tvName.setText(song.title);
        String convertDuration = ((Song) this.lstAudio.get(i)).duration != null ? AppUtils.convertDuration(Long.valueOf(((Song) this.lstAudio.get(i)).duration).longValue()) : this.context.getString(R.string.unknow);
        String string = ((Song) this.lstAudio.get(i)).artist != null ? ((Song) this.lstAudio.get(i)).artist : this.context.getString(R.string.unknow);
        TextView textView = recyclerViewHolder.tv_duration;
        StringBuilder sb = new StringBuilder();
        sb.append(convertDuration);
        sb.append(" - ");
        sb.append(string);
        textView.setText(sb.toString());
        Glide.with(this.context).load(ArtworkUtils.uri(song.albumId)).error((int) R.drawable.song_not_found).into(recyclerViewHolder.imgThumb);
        recyclerViewHolder.itemView.setOnClickListener(new OnClickListener() {
          

            public final void onClick(View view) {
                SongLightAdapter.this.SongLightAdapter0(i, view);
            }
        });
        recyclerViewHolder.btnMore.setOnClickListener(new OnClickListener() {
         

            public final void onClick(View view) {
                SongLightAdapter.this.SongLightAdapter1(song, i, view);
            }
        });
        Song song2 = this.itemIndex;
        if (song2 == null) {
            return;
        }
        if (song2.getmSongPath().equals(song.getmSongPath())) {
            String str = "#1ed760";
            recyclerViewHolder.tvName.setTextColor(Color.parseColor(str));
            recyclerViewHolder.tv_duration.setTextColor(Color.parseColor(str));
            rotateThumb(recyclerViewHolder.imgThumb);
            return;
        }
        String str2 = "#ffffff";
        recyclerViewHolder.tvName.setTextColor(Color.parseColor(str2));
        recyclerViewHolder.tv_duration.setTextColor(Color.parseColor(str2));
        clearAnimationThumb(recyclerViewHolder.imgThumb);
    }

    public /* synthetic */ void SongLightAdapter0(int i, View view) {
        OnItemSongClickListener onItemSongClickListener = this.mOnItemClickListener;
        if (onItemSongClickListener != null) {
            onItemSongClickListener.onItemSongClick(this.lstAudio, i);
        }
    }

    public /* synthetic */ void SongLightAdapter1(Song song, int i, View view) {
        OnItemSongClickListener onItemSongClickListener = this.mOnItemClickListener;
        if (onItemSongClickListener != null) {
            onItemSongClickListener.onMoreClick(song, i);
        }
    }

    public void removeAt(int i) {
        this.lstAudio.remove(i);
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, this.lstAudio.size());
    }

    public int getPositionFromName(Song song) {
        for (int i = 0; i < this.lstAudio.size(); i++) {
            if (((Song) this.lstAudio.get(i)).getmSongPath().equals(song.getmSongPath())) {
                return i;
            }
        }
        return 0;
    }

    public int getItemCount() {
        return this.lstAudio.size();
    }

    public void setData(ArrayList<Song> arrayList) {
        this.lstAudio.clear();
        this.lstAudio.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void rotateThumb(ImageView imageView) {
        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
        this.rotate = rotateAnimation;
        this.rotate.setDuration(15000);
        this.rotate.setRepeatCount(-1);
        imageView.startAnimation(this.rotate);
    }

    public void clearAnimationThumb(ImageView imageView) {
        imageView.clearAnimation();
    }
}
