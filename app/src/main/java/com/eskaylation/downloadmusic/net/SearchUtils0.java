package com.eskaylation.downloadmusic.net;

import io.reactivex.functions.BiConsumer;
import org.schabi.newpipe.extractor.ListExtractor.InfoItemsPage;


public final  class SearchUtils0 implements BiConsumer {
    public static final  SearchUtils0 INSTANCE = new SearchUtils0();

    private  SearchUtils0() {
    }

    public final void accept(Object obj, Object obj2) {
        try {
            SearchUtils.lambda$pareSearch$1((InfoItemsPage) obj, (Throwable) obj2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
