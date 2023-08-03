package com.eskaylation.downloadmusic.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.eskaylation.downloadmusic.bus.NetworkChange;
import org.greenrobot.eventbus.EventBus;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @SuppressLint({"UnsafeProtectedBroadcastReceiver"})
    public void onReceive(Context context, Intent intent) {
        try {
            if (isOnline(context)) {
                EventBus.getDefault().postSticky(new NetworkChange(true));
            } else {
                EventBus.getDefault().postSticky(new NetworkChange(false));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public final boolean isOnline(Context context) {
        boolean z = false;
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                z = true;
            }
            return z;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
