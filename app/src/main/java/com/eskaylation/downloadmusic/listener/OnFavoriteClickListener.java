package com.eskaylation.downloadmusic.listener;

import android.widget.ImageView;
import android.widget.TextView;
import com.eskaylation.downloadmusic.model.Favorite;

public interface OnFavoriteClickListener {
    void onItemFavoriteClick(int i, Favorite favorite, ImageView imageView, TextView textView);

    void onMoreClick(int i, Favorite favorite);

}
