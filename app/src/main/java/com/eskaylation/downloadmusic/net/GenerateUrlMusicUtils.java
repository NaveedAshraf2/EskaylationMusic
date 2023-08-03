package com.eskaylation.downloadmusic.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.annimon.stream.Stream;
import com.eskaylation.downloadmusic.listener.GenerateUrlListener;
import com.eskaylation.downloadmusic.model.AudioExtract;
import com.eskaylation.downloadmusic.utils.AppConstants;
import com.eskaylation.downloadmusic.utils.ConfigApp;
import com.eskaylation.downloadmusic.utils.ExtractorHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.stream.AudioStream;
import org.schabi.newpipe.extractor.stream.StreamInfo;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;

public class GenerateUrlMusicUtils {
    public Context context;
    public GenerateUrlListener listener;

    public static  void lambda$getUrlAudio$0(StreamInfo streamInfo, Throwable th) throws Exception {
    }

    public GenerateUrlMusicUtils(Context context2, GenerateUrlListener generateUrlListener) {
        this.context = context2;
        this.listener = generateUrlListener;
    }

    public int getTypeSearch() {
        return ConfigApp.getInstance(this.context).getDataType();
    }

    @SuppressLint({"CheckResult"})
    public void getUrlAudio(AudioExtract audioExtract) {
        ExtractorHelper.getStreamInfo(getTypeSearch(), audioExtract.urlVideo,
                false).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnEvent(GenerateUrlMusicUtils0.INSTANCE).subscribe(new Consumer() {
            public final void accept(Object obj) {
                GenerateUrlMusicUtils.this.pareRecomend((StreamInfo) obj);
            }
        }, new Consumer() {
            public final void accept(Object obj) {
                GenerateUrlMusicUtils.this.
                        handleError((Throwable) obj);
            }
        });
    }

    public final void pareRecomend(StreamInfo streamInfo) {
        if (streamInfo.getAudioStreams() == null) {
            handleError(new Throwable());
        } else if (!streamInfo.getAudioStreams().isEmpty()) {
            List relatedStreams = Stream.of(streamInfo.getRelatedItems())
                    .filter(infoItem -> infoItem.getInfoType() == InfoItem.InfoType.STREAM)
                    .toList();
            int i = 0;
            String url = ((AudioStream) streamInfo.getAudioStreams().get(0)).getUrl();
            ArrayList arrayList = new ArrayList();
            while (i < relatedStreams.size()) {
                StreamInfoItem streamInfoItem = (StreamInfoItem) relatedStreams.get(i);
                long duration = streamInfoItem.getDuration() * 1000;
                List list = relatedStreams;

                AudioExtract audioExtract2 = new AudioExtract(0, streamInfoItem.getUrl(), streamInfoItem.getName(), duration, AppConstants.randomThumb(), streamInfoItem.getThumbnailUrl());
                if (duration > 0) {
                    arrayList.add(audioExtract2);
                }
                i++;
                relatedStreams = list;
            }
            this.listener.onGenerateUrlSongDone(url, arrayList);
        } else {
            handleError(new Throwable());
        }
    }

    public boolean handleError(Throwable th) {
        Log.e("PiPe Error", th.toString());
        this.listener.onGenerateUrlSongFailed();
        return true;
    }
}
