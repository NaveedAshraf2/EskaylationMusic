package com.eskaylation.downloadmusic.listener;

import com.eskaylation.downloadmusic.model.Album;
import java.util.ArrayList;

public interface AlbumListenner {
    void onAlbumLoadedSuccessful(ArrayList<Album> arrayList);
}
