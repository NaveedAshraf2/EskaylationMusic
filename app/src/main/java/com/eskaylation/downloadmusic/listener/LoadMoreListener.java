package com.eskaylation.downloadmusic.listener;

import com.eskaylation.downloadmusic.model.AudioExtract;
import java.util.ArrayList;
import org.schabi.newpipe.extractor.Page;

public interface LoadMoreListener {
    void onLoadMoreFailed();

    void onLoadMoreSuccess(ArrayList<AudioExtract> arrayList, Page page);
}
