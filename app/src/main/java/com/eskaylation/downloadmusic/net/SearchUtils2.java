package com.eskaylation.downloadmusic.net;

import io.reactivex.functions.BiConsumer;
import org.schabi.newpipe.extractor.search.SearchInfo;


public final  class SearchUtils2 implements BiConsumer {
    public static final  SearchUtils2 INSTANCE = new SearchUtils2();

    private  SearchUtils2() {
    }

    public final void accept(Object obj, Object obj2) {
        try {
            SearchUtils.lambda$search$0((SearchInfo) obj, (Throwable) obj2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
