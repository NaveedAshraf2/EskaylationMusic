package com.eskaylation.downloadmusic.ui.activity.plash.gaugeseekbar;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import kotlin.jvm.internal.Intrinsics;


public final class ThumbDrawable extends Drawable {
    public final Paint thumbInnerPaint;
    public final Paint thumbOuterPaint;
    public final Paint whitePaint;

    public int getOpacity() {
        return -3;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public ThumbDrawable(int i) {
        Paint paint = new Paint();
        paint.setColor(-1);
        paint.setAlpha(255);
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
        this.whitePaint = paint;
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setColor(i);
        paint2.setAlpha(102);
        this.thumbOuterPaint = paint2;
        Paint paint3 = new Paint();
        paint3.setAntiAlias(true);
        paint3.setColor(i);
        this.thumbInnerPaint = paint3;
    }

    public void draw(Canvas canvas) {
        Intrinsics.checkParameterIsNotNull(canvas, "canvas");
        float exactCenterX = getBounds().exactCenterX();
        float exactCenterY = getBounds().exactCenterY();
        float f = exactCenterX - ((float) getBounds().left);
        canvas.drawCircle(exactCenterX, exactCenterY, f, this.thumbOuterPaint);
        canvas.drawCircle(exactCenterX, exactCenterY, f / 2.0f, this.thumbInnerPaint);
        canvas.drawCircle(exactCenterX, exactCenterY, 3.0f, this.whitePaint);
    }
}
