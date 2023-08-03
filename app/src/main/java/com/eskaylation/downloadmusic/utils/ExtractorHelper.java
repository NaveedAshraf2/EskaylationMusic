package com.eskaylation.downloadmusic.utils;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import com.eskaylation.downloadmusic.R;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import org.schabi.newpipe.extractor.Info;
import org.schabi.newpipe.extractor.InfoItem.InfoType;
import org.schabi.newpipe.extractor.ListExtractor.InfoItemsPage;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.Page;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.search.SearchInfo;
import org.schabi.newpipe.extractor.stream.StreamInfo;

public final class ExtractorHelper {
    public static final InfoCache CACHE = InfoCache.getInstance();

    public static  void checkCache$6(Info info) throws Exception {
    }


    public static void checkServiceId(int i) {
        if (i == -1) {
            throw new IllegalArgumentException("serviceId is NO_SERVICE_ID");
        }
    }

    public static Single<SearchInfo> searchFor(int i, String str, List<String> list, String str2) {
        checkServiceId(i);
        return Single.fromCallable(new Callable( ) {


            public final Object call() throws ExtractionException, IOException {

                    return SearchInfo.getInfo(NewPipe.getService(i), NewPipe.getService
                            (i).getSearchQHFactory().fromQuery(str, list, str2));

            }
        });
    }

    public static Single<InfoItemsPage> getMoreSearchItems(int i, String str, List<String> list, String str2, Page page) {
        checkServiceId(i);
        Callable r0 = new Callable() {
     

            public final Object call() throws ExtractionException, IOException {

                    return SearchInfo.getMoreItems(NewPipe.getService(i),
                            NewPipe.getService(i).getSearchQHFactory().fromQuery(str, list, str2), page);

            }
        };
        return Single.fromCallable(r0);
    }

    public static Single<StreamInfo> getStreamInfo(int i, String str, boolean z) {
        checkServiceId(i);
        return checkCache(z, i, str, InfoType.STREAM, Single.fromCallable(new Callable() {
      
            public final Object call() throws ExtractionException, IOException {

                    return StreamInfo.getInfo(NewPipe.getService(i), str);

            }
        }));
    }

    public static <I extends Info> Single<I> checkCache(boolean z, int i, String str, InfoType infoType, Single<I> single) {
        checkServiceId(i);
        Single<I> doOnSuccess = single.doOnSuccess(ExtractorHelper0.INSTANCE);
        if (!z) {
            return Maybe.concat(loadFromCache(i, str, infoType), doOnSuccess.toMaybe()).firstElement().toSingle();
        }
        CACHE.removeInfo(i, str, infoType);
        return doOnSuccess;
    }

    public static <I extends Info> Maybe<I> loadFromCache(int i, String str, InfoType infoType) {
        checkServiceId(i);
        return Maybe.defer(new Callable() {
      
            public final Object call() throws Exception {
                return ExtractorHelper.loadFromCache$7(i, str, infoType);
            }
        });
    }

    public static  MaybeSource loadFromCache$7(int i, String str, InfoType infoType) throws Exception {
        Info fromKey = CACHE.getFromKey(i, str, infoType);
        if (fromKey != null) {
            return Maybe.just(fromKey);
        }
        return Maybe.empty();
    }
}
