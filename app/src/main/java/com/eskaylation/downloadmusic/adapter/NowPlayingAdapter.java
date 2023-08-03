package com.eskaylation.downloadmusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.base.GlideApp;
import com.eskaylation.downloadmusic.listener.OnItemNowPlayingOnlineClick;
import com.eskaylation.downloadmusic.listener.OnItemSongClickListener;
import com.eskaylation.downloadmusic.model.AudioExtract;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.utils.AppConstants;
import com.eskaylation.downloadmusic.utils.AppUtils;
import com.eskaylation.downloadmusic.utils.ArtworkUtils;
import java.util.ArrayList;

public class NowPlayingAdapter extends Adapter<ViewHolder> {
    public Context context;
    public Song indexOffline;
    public ArrayList<AudioExtract> listOnline = new ArrayList<>();
    public ArrayList<Song> lstOffline = new ArrayList<>();
    public OnItemSongClickListener mOnItemClickListener;
    public OnItemNowPlayingOnlineClick onlineClickListener;

    public class ItemOfflineViewHolder extends ViewHolder {
        public ImageView imgThumb;
        public TextView tvName;
        public TextView tv_duration;

        public ItemOfflineViewHolder(View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.tv_name);
            this.tv_duration = (TextView) view.findViewById(R.id.tv_duration);
            this.imgThumb = (ImageView) view.findViewById(R.id.img_thumb);
        }

        public void bind(Song song) {
            this.tvName.setText(song.title);
            this.tvName.setSelected(true);
            this.tv_duration.setSelected(true);
            String str = song.duration;
            String convertDuration = str != null ? AppUtils.convertDuration(Long.valueOf(str).longValue()) : NowPlayingAdapter.this.context.getString(R.string.unknow);
            String str2 = song.artist;
            if (str2 == null) {
                str2 = NowPlayingAdapter.this.context.getString(R.string.unknow);
            }
            TextView textView = this.tv_duration;
            StringBuilder sb = new StringBuilder();
            sb.append(convertDuration);
            sb.append(" - ");
            sb.append(str2);
            textView.setText(sb.toString());
            Glide.with(NowPlayingAdapter.this.context).load(ArtworkUtils.uri(song.albumId)).diskCacheStrategy(DiskCacheStrategy.ALL).dontTransform().placeholder((int) R.drawable.song_not_found).into(this.imgThumb);
        }
    }

    public class ItemOnlineViewHolder extends ViewHolder {
        public ImageView imgThumb;
        public TextView tvName;
        public TextView tv_duration;

        public ItemOnlineViewHolder(View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.tv_name);
            this.tv_duration = (TextView) view.findViewById(R.id.tv_duration);
            this.imgThumb = (ImageView) view.findViewById(R.id.img_thumb);
        }

        public void bind(AudioExtract audioExtract) {
            this.tvName.setText(audioExtract.title);
            this.tvName.setSelected(true);
            this.tv_duration.setSelected(true);
            this.tv_duration.setText(!TextUtils.isEmpty(String.valueOf(audioExtract.duration)) ? AppUtils.convertDuration(audioExtract.duration) : NowPlayingAdapter.this.context.getString(R.string.unknow));
            Glide.with(NowPlayingAdapter.this.context).load(Integer.valueOf(AppConstants.randomThumb())).diskCacheStrategy(DiskCacheStrategy.ALL).dontTransform().placeholder((int) R.drawable.song_not_found).into(this.imgThumb);
        }
    }

    public NowPlayingAdapter(Context context2, OnItemSongClickListener onItemSongClickListener, OnItemNowPlayingOnlineClick onItemNowPlayingOnlineClick) {
        this.mOnItemClickListener = onItemSongClickListener;
        this.onlineClickListener = onItemNowPlayingOnlineClick;
        this.context = context2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new ItemOfflineViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_now_playing, viewGroup, false));
        }
        return new ItemOnlineViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_now_playing, viewGroup, false));
    }

    public int getItemViewType(int i) {
        return this.lstOffline.isEmpty() ? 1 : 0;
    }

    public void setIndexOffline(Song song) {
        this.indexOffline = song;
        notifyDataSetChanged();
    }

    @SuppressLint({"ResourceAsColor", "SetTextI18n"})
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ItemOfflineViewHolder) {
            Song song = (Song) this.lstOffline.get(i);
            ItemOfflineViewHolder itemOfflineViewHolder = (ItemOfflineViewHolder) viewHolder;
            itemOfflineViewHolder.bind(song);
            viewHolder.itemView.setOnClickListener(new OnClickListener() {


                public final void onClick(View view) {
                    NowPlayingAdapter.this.NowPlayingAdapter0(i, view);
                }
            });
            Song song2 = this.indexOffline;
            if (song2 != null) {
                String str = "#ffffff";
                if (song2.getmSongPath() == null) {
                    itemOfflineViewHolder.tvName.setTextColor(Color.parseColor(str));
                    itemOfflineViewHolder.tv_duration.setTextColor(Color.parseColor(str));
                } else if (this.indexOffline.getmSongPath().equals(song.getmSongPath())) {
                    String str2 = "#1ed760";
                    itemOfflineViewHolder.tvName.setTextColor(Color.parseColor(str2));
                    itemOfflineViewHolder.tv_duration.setTextColor(Color.parseColor(str2));
                } else {
                    itemOfflineViewHolder.tvName.setTextColor(Color.parseColor(str));
                    itemOfflineViewHolder.tv_duration.setTextColor(Color.parseColor(str));
                }
            }
        } else {
            AudioExtract audioExtract = (AudioExtract) this.listOnline.get(i);
            ((ItemOnlineViewHolder) viewHolder).bind(audioExtract);
            viewHolder.itemView.setOnClickListener(new OnClickListener() {

                public final void onClick(View view) {
                    NowPlayingAdapter.this.NowPlayingAdapter1(audioExtract, view);
                }
            });
        }
    }

    public  void NowPlayingAdapter0(int i, View view) {
        OnItemSongClickListener onItemSongClickListener = this.mOnItemClickListener;
        if (onItemSongClickListener != null) {
            onItemSongClickListener.onItemSongClick(this.lstOffline, i);
        }
    }

    public  void NowPlayingAdapter1(AudioExtract audioExtract, View view) {
        if (this.mOnItemClickListener != null) {
            this.onlineClickListener.onItemNowPlayingOnlineClick(audioExtract);
        }
    }

    public void setListOnline(ArrayList<AudioExtract> arrayList) {
        this.lstOffline.clear();
        this.listOnline.clear();
        this.listOnline.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void setListOffline(ArrayList<Song> arrayList) {
        this.listOnline.clear();
        this.lstOffline.clear();
        this.lstOffline.addAll(arrayList);
        notifyDataSetChanged();
    }

    public int getItemCount() {
        if (!this.lstOffline.isEmpty()) {
            return this.lstOffline.size();
        }
        if (!this.listOnline.isEmpty()) {
            return this.listOnline.size();
        }
        return 0;
    }
}
