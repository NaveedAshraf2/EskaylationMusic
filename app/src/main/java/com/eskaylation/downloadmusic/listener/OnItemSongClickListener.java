package com.eskaylation.downloadmusic.listener;

import com.eskaylation.downloadmusic.model.Song;
import java.util.ArrayList;

public interface OnItemSongClickListener {
    void onItemSongClick(ArrayList<Song> arrayList, int i);

    void onMoreClick(Song song, int i);
}
