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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.Utils;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.base.GlideApp;
import com.eskaylation.downloadmusic.model.Album;
import com.eskaylation.downloadmusic.utils.ArtworkUtils;
import java.util.ArrayList;

public class AlbumAdapter extends Adapter<AlbumAdapter.GridHolder> {
    public Context context;
    public ArrayList<Album> lst = new ArrayList<>();
    public OnItemAlbumClickListener mOnItemClickListener;

    public class GridHolder extends ViewHolder {
        @BindView(R.id.imgThumbGrid)
        public LottieAnimationView img_thumb;

        @BindView(R.id.tv_name)
        public TextView tv_name_album;

        public GridHolder(AlbumAdapter albumAdapter, View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }


    public interface OnItemAlbumClickListener {
        void onItemClick(int i, Album album);
    }

    public AlbumAdapter(Context context2, OnItemAlbumClickListener onItemAlbumClickListener) {
        this.mOnItemClickListener = onItemAlbumClickListener;
        this.context = context2;
    }

    public GridHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new GridHolder(this, LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_album, viewGroup, false));
    }

    public void setData(ArrayList<Album> arrayList) {
        this.lst.clear();
        this.lst.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void onBindViewHolder(GridHolder gridHolder, int i) {
        Album album = (Album) this.lst.get(i);

//        Glide.with(this.context).load(ArtworkUtils.getAlbumArtUri((int)
//                album.getId())).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder((int)
//                R.drawable.ic_album).error((int) R.drawable.ic_album).transition(GenericTransitionOptions.with(R.anim.fade_in)).into(gridHolder.img_thumb);
//
        gridHolder.tv_name_album.setText(album.getAlbumName());

        gridHolder.itemView.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                AlbumAdapter.this.AlbumAdapter0(i, album, view);
            }
        });
    }

    public void AlbumAdapter0(int i, Album album, View view) {
        OnItemAlbumClickListener onItemAlbumClickListener = this.mOnItemClickListener;
        if (onItemAlbumClickListener != null) {
            onItemAlbumClickListener.onItemClick(i, album);
        }
    }

    public int getItemCount() {
        ArrayList<Album> arrayList = this.lst;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }
}
