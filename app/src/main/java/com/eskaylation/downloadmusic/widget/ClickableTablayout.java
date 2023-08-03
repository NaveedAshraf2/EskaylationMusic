package com.eskaylation.downloadmusic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.google.android.material.tabs.TabLayout;

public class ClickableTablayout extends TabLayout {
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return true;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    public ClickableTablayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ClickableTablayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
