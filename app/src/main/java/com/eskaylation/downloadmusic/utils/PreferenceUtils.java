package com.eskaylation.downloadmusic.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import com.eskaylation.downloadmusic.R;
public class PreferenceUtils {
    public static PreferenceUtils sInstance;
    public SharedPreferences mPref;

    public PreferenceUtils(Context context) {
        this.mPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        this.mPref.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public static PreferenceUtils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferenceUtils(context.getApplicationContext());
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

    public void putInt(String str, int i) {
        Editor edit = this.mPref.edit();
        edit.putInt(str, i);
        edit.apply();
    }

    public final int getInt(String str) {
        return this.mPref.getInt(str, 0);
    }

    public final int getInt(String str, int i) {
        return this.mPref.getInt(str, i);
    }

    public void setIDPlaylist() {
        int iDPlaylist = getIDPlaylist() + 1;
        Editor edit = this.mPref.edit();
        edit.putInt("PREF_ID_PLAYLIST", iDPlaylist);
        edit.apply();
    }

    public final int getIDPlaylist() {
        return this.mPref.getInt("PREF_ID_PLAYLIST", 0);
    }

    public final int getSpinerPosition() {
        return this.mPref.getInt("com.eskaylation.downloadmusic.SPINER_POSITION", 0);
    }

    public final int getBBSlider() {
        return this.mPref.getInt("com.eskaylation.downloadmusic.BBSLIDER", 0);
    }

    public final int getVirSlider() {
        return this.mPref.getInt("com.eskaylation.downloadmusic.VIRSLIDER", 0);
    }

    public void setTimmer(int i) {
        Editor edit = this.mPref.edit();
        edit.putInt("com.eskaylation.downloadmusic.TIMER", i);
        edit.apply();
    }

    public void setIsTimerOff(boolean z) {
        Editor edit = this.mPref.edit();
        edit.putBoolean("com.eskaylation.downloadmusic.PREF_TIMEOFF", z);
        edit.apply();
    }

    public final int getThemesPosition() {
        return this.mPref.getInt("THEME_POSITION", 0);
    }

    public void setThemesPosition(int i) {
        Editor edit = this.mPref.edit();
        edit.putInt("THEME_POSITION", i);
        edit.apply();
    }

    public void setShowAds(boolean z) {
        Editor edit = this.mPref.edit();
        edit.putBoolean("PREF_SHOWADS_INTRO", z);
        edit.apply();
    }
}
