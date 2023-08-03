package com.eskaylation.downloadmusic.model;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AdsApplication extends MultiDexApplication {

    public static AppOpenManager appOpenManager;

    private static AdsApplication instance;

    public static AdsApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null)
            instance = this;
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                appOpenManager = new AppOpenManager(AdsApplication.this);
            }
        });



    }
}
