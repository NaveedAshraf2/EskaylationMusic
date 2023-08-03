package com.eskaylation.downloadmusic.ui.activity.plash.gaugeseekbar;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import kotlin.jvm.internal.Intrinsics;

public final class ProgressDrawable extends DrawableEntity {
    public int[] gradientArray;
    public final float[] gradientPositionsArray;
    public final float margin;
    public float progress;
    public final Paint progressPaint;
    public final float radiusPx;
    public final float startAngle;
    public final float trackWidthPx;

    public int getOpacity() {
        return -3;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public ProgressDrawable(PointF pointF, float f, float f2, float f3, int[] iArr, float f4, float f5, float[] fArr) {
        super(pointF);
        float[] fArr2;
        Intrinsics.checkParameterIsNotNull(pointF, "position");
        Intrinsics.checkParameterIsNotNull(iArr, "gradientArray");

        this.progress = f;
        this.radiusPx = f2;
        this.margin = f3;
        this.gradientArray = iArr;
        this.startAngle = f4;
        this.trackWidthPx = f5;
        if (fArr != null) {
            fArr2 = getGradientPositions(fArr);
        } else {
            int length = this.gradientArray.length;
            float[] fArr3 = new float[length];
            for (int i = 0; i < length; i++) {
                fArr3[i] = ((float) i) / ((float) (this.gradientArray.length - 1));
            }
            fArr2 = getGradientPositions(fArr3);
        }
        this.gradientPositionsArray = fArr2;
        if (this.gradientArray.length == this.gradientPositionsArray.length) {
            Paint paint = new Paint();
            paint.setStrokeWidth(this.trackWidthPx);
            paint.setAntiAlias(true);
            paint.setStrokeCap(Cap.ROUND);
            paint.setStyle(Style.STROKE);
            int[] iArr2 = this.gradientArray;
            if (iArr2.length > 1) {
                paint.setShader(createSweepGradient());
            } else {
                paint.setColor(iArr2[0]);
            }
            this.progressPaint = paint;
            return;
        }
        throw new IllegalStateException("gradientArray and gradientPositionsArray sizes must be equal.");
    }

    public final SweepGradient createSweepGradient() {
        SweepGradient sweepGradient = new SweepGradient(getCenterPosition().x, getCenterPosition().y, this.gradientArray, this.gradientPositionsArray);
        Matrix matrix = new Matrix();
        matrix.preRotate((this.startAngle + 90.0f) - ((float) Math.toDegrees(((double) 2) * Math.asin((double) (this.trackWidthPx / this.radiusPx)))), getCenterPosition().x, getCenterPosition().y);
        sweepGradient.setLocalMatrix(matrix);
        return sweepGradient;
    }

    public final float[] getGradientPositions(float[] fArr) {
        float f = this.startAngle / 360.0f;
        float f2 = 1.0f - (((float) 2) * f);
        int length = fArr.length;
        float[] fArr2 = new float[length];
        for (int i = 0; i < length; i++) {
            fArr2[i] = (fArr[i] * f2) + f;
        }
        return fArr2;
    }

    public final void draw(Canvas canvas, float f) {
        Intrinsics.checkParameterIsNotNull(canvas, "canvas");
        this.progress = f;
        draw(canvas);
    }

    public void draw(Canvas canvas) {
        Intrinsics.checkParameterIsNotNull(canvas, "canvas");
        float f = (((float) 360) - (this.startAngle * ((float) 2))) * this.progress;
        if (f > ((float) 0)) {
            canvas.drawArc(new RectF((getCenterPosition().x - this.radiusPx) + this.margin, (getCenterPosition().y - this.radiusPx) + this.margin, (getCenterPosition().x + this.radiusPx) - this.margin, (getCenterPosition().y + this.radiusPx) - this.margin), this.startAngle + 90.0f, f, false, this.progressPaint);
        }
    }
}
