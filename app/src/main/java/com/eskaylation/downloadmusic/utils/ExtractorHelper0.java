package com.eskaylation.downloadmusic.utils;

import io.reactivex.functions.Consumer;
import org.schabi.newpipe.extractor.Info;


public final  class ExtractorHelper0 implements Consumer {
    public static final ExtractorHelper0 INSTANCE = new ExtractorHelper0();

    private ExtractorHelper0() {
    }

    public final void accept(Object obj) {
        try {
            ExtractorHelper.checkCache$6((Info) obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
