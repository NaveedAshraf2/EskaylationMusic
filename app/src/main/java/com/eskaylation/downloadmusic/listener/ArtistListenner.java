package com.eskaylation.downloadmusic.listener;

import com.eskaylation.downloadmusic.model.Artist;
import java.util.ArrayList;

public interface ArtistListenner {
    void onLoadArtistSuccessful(ArrayList<Artist> arrayList);
}
