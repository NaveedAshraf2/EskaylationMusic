package com.eskaylation.downloadmusic.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint({"AppCompatCustomView"})
public class TypeWriter extends TextView {
    public Runnable characterAdder = new Runnable() {
        public void run() {
            TypeWriter typeWriter = TypeWriter.this;
            typeWriter.setText(typeWriter.mText.subSequence(0, TypeWriter.this.mIndex = TypeWriter.this.mIndex + 1));
            if (TypeWriter.this.mIndex <= TypeWriter.this.mText.length()) {
                TypeWriter.this.mHandler.postDelayed(TypeWriter.this.characterAdder, TypeWriter.this.mDelay);
            }
        }
    };
    public long mDelay = 150;
    public Handler mHandler = new Handler();
    public int mIndex;
    public CharSequence mText;

    public TypeWriter(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setCharacterDelay(long j) {
        this.mDelay = j;
    }
}
