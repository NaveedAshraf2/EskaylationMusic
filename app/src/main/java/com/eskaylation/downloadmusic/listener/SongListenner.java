package com.eskaylation.downloadmusic.listener;

import com.eskaylation.downloadmusic.model.Song;
import java.util.ArrayList;

public interface SongListenner {
    void onAudioLoadedSuccessful(ArrayList<Song> arrayList);
}
