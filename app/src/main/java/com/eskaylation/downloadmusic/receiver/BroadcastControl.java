package com.eskaylation.downloadmusic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.eskaylation.downloadmusic.bus.Control;
import org.greenrobot.eventbus.EventBus;

public class BroadcastControl extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String str = "com.eskaylation.downloadmusic.action.next";
        if (intent.getAction().equals(str)) {
            EventBus.getDefault().postSticky(new Control(str));
            return;
        }
        String str2 = "com.eskaylation.downloadmusic.action.playpause";
        if (intent.getAction().equals(str2)) {
            EventBus.getDefault().postSticky(new Control(str2));
            return;
        }
        String str3 = "com.eskaylation.downloadmusic.action.prive";
        if (intent.getAction().equals(str3)) {
            EventBus.getDefault().postSticky(new Control(str3));
            return;
        }
        String action = intent.getAction();
        String str4 = "com.eskaylation.downloadmusic.action.stop_music";
        if (action.equals(str4)) {
            EventBus.getDefault().postSticky(new Control(str4));
        }
    }
}
