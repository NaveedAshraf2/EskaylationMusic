package com.eskaylation.downloadmusic.utils;

import android.text.TextUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CookieUtils {
    public static String concatCookies(Collection<String> collection) {
        HashSet hashSet = new HashSet();
        for (String splitCookies : collection) {
            hashSet.addAll(splitCookies(splitCookies));
        }
        return TextUtils.join("; ", hashSet).trim();
    }

    public static Set<String> splitCookies(String str) {
        return new HashSet(Arrays.asList(str.split("; *")));
    }
}
