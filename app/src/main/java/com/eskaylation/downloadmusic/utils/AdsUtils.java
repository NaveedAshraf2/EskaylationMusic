package com.eskaylation.downloadmusic.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.model.AdsManager;

import static android.content.ContentValues.TAG;

public class AdsUtils {
    public static AdsUtils adsUtils;

    public boolean isReload = false;
    Context context1;

    public static AdsUtils getSharedInstance() {
        if (adsUtils == null) {
            adsUtils = new AdsUtils();
        }
        return adsUtils;
    }

    public void setupInterstitialAd(Context context) {
   context1=context;

    }


}
