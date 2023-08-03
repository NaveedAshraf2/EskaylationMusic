package com.eskaylation.downloadmusic.listener;

import com.eskaylation.downloadmusic.model.AudioExtract;
import java.util.ArrayList;

public interface GenerateUrlListener {
    void onGenerateUrlSongDone(String str, ArrayList<AudioExtract> arrayList);

    void onGenerateUrlSongFailed();
}
