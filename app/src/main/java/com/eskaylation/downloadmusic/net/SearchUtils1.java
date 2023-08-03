package com.eskaylation.downloadmusic.net;

import io.reactivex.functions.BiConsumer;
import org.schabi.newpipe.extractor.ListExtractor.InfoItemsPage;


public final  class SearchUtils1 implements BiConsumer {
    public static final  SearchUtils1 INSTANCE = new SearchUtils1();

    private  SearchUtils1() {
    }

    public final void accept(Object obj, Object obj2) {
        try {
            SearchUtils.lambda$parserSearchMore$2((InfoItemsPage) obj, (Throwable) obj2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
