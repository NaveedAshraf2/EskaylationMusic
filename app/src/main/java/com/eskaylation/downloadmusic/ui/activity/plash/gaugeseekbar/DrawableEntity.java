package com.eskaylation.downloadmusic.ui.activity.plash.gaugeseekbar;

import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import kotlin.jvm.internal.Intrinsics;

public abstract class DrawableEntity extends Drawable {
    public PointF centerPosition;

    public DrawableEntity(PointF pointF) {
        Intrinsics.checkParameterIsNotNull(pointF, "centerPosition");
        this.centerPosition = pointF;
    }

    public final PointF getCenterPosition() {
        return this.centerPosition;
    }
}
