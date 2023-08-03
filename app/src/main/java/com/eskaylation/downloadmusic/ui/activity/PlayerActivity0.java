package com.eskaylation.downloadmusic.ui.activity;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public final  class PlayerActivity0 implements OnTouchListener {
    public static final PlayerActivity0 INSTANCE = new PlayerActivity0();

    private PlayerActivity0() {
    }

    public final boolean onTouch(View view, MotionEvent motionEvent) {
        return PlayerActivity.lambda$init$0(view, motionEvent);
    }
}
