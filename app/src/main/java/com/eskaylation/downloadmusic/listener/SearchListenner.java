package com.eskaylation.downloadmusic.listener;

import com.eskaylation.downloadmusic.model.Song;
import java.util.ArrayList;

public interface SearchListenner {
    void onAudioSearchSuccessful(ArrayList<Song> arrayList);
}
