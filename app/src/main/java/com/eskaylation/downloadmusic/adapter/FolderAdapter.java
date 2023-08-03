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
import com.eskaylation.downloadmusic.adapter.FolderAdapter.RecyclerViewHolder;
import com.eskaylation.downloadmusic.model.Folder;
import java.util.ArrayList;
import java.util.List;

public class FolderAdapter extends Adapter<RecyclerViewHolder> {
    public Context context;
    public List<Folder> lstFolder = new ArrayList();
    public OnItemFolderClickListener mOnItemFolderClickListener;

    public interface OnItemFolderClickListener {
        void onFolderClick(int i, Folder folder, ImageView imageView, TextView textView, TextView textView2);
    }

    public class RecyclerViewHolder extends ViewHolder {
        public ImageView imgThumb;
        public TextView tvName;
        public TextView tv_count_song;

        public RecyclerViewHolder(FolderAdapter folderAdapter, View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.tv_name);
            this.tv_count_song = (TextView) view.findViewById(R.id.tv_count_song);
            this.imgThumb = (ImageView) view.findViewById(R.id.img_thumb);
        }
    }

    public FolderAdapter(Context context2, OnItemFolderClickListener onItemFolderClickListener) {
        this.mOnItemFolderClickListener = onItemFolderClickListener;
        this.context = context2;
    }

    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecyclerViewHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_folder_music, viewGroup, false));
    }

    @SuppressLint({"ResourceAsColor"})
    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, int i) {
        Folder folder = (Folder) this.lstFolder.get(i);
        recyclerViewHolder.tvName.setText(folder.name);
        TextView textView = recyclerViewHolder.tv_count_song;
        StringBuilder sb = new StringBuilder();
        sb.append(folder.count);
        sb.append(" ");
        sb.append(this.context.getString(R.string.txt_songs));
        textView.setText(sb.toString());
        recyclerViewHolder.itemView.setOnClickListener(new OnClickListener() {


            public final void onClick(View view) {
                FolderAdapter.this.FolderAdapter0(i, folder, recyclerViewHolder, view);
            }
        });
    }

    public void FolderAdapter0(int i, Folder folder, RecyclerViewHolder recyclerViewHolder, View view) {
        OnItemFolderClickListener onItemFolderClickListener = this.mOnItemFolderClickListener;
        if (onItemFolderClickListener != null) {
            onItemFolderClickListener.onFolderClick(i, folder, recyclerViewHolder.imgThumb, recyclerViewHolder.tvName, recyclerViewHolder.tv_count_song);
        }
    }

    public void setData(ArrayList<Folder> arrayList) {
        this.lstFolder.clear();
        this.lstFolder.addAll(arrayList);
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return this.lstFolder.size();
    }
}
