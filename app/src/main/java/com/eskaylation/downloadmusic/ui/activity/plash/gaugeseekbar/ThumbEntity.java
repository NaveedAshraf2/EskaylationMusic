package com.eskaylation.downloadmusic.ui.activity.plash.gaugeseekbar;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;

import kotlin.jvm.internal.Intrinsics;


public final class ThumbEntity {
    public final PointF centerPosition;
    public float progress;
    public final float startAngle;

    public final Drawable thumbDrawable;
    public final float thumbRadius;
    public Drawable thumb ;

    public static final class Companion {
        public Companion() {
        }


    }

    static {
        new Companion();
    }

    public ThumbEntity(PointF pointF, float f, float f2, float f3, Drawable drawable) {
        Intrinsics.checkParameterIsNotNull(pointF, "centerPosition");
        Intrinsics.checkParameterIsNotNull(drawable, "thumbDrawable");
        this.centerPosition = pointF;
        this.progress = f;
        this.startAngle = f2;
        this.thumbRadius = f3;
        this.thumbDrawable = drawable;
        thumb = this.thumbDrawable;

        updatePosition(this.progress);
    }

    public final void draw(Canvas canvas, float f) {
        Intrinsics.checkParameterIsNotNull(canvas, "canvas");
        this.progress = f;
        updatePosition(f);
        Drawable drawable = this.thumb;
        if (drawable != null) {
            drawable.draw(canvas);
        }
    }

    public final void updatePosition(float f) {
        PointF pointF = this.centerPosition;
        float min = Math.min(pointF.x, pointF.y) - this.thumbRadius;
        float f2 = this.startAngle;
        double d = ((double) (f2 + ((((float) 360) - (((float) 2) * f2)) * f))) * 0.0174533d;
        double d2 = (double) min;
        double sin = ((double) this.centerPosition.x) - (Math.sin(d) * d2);
        double cos = (Math.cos(d) * d2) + ((double) this.centerPosition.y);
        Drawable drawable = this.thumb;
        if (drawable != null) {
            float f3 = this.thumbRadius;
            drawable.setBounds((int) (sin - ((double) f3)), (int) (cos - ((double) f3)), (int) (sin + ((double) f3)), (int) (cos + ((double) f3)));
        }
    }

    public final void setThumbDrawable(Drawable drawable) {
        Intrinsics.checkParameterIsNotNull(drawable, "thumb");
        this.thumb = drawable;
    }
}
