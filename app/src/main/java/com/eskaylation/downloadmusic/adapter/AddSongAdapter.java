package com.eskaylation.downloadmusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.adapter.AddSongAdapter.RecyclerViewHolder;
import com.eskaylation.downloadmusic.base.GlideApp;
import com.eskaylation.downloadmusic.model.Song;
import com.eskaylation.downloadmusic.utils.AppUtils;
import com.eskaylation.downloadmusic.utils.ArtworkUtils;
import java.util.ArrayList;

public class AddSongAdapter extends Adapter<RecyclerViewHolder> {
    public Context context;
    public ArrayList<Song> lstAudio = new ArrayList<>();
    public ArrayList<Song> lstSelected = new ArrayList<>();

    public class RecyclerViewHolder extends ViewHolder {
        public CheckBox ckbSelect;
        public ImageView imgThumb;
        public TextView tvName;
        public TextView tv_duration;

        public RecyclerViewHolder(AddSongAdapter addSongAdapter, View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.tv_name);
            this.tv_duration = (TextView) view.findViewById(R.id.tv_duration);
            this.imgThumb = (ImageView) view.findViewById(R.id.img_thumb);
            this.ckbSelect = (CheckBox) view.findViewById(R.id.ckbSelect);
        }
    }

    public AddSongAdapter(Context context2) {
        this.context = context2;
    }

    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecyclerViewHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_add_song, viewGroup, false));
    }

    @SuppressLint({"ResourceAsColor"})
    public void onBindViewHolder(final RecyclerViewHolder recyclerViewHolder, int i) {
        final Song song = (Song) this.lstAudio.get(i);
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
        Glide.with(this.context).load(ArtworkUtils.uri(song.albumId)).placeholder((int) R.drawable.song_not_found)
                .into(recyclerViewHolder.imgThumb);
        recyclerViewHolder.itemView.setOnClickListener(new OnClickListener() {
      

            public final void onClick(View view) {
                AddSongAdapter.this.AddSongAdapter0(song, recyclerViewHolder, view);
            }
        });
        recyclerViewHolder.ckbSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                AddSongAdapter.this.AddSongAdapter1(song, compoundButton, z);
            }
        });
        recyclerViewHolder.ckbSelect.setChecked(song.isSelected);
    }

    public  void AddSongAdapter0(Song song, RecyclerViewHolder recyclerViewHolder, View view) {
        Log.e("Click", "true");
        if (!song.isSelected) {
            song.setSelected(true);
            recyclerViewHolder.ckbSelect.setChecked(true);
            this.lstSelected.add(song);
            return;
        }
        song.setSelected(false);
        recyclerViewHolder.ckbSelect.setChecked(false);
        removeSong(song);
    }

    public /* synthetic */ void AddSongAdapter1(Song song, CompoundButton compoundButton, boolean z) {
        if (z) {
            song.setSelected(true);
            this.lstSelected.add(song);
            return;
        }
        song.setSelected(false);
        removeSong(song);
    }

    public void removeSong(Song song) {
        for (int i = 0; i < this.lstSelected.size(); i++) {
            Song song2 = (Song) this.lstSelected.get(i);
            if (song.getmSongPath().equals(song2.getmSongPath())) {
                this.lstSelected.remove(song2);
                return;
            }
        }
    }

    public void setData(ArrayList<Song> arrayList) {
        this.lstAudio.clear();
        this.lstAudio.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void initFavoriteData(ArrayList<Song> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            Song song = (Song) arrayList.get(i);
            int i2 = 0;
            while (true) {
                if (i2 >= this.lstAudio.size()) {
                    break;
                } else if (((Song) this.lstAudio.get(i2)).getmSongPath().equals(song.getmSongPath())) {
                    ArrayList<Song> arrayList2 = this.lstAudio;
                    arrayList2.remove(arrayList2.get(i2));
                    break;
                } else {
                    i2++;
                }
            }
        }
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return this.lstAudio.size();
    }

    public ArrayList<Song> getListSelect() {
        return this.lstSelected;
    }
}
