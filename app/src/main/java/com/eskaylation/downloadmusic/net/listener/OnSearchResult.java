package com.eskaylation.downloadmusic.net.listener;

import com.eskaylation.downloadmusic.model.AudioExtract;
import java.util.ArrayList;
import org.schabi.newpipe.extractor.Page;

public interface OnSearchResult {
    void onSearchFailed(String str);

    void onSearchSuccessful(ArrayList<AudioExtract> arrayList, Page page);
}
