package com.eskaylation.downloadmusic.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigApp {
    public static ConfigApp sInstance;
    public SharedPreferences mPref;
    public static final String PURCHASE_KEY= "purchase";
 //   public  final String PURCHASE_KEY;
    Context contextt;
    public static final String PREF_FILE= "MyPref";

    public ConfigApp(Context context) {
        this.mPref = PreferenceManager.getDefaultSharedPreferences(context);
   //      PURCHASE_KEY= "purchase";
         contextt=context;
    }

    public static ConfigApp getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ConfigApp(context.getApplicationContext());
        }
        return sInstance;
    }

    public void putConfigApp(String str) {
        Editor edit = this.mPref.edit();
        edit.putString("PREF_CONFIG_APP", str);
        edit.apply();
    }

    public final String getConfigApp() {
        return this.mPref.getString("PREF_CONFIG_APP",
                "{\"version_code\":10,\"require_update\": true,\"hideRate\":false,\"data_type\":0}");
    }

    public final boolean isHideRate() {
        String configApp = getConfigApp();
        if (configApp.length() > 0) {
            try {
                return new JSONObject(configApp).getBoolean("hideRate");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public final boolean isShowAds() {
        if(getPurchaseValueFromPref())
        {
                return false;
            }
            else{
                return  true;
            }


    }

    private boolean getPurchaseValueFromPref(){
        return getPreferenceObject().getBoolean( PURCHASE_KEY,false);
    }

    private SharedPreferences getPreferenceObject() {
        return contextt.getSharedPreferences(PREF_FILE, 0);
    }

    public int getDataType() {
        String configApp = getConfigApp();
        if (configApp.length() > 0) {
            try {
                return new JSONObject(configApp).getInt("data_type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
}
