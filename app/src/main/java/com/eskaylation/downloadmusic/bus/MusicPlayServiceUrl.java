package com.eskaylation.downloadmusic.bus;

import com.eskaylation.downloadmusic.model.AudioExtract;
import java.util.ArrayList;

public class MusicPlayServiceUrl {
    public ArrayList<AudioExtract> lstRecomend;
    public String url;

    public MusicPlayServiceUrl(String str, String str2, ArrayList<AudioExtract> arrayList) {
        this.url = str2;
        this.lstRecomend = arrayList;
    }
}
