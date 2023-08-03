package com.eskaylation.downloadmusic.widget;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;

import com.eskaylation.downloadmusic.R;

public class MarqueeView extends View {
    public float mLength;
    public TextPaint mPaint;
    public int mSpacing;
    public String mText;

    public void setInterpolator(TimeInterpolator timeInterpolator) {
    }

    public MarqueeView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MarqueeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    public final void init(Context context, AttributeSet attributeSet) {
        this.mPaint = new TextPaint();
        this.mPaint.setAntiAlias(true);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.MarqueeView);
        obtainStyledAttributes.getDimensionPixelSize(0, 50);
        int color = obtainStyledAttributes.getColor(1, -16777216);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(3, 24);
        int color2 = obtainStyledAttributes.getColor(2, 0);
        float f = obtainStyledAttributes.getFloat(6, 0.0f);
        float f2 = obtainStyledAttributes.getFloat(4, 0.0f);
        float f3 = obtainStyledAttributes.getFloat(5, 0.0f);
        this.mSpacing = obtainStyledAttributes.getDimensionPixelOffset(7, 10);
        obtainStyledAttributes.recycle();
        this.mPaint.setTextSize((float) dimensionPixelSize);
        this.mPaint.setColor(color);
        this.mPaint.setShadowLayer(f, f2, f3, color2);
    }

    public void setText(String str) {
        this.mText = str;
        this.mLength = this.mPaint.measureText(this.mText) + ((float) this.mSpacing);
        LayoutParams layoutParams = (LayoutParams) getLayoutParams();
        layoutParams.rightMargin = -((int) this.mLength);
        setLayoutParams(layoutParams);
        invalidate();
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(this.mText)) {
            float f = 0.0f;
            if (this.mLength != 0.0f) {
                while (f < ((float) getWidth())) {
                    canvas.drawText(this.mText, f, ((float) getPaddingTop()) - this.mPaint.ascent(), this.mPaint);
                    f += this.mLength;
                }
            }
        }
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(MeasureSpec.getSize(i), ((int) (this.mPaint.descent() - this.mPaint.ascent())) + getPaddingBottom() + getPaddingTop());
    }
}
