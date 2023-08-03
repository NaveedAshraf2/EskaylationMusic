package com.eskaylation.downloadmusic.adapter;

import android.content.Context;
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
import com.eskaylation.downloadmusic.model.AudioExtract;
import com.eskaylation.downloadmusic.utils.AppUtils;
import com.wang.avi.AVLoadingIndicatorView;
import java.util.ArrayList;

public class OnlineAudioAdapter extends Adapter<ViewHolder> {
    public Context context;
    public boolean isMoreLoading = true;
    public ArrayList<AudioExtract> lstAudio = new ArrayList<>();
    public OnItemSelected onItemSelected;
    public OnLoadMoreListener onLoadMoreListener;

    public class ItemHolder extends ViewHolder {
        public ImageView img_thumb;
        public TextView tv_duration;
        public TextView tv_title;

        public ItemHolder(View view) {
            super(view);
            this.tv_title = (TextView) view.findViewById(R.id.tv_name);
            this.img_thumb = (ImageView) view.findViewById(R.id.img_thumb);
            this.tv_duration = (TextView) view.findViewById(R.id.tv_duration);
        }

        public void bindData(int i) {
            AudioExtract audioExtract = (AudioExtract) OnlineAudioAdapter.this.lstAudio.get(i);
            this.tv_title.setText(audioExtract.title);
            this.tv_duration.setText(AppUtils.convertDuration(audioExtract.duration));
            Glide.with(OnlineAudioAdapter.this.context).load(Integer.valueOf(R.drawable.song_not_found)).into(this.img_thumb);
            this.itemView.setOnClickListener(new OnClickListener() {


                public final void onClick(View view) {
                    ItemHolder.this.OnlineAudioAdapter0(audioExtract, i, view);
                }
            });
        }

        public  void OnlineAudioAdapter0(AudioExtract audioExtract, int i, View view) {
            OnItemSelected onItemSelected = OnlineAudioAdapter.this.onItemSelected;
            if (onItemSelected != null) {
                onItemSelected.onClickItem(audioExtract, i);
            }
        }
    }

    public interface OnItemSelected {
        void onClickItem(AudioExtract audioExtract, int i);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public static class ProgressViewHolder extends ViewHolder {
        public ProgressViewHolder(View view) {
            super(view);
            AVLoadingIndicatorView aVLoadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.pb_loading);
        }
    }

    public OnlineAudioAdapter(Context context2, OnItemSelected onItemSelected2) {
        this.context = context2;
        this.onItemSelected = onItemSelected2;
    }

    public int getItemViewType(int i) {
        return this.lstAudio.get(i) != null ? 1 : 0;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1) {
            return new ItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_online_big, viewGroup, false));
        }
        return new ProgressViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_loading, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ItemHolder) {
            ((ItemHolder) viewHolder).bindData(i);
        }
    }

    public int getItemCount() {
        return this.lstAudio.size();
    }

    public void addAll(ArrayList<AudioExtract> arrayList) {
        this.lstAudio.clear();
        this.lstAudio.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void showLoading() {
        if (this.isMoreLoading) {
            ArrayList<AudioExtract> arrayList = this.lstAudio;
            if (arrayList != null && this.onLoadMoreListener != null) {
                this.isMoreLoading = false;
                arrayList.add(null);
                notifyItemInserted(this.lstAudio.size() - 1);
                this.onLoadMoreListener.onLoadMore();
            }
        }
    }

    public void setLoadMore(OnLoadMoreListener onLoadMoreListener2) {
        this.onLoadMoreListener = onLoadMoreListener2;
    }

    public void setMore(boolean z) {
        this.isMoreLoading = z;
    }

    public void dismissLoading() {
        ArrayList<AudioExtract> arrayList = this.lstAudio;
        if (arrayList != null && arrayList.size() > 0) {
            ArrayList<AudioExtract> arrayList2 = this.lstAudio;
            arrayList2.remove(arrayList2.size() - 1);
            notifyItemRemoved(this.lstAudio.size());
        }
    }

    public void addItemMore(ArrayList<AudioExtract> arrayList) {
        int size = this.lstAudio.size();
        this.lstAudio.addAll(arrayList);
        notifyItemRangeChanged(size, this.lstAudio.size());
    }

    public boolean isEmpty() {
        return this.lstAudio.isEmpty();
    }
}
