package com.eskaylation.downloadmusic.ui.activity.plash.gaugeseekbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.SweepGradient;


import com.eskaylation.downloadmusic.R;

import kotlin.jvm.internal.Intrinsics;


public final class TrackDrawable extends DrawableEntity {
    public Context context;
    public final int[] gradientArray;
    public final float[] gradientPositionsArray;
    public final float margin;
    public final Paint progressPaint;
    public final float radiusPx;
    public final float startAngle;
    public final Paint textPain;
    public final float trackWidthPx;

    public int getOpacity() {
        return -3;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public TrackDrawable(PointF pointF, Context context2, float f, float f2, int[] iArr, float f3, float f4, float[] fArr) {
        super(pointF);
        Intrinsics.checkParameterIsNotNull(pointF, "position");
        Intrinsics.checkParameterIsNotNull(context2, "context");
        Intrinsics.checkParameterIsNotNull(iArr, "gradientArray");

        this.context = context2;
        this.radiusPx = f;
        this.margin = f2;
        this.gradientArray = iArr;
        this.startAngle = f3;
        this.trackWidthPx = f4;
        if (fArr == null) {
            int length = this.gradientArray.length;
            fArr = new float[length];
            for (int i = 0; i < length; i++) {
                fArr[i] = ((float) i) / ((float) this.gradientArray.length);
            }
        }
        this.gradientPositionsArray = fArr;
        if (this.gradientArray.length == this.gradientPositionsArray.length) {
            Paint paint = new Paint();
            paint.setStrokeWidth(this.trackWidthPx);
            paint.setAntiAlias(true);
            paint.setStrokeCap(Cap.ROUND);
            paint.setStyle(Style.STROKE);
            paint.setShader(createSweepGradient());
            this.progressPaint = paint;
            Paint paint2 = new Paint();
            paint2.setColor(-1);
            paint2.setStyle(Style.FILL);
            this.textPain = paint2;
            return;
        }
        throw new IllegalStateException("gradientArray and gradientPositionsArray sizes must be equal.");
    }

    public  TrackDrawable(PointF pointF, Context context2, float f, float f2, int[] iArr, float f3, float f4, float[] fArr, int i) {
        this(pointF, context2, f, f2, iArr, f3, f4, (i & 128) != 0 ? null : fArr);
    }

    public final SweepGradient createSweepGradient() {
        String str = "#1e2747";
        SweepGradient sweepGradient = new SweepGradient(getCenterPosition().x, getCenterPosition().y, Color.parseColor(str), Color.parseColor(str));
        Matrix matrix = new Matrix();
        matrix.preRotate((this.startAngle + 90.0f) - ((float) 5), getCenterPosition().x, getCenterPosition().y);
        sweepGradient.setLocalMatrix(matrix);
        return sweepGradient;
    }

    public void draw(Canvas canvas) {
        Intrinsics.checkParameterIsNotNull(canvas, "canvas");
        Canvas canvas2 = canvas;
        canvas2.drawArc(new RectF((getCenterPosition().x - this.radiusPx) + this.margin, (getCenterPosition().y - this.radiusPx) + this.margin, (getCenterPosition().x + this.radiusPx) - this.margin, (getCenterPosition().y + this.radiusPx) - this.margin), this.startAngle + 90.0f, ((float) 360) - (this.startAngle * ((float) 2)), false, this.progressPaint);
        this.textPain.setTextSize((float) this.context.getResources().getDimensionPixelSize(R.dimen.myFontSize));
        float f = (float) 3;
        canvas.drawText("MIN", (getCenterPosition().x / f) + this.margin, getCenterPosition().y + this.radiusPx, this.textPain);
        canvas.drawText("MAX", getCenterPosition().x + (getCenterPosition().x / f), getCenterPosition().y + this.radiusPx, this.textPain);
    }
}
