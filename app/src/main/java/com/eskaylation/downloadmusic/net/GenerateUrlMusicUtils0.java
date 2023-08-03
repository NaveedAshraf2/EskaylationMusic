package com.eskaylation.downloadmusic.net;

import io.reactivex.functions.BiConsumer;
import org.schabi.newpipe.extractor.stream.StreamInfo;


public final  class GenerateUrlMusicUtils0 implements BiConsumer {
    public static final  GenerateUrlMusicUtils0 INSTANCE = new GenerateUrlMusicUtils0();

    private  GenerateUrlMusicUtils0() {
    }

    public final void accept(Object obj, Object obj2) {
        try {
            GenerateUrlMusicUtils.lambda$getUrlAudio$0((StreamInfo) obj, (Throwable) obj2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
