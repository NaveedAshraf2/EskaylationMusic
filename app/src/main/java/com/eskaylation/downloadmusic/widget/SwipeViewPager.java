package com.eskaylation.downloadmusic.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

import com.eskaylation.downloadmusic.R;

public class SwipeViewPager extends ViewPager {
    public boolean mSwipeEnable;

    public SwipeViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.SwipeViewPager);
        try {
            this.mSwipeEnable = obtainStyledAttributes.getBoolean(0, true);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.mSwipeEnable && super.onTouchEvent(motionEvent);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.mSwipeEnable && super.onInterceptTouchEvent(motionEvent);
    }

    public void setSwipeEnable(boolean z) {
        this.mSwipeEnable = z;
    }
}
