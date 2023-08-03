package com.eskaylation.downloadmusic.net;

import android.annotation.SuppressLint;
import android.content.Context;
import com.eskaylation.downloadmusic.listener.LoadMoreListener;
import com.eskaylation.downloadmusic.model.AudioExtract;
import com.eskaylation.downloadmusic.net.listener.OnSearchResult;
import com.eskaylation.downloadmusic.utils.AppConstants;

import com.eskaylation.downloadmusic.utils.ConfigApp;
import com.eskaylation.downloadmusic.utils.ExtractorHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.schabi.newpipe.extractor.InfoItem;
import org.schabi.newpipe.extractor.ListExtractor.InfoItemsPage;
import org.schabi.newpipe.extractor.Page;
import org.schabi.newpipe.extractor.search.SearchInfo;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;

public class SearchUtils {
    public String filter2 = "videos";
    public String filterSC = "tracks";
    public ArrayList<String> listPubBlock = new ArrayList<>();
    public LoadMoreListener loadMoreListener;
    public Context mContext;
    public String mQuery = "";
    public OnSearchResult onSearchResult;

    public static  void lambda$pareSearch$1(InfoItemsPage infoItemsPage, Throwable th) throws Exception {
    }

    public static  void lambda$parserSearchMore$2(InfoItemsPage infoItemsPage, Throwable th) throws Exception {
    }

    public static  void lambda$search$0(SearchInfo searchInfo, Throwable th) throws Exception {
    }

    public static  void lambda$searchMore$3(InfoItemsPage infoItemsPage, Throwable th) throws Exception {
    }

    public SearchUtils(OnSearchResult onSearchResult2, LoadMoreListener loadMoreListener2) {
        this.onSearchResult = onSearchResult2;
        this.loadMoreListener = loadMoreListener2;
        this.listPubBlock.clear();
        this.listPubBlock.addAll(AppConstants.listPubBlock());
    }

    public String getFilter(Context context) {
        return ConfigApp.getInstance(context).getDataType() == 0 ? this.filter2 : this.filterSC;
    }

    public int getTypeSearch(Context context) {
        return ConfigApp.getInstance(context).getDataType();
    }

