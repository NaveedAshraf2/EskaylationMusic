package com.eskaylation.downloadmusic.net;

import io.reactivex.functions.BiConsumer;
import org.schabi.newpipe.extractor.ListExtractor.InfoItemsPage;


public final  class SearchUtils3 implements BiConsumer {
    public static final  SearchUtils3 INSTANCE = new SearchUtils3();

    private  SearchUtils3() {
    }

    public final void accept(Object obj, Object obj2) {
        try {
            SearchUtils.lambda$searchMore$3((InfoItemsPage) obj, (Throwable) obj2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
