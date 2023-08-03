package com.eskaylation.downloadmusic.widget.smarttablayout;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public abstract class SmartTabIndicationInterpolator {
    public static final SmartTabIndicationInterpolator LINEAR = new LinearIndicationInterpolator();
    public static final SmartTabIndicationInterpolator SMART = new SmartIndicationInterpolator();

    public static class LinearIndicationInterpolator extends SmartTabIndicationInterpolator {
        public float getLeftEdge(float f) {
            return f;
        }

        public float getRightEdge(float f) {
            return f;
        }
    }

    public static class SmartIndicationInterpolator extends SmartTabIndicationInterpolator {
        public final Interpolator leftEdgeInterpolator;
        public final Interpolator rightEdgeInterpolator;

        public SmartIndicationInterpolator() {
            this(3.0f);
        }

        public SmartIndicationInterpolator(float f) {
            this.leftEdgeInterpolator = new AccelerateInterpolator(f);
            this.rightEdgeInterpolator = new DecelerateInterpolator(f);
        }

        public float getLeftEdge(float f) {
            return this.leftEdgeInterpolator.getInterpolation(f);
        }

        public float getRightEdge(float f) {
            return this.rightEdgeInterpolator.getInterpolation(f);
        }

        public float getThickness(float f) {
            return 1.0f / ((1.0f - getLeftEdge(f)) + getRightEdge(f));
        }
    }

    public abstract float getLeftEdge(float f);

    public abstract float getRightEdge(float f);

    public float getThickness(float f) {
        return 1.0f;
    }

    public static SmartTabIndicationInterpolator of(int i) {
        if (i == 0) {
            return SMART;
        }
        if (i == 1) {
            return LINEAR;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown id: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }
}