    @SuppressLint({"CheckResult"})
    public void search(Context context, String str) {
        this.mContext = context;
        this.mQuery = str;
        ExtractorHelper.searchFor(getTypeSearch(context), str, Arrays.asList(new String[]{getFilter(context)}), getFilter(context)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnEvent(SearchUtils2.INSTANCE).subscribe(new Consumer() {
            public final void accept(Object obj) {
                SearchUtils.this.pareSearch((SearchInfo) obj);
            }
        }, new Consumer() {
            public final void accept(Object obj) {
                SearchUtils.this.onErrorSearch((Throwable) obj);
            }
        });
    }

    public boolean onErrorSearch(Throwable th) {
        this.onSearchResult.onSearchFailed("");
        return true;
    }

    public boolean onErrorSearchMore(Throwable th) {
        this.loadMoreListener.onLoadMoreFailed();
        return true;
    }

    @SuppressLint({"CheckResult"})
    public void pareSearch(SearchInfo searchInfo) {
        ArrayList arrayList = new ArrayList();
        Page nextPage = searchInfo.getNextPage();
        List relatedItems = searchInfo.getRelatedItems();
        for (int i = 0; i < relatedItems.size(); i++) {
            InfoItem infoItem = (InfoItem) relatedItems.get(i);
            StreamInfoItem streamInfoItem = (StreamInfoItem) infoItem;
            String url = streamInfoItem.getUrl();
            String name = streamInfoItem.getName();

            AudioExtract audioExtract2 = new AudioExtract(0, url, name, streamInfoItem.getDuration() * 1000, AppConstants.randomThumb(), infoItem.getThumbnailUrl());
            if (!this.listPubBlock.contains(streamInfoItem.getUploaderName().toUpperCase()) && !name.toUpperCase().contains("AE PAAPI") && !name.toUpperCase().contains("SHAHID") && !name.toUpperCase().contains("AAI PAAPI") && !name.toUpperCase().contains("KISMAT") && !name.toUpperCase().contains("NEERAJ SHRIDHAR") && !name.toUpperCase().contains("MOVE YOUR BODY NOW") && !name.toUpperCase().contains("AI PAPI") && !name.toUpperCase().contains("AI PAPPI")) {
                arrayList.add(audioExtract2);
            }
        }
        this.onSearchResult.onSearchSuccessful(arrayList, nextPage);
        ExtractorHelper.getMoreSearchItems(getTypeSearch(this.mContext), this.mQuery, Arrays.asList(new String[]{getFilter(this.mContext)}), getFilter(this.mContext), nextPage).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnEvent(SearchUtils0.INSTANCE).subscribe(new Consumer() {
            public final void accept(Object obj) {
                SearchUtils.this.parserSearchMoreSingle((InfoItemsPage) obj);
            }
        }, new Consumer() {
            public final void accept(Object obj) {
                SearchUtils.this.onErrorSearchMore((Throwable) obj);
            }
        });
    }

    public void parserSearchMoreSingle(InfoItemsPage infoItemsPage) {
        ArrayList arrayList = new ArrayList();
        Page nextPage = infoItemsPage.getNextPage();
        if (infoItemsPage.getItems() != null) {
            List items = infoItemsPage.getItems();
            int i = 0;
            while (i < items.size()) {
                InfoItem infoItem = (InfoItem) items.get(i);
                StreamInfoItem streamInfoItem = (StreamInfoItem) infoItem;
                String url = streamInfoItem.getUrl();
                String name = streamInfoItem.getName();
                List list = items;
                AudioExtract audioExtract2 = new AudioExtract(0, url, name, streamInfoItem.getDuration() * 1000, AppConstants.randomThumb(), infoItem.getThumbnailUrl());
                if (!this.listPubBlock.contains(streamInfoItem.getUploaderName().toUpperCase()) && !name.toUpperCase().contains("AE PAAPI") && !name.toUpperCase().contains("SHAHID") && !name.toUpperCase().contains("AAI PAAPI") && !name.toUpperCase().contains("KISMAT") && !name.toUpperCase().contains("NEERAJ SHRIDHAR") && !name.toUpperCase().contains("MOVE YOUR BODY NOW") && !name.toUpperCase().contains("AI PAPI") && !name.toUpperCase().contains("AI PAPPI")) {
                    arrayList.add(audioExtract2);
                }
                i++;
                items = list;
            }
            this.loadMoreListener.onLoadMoreSuccess(arrayList, nextPage);
        }
    }

    @SuppressLint({"CheckResult"})
    public void parserSearchMore(InfoItemsPage infoItemsPage) {
        ArrayList arrayList = new ArrayList();
        Page nextPage = infoItemsPage.getNextPage();
        if (infoItemsPage.getItems() != null) {
            List items = infoItemsPage.getItems();
            for (int i = 0; i < items.size(); i++) {
                InfoItem infoItem = (InfoItem) items.get(i);
                StreamInfoItem streamInfoItem = (StreamInfoItem) infoItem;
                String url = streamInfoItem.getUrl();
                String name = streamInfoItem.getName();

                AudioExtract audioExtract2 = new AudioExtract(0, url, name, streamInfoItem.getDuration() * 1000, AppConstants.randomThumb(), infoItem.getThumbnailUrl());
                if (!this.listPubBlock.contains(streamInfoItem.getUploaderName().toUpperCase()) && !name.toUpperCase().contains("AE PAAPI") && !name.toUpperCase().contains("SHAHID") && !name.toUpperCase().contains("AAI PAAPI") && !name.toUpperCase().contains("KISMAT") && !name.toUpperCase().contains("NEERAJ SHRIDHAR") && !name.toUpperCase().contains("MOVE YOUR BODY NOW") && !name.toUpperCase().contains("AI PAPI") && !name.toUpperCase().contains("AI PAPPI")) {
                    arrayList.add(audioExtract2);
                }
            }
            this.loadMoreListener.onLoadMoreSuccess(arrayList, nextPage);
            ExtractorHelper.getMoreSearchItems(getTypeSearch(this.mContext), this.mQuery, Arrays.asList(new String[]{getFilter(this.mContext)}), getFilter(this.mContext), nextPage).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnEvent(SearchUtils1.INSTANCE).subscribe(new Consumer() {
                public final void accept(Object obj) {
                    SearchUtils.this.parserSearchMoreSingle((InfoItemsPage) obj);
                }
            }, new Consumer() {
                public final void accept(Object obj) {
                    SearchUtils.this.onErrorSearchMore((Throwable) obj);
                }
            });
        }
    }

    @SuppressLint({"CheckResult"})
    public void searchMore(Context context, String str, Page page) {
        this.mContext = context;
        ExtractorHelper.getMoreSearchItems(getTypeSearch(context), str, Arrays.asList(new String[]{getFilter(this.mContext)}), getFilter(this.mContext), page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnEvent(SearchUtils3.INSTANCE).subscribe(new Consumer() {
            public final void accept(Object obj) {
                SearchUtils.this.parserSearchMore((InfoItemsPage) obj);
            }
        }, new Consumer() {
            public final void accept(Object obj) {
                SearchUtils.this.onErrorSearchMore((Throwable) obj);
            }
        });
    }
}
