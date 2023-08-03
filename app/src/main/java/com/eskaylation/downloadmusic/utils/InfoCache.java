package com.eskaylation.downloadmusic.utils;

import android.util.LruCache;
import org.schabi.newpipe.extractor.Info;
import org.schabi.newpipe.extractor.InfoItem.InfoType;
import com.eskaylation.downloadmusic.R;
public class InfoCache {
    public static final InfoCache INSTANCE = new InfoCache();
    public static final LruCache<String, CacheData> LRU_CACHE = new LruCache<>(60);

    public static final class CacheData {
        public long expireTimestamp;
        public Info info;

        public final boolean isExpired() {
            return System.currentTimeMillis() > this.expireTimestamp;
        }
    }

    public static InfoCache getInstance() {
        return INSTANCE;
    }

    public static String keyOf(int i, String str, InfoType infoType) {
        StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(str);
        sb.append(infoType.toString());
        return sb.toString();
    }

    public static Info getInfo(String str) {
        CacheData cacheData = (CacheData) LRU_CACHE.get(str);
        if (cacheData == null) {
            return null;
        }
        if (!cacheData.isExpired()) {
            return cacheData.info;
        }
        LRU_CACHE.remove(str);
        return null;
    }

    public Info getFromKey(int i, String str, InfoType infoType) {
        Info info;
        synchronized (LRU_CACHE) {
            info = getInfo(keyOf(i, str, infoType));
        }
        return info;
    }

    public void removeInfo(int i, String str, InfoType infoType) {
        synchronized (LRU_CACHE) {
            LRU_CACHE.remove(keyOf(i, str, infoType));
        }
    }
}
