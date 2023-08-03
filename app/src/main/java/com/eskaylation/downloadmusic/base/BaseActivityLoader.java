package com.eskaylation.downloadmusic.base;

import android.app.Activity;
import android.widget.FrameLayout;

import com.eskaylation.downloadmusic.bus.SDCardChange;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseActivityLoader extends BaseActivity {
    public abstract void loader();

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void reloadData(SDCardChange sDCardChange) {loader();}

}
