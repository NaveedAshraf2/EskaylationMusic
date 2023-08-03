package com.eskaylation.downloadmusic.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings.System;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TextView;
import java.util.Calendar;

@SuppressLint({"AppCompatCustomView"})
public class DigitalClock extends TextView {
    public Calendar mCalendar;
    public String mFormat;
    public FormatChangeObserver mFormatChangeObserver;
    public Handler mHandler;
    public Runnable mTicker;
    public boolean mTickerStopped = false;

    public class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        public void onChange(boolean z) {
            DigitalClock.this.setFormat();
        }
    }

    public DigitalClock(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initClock(context);
    }

    public final void initClock(Context context) {
        context.getResources();
        if (this.mCalendar == null) {
            this.mCalendar = Calendar.getInstance();
        }
        this.mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(System.CONTENT_URI, true, this.mFormatChangeObserver);
        setFormat();
    }

    public void onAttachedToWindow() {
        this.mTickerStopped = false;
        super.onAttachedToWindow();
        this.mHandler = new Handler();
        this.mTicker = new Runnable() {
            public void run() {
                if (!DigitalClock.this.mTickerStopped) {
                    DigitalClock.this.mCalendar.setTimeInMillis(java.lang.System.currentTimeMillis());
                    DigitalClock digitalClock = DigitalClock.this;
                    digitalClock.setText(DateFormat.format(digitalClock.mFormat, digitalClock.mCalendar));
                    DigitalClock.this.invalidate();
                    long uptimeMillis = SystemClock.uptimeMillis();
                    DigitalClock.this.mHandler.postAtTime(DigitalClock.this.mTicker, uptimeMillis + (1000 - (uptimeMillis % 1000)));
                }
            }
        };
        this.mTicker.run();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mTickerStopped = true;
    }

    private boolean get24HourMode() {
        return DateFormat.is24HourFormat(getContext());
    }

    public final void setFormat() {
        if (get24HourMode()) {
            this.mFormat = "k:mm:ss";
        } else {
            this.mFormat = "h:mm:ss aa";
        }
    }
}
