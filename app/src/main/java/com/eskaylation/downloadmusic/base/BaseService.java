package com.eskaylation.downloadmusic.base;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public abstract class BaseService extends Service {
    public BroadcastReceiver mSDCardStateChangeListener;

    public abstract void changeSDCard();

    public void onCreate() {
        super.onCreate();
        registerSDCardStateChangeListener();
    }

    public void onDestroy() {
        unregisterReceiver(this.mSDCardStateChangeListener);
        super.onDestroy();
    }

    public void registerSDCardStateChangeListener() {
        this.mSDCardStateChangeListener = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase("android.intent.action.MEDIA_UNMOUNTED")) {
                    BaseService.this.changeSDCard();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MEDIA_REMOVED");
        intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
        intentFilter.addAction("android.intent.action.MEDIA_SCANNER_FINISHED");
        intentFilter.addDataScheme("file");
        registerReceiver(this.mSDCardStateChangeListener, intentFilter);
    }
}
