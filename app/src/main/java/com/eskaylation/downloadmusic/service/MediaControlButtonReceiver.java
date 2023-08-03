package com.eskaylation.downloadmusic.service;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.KeyEvent;
import com.eskaylation.downloadmusic.receiver.BroadcastControl;

public class MediaControlButtonReceiver extends BroadcastReceiver {
    public static int mClickCounter;
    @SuppressLint({"HandlerLeak"})
    public static Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 2) {
                int i = message.arg1;
                String str = i != 1 ? i != 2 ? i != 3 ? null : "com.eskaylation.downloadmusic.action.prive" : "com.eskaylation.downloadmusic.action.next" : "com.eskaylation.downloadmusic.action.playpause";
                if (str != null) {
                    MediaControlButtonReceiver.startServices((Context) message.obj, str);
                }
            }
            MediaControlButtonReceiver.releaseWakeLockIfHandlerIdle();
        }
    };
    public static long mLastClickTime;
    public static WakeLock mWakeLock;

    public static void releaseWakeLockIfHandlerIdle() {
        if (!mHandler.hasMessages(2)) {
            WakeLock wakeLock = mWakeLock;
            if (wakeLock != null) {
                wakeLock.release();
                mWakeLock = null;
            }
        }
    }

    @SuppressLint({"InvalidWakeLockTag"})
    public static void acquireWakeLockAndSendMessage(Context context, Message message, long j) {
        if (mWakeLock == null) {
            mWakeLock = ((PowerManager) context.getApplicationContext().getSystemService("power")).newWakeLock(1, "MusicPlayer headset button");
            mWakeLock.setReferenceCounted(false);
        }
        mWakeLock.acquire(10000);
        mHandler.sendMessageDelayed(message, j);
    }

    public static void startServices(Context context, String str) {
        Intent intent = new Intent(context, BroadcastControl.class);
        intent.setAction(str);
        context.sendBroadcast(intent);
    }


    public void onReceive(Context context, Intent intent) {
        String str = null;
        String action = intent.getAction();
        if (intent.getAction() != null && action.equals("android.intent.action.MEDIA_BUTTON")) {
            KeyEvent keyEvent = (KeyEvent) intent.getParcelableExtra("android.intent.extra.KEY_EVENT");
            if (keyEvent != null) {
                int keyCode = keyEvent.getKeyCode();
                int action2 = keyEvent.getAction();
                long eventTime = keyEvent.getEventTime();
                if (keyCode != 79) {
                    if (keyCode == 126) {
                        str = "com.eskaylation.downloadmusic.ACTION.PLAY";
                    } else if (keyCode != 127) {
                        switch (keyCode) {
                            case 85:
                                break;
                            case 86:
                                str = "com.eskaylation.downloadmusic.action.stop_music";
                                break;
                            case 87:
                                str = "com.eskaylation.downloadmusic.action.next";
                                break;
                            case 88:
                                str = "com.eskaylation.downloadmusic.action.prive";
                                break;
                            default:
                                str = null;
                                break;
                        }
                    } else {
                        str = "com.eskaylation.downloadmusic.ACTION.PAUSE";
                    }
                    if (str != null && action2 == 0 && keyEvent.getRepeatCount() == 0) {
                        if (keyCode != 79) {
                            long j = 500;
                            if (eventTime - mLastClickTime >= 500) {
                                mClickCounter = 0;
                            }
                            mClickCounter++;
                            mHandler.removeMessages(2);
                            Message obtainMessage = mHandler.obtainMessage(2, mClickCounter, 0, context);
                            if (mClickCounter >= 3) {
                                j = 0;
                            }
                            if (mClickCounter >= 3) {
                                mClickCounter = 0;
                            }
                            mLastClickTime = eventTime;
                            acquireWakeLockAndSendMessage(context, obtainMessage, j);
                        } else {
                            startServices(context, str);
                        }
                    }
                }
                str = "com.eskaylation.downloadmusic.action.playpause";
                if (keyCode != 79) {
                }
            }
        }
    }
}
