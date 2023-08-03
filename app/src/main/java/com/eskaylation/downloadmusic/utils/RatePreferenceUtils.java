package com.eskaylation.downloadmusic.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import com.eskaylation.downloadmusic.R;
public class RatePreferenceUtils {
    public static String PREF_DONT_SHOW_RATE = "PREF_DONT_SHOW_RATE_gaudio";
    public static String PREF_TIME_COUNT_START_APP = "PREF_TIME_COUNT_START_APP_gaudio";
    public static String PREF_TIME_LATTER_4_HOUR = "PREF_TIME_LATTER_4_HOUR_gaudio";
    public static RatePreferenceUtils sInstance;
    public SharedPreferences mPref;

    public RatePreferenceUtils(Context context) {
        this.mPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static RatePreferenceUtils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RatePreferenceUtils(context.getApplicationContext());
        }
        return sInstance;
    }

    public void putBoolean(String str, boolean z) {
        Editor edit = this.mPref.edit();
        edit.putBoolean(str, z);
        edit.apply();
    }

    public final boolean getBoolean(String str) {
        return this.mPref.getBoolean(str, false);
    }

    public void putTime(String str, long j) {
        Editor edit = this.mPref.edit();
        edit.putLong(str, j);
        edit.apply();
    }

    public void setCount() {
        Editor edit = this.mPref.edit();
        edit.putInt(PREF_TIME_COUNT_START_APP, getCount() + 1);
        edit.apply();
    }

    public final int getCount() {
        return this.mPref.getInt(PREF_TIME_COUNT_START_APP, 0);
    }
}
