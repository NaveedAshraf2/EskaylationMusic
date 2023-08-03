package com.eskaylation.downloadmusic.widget;

import android.view.View;
import androidx.viewpager.widget.ViewPager.PageTransformer;

import kotlin.jvm.internal.Intrinsics;


public final class ZoomOutPageTransformer implements PageTransformer {


    public static final class Companion {
        public Companion() {
        }


    }

    static {
        new Companion();
    }

    public void transformPage(View view, float f) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        int width = view.getWidth();
        int height = view.getHeight();
        if (f < ((float) -1)) {
            view.setAlpha(0.0f);
            return;
        }
        float f2 = (float) 1;
        if (f <= f2) {
            float max = Math.max(0.9f, f2 - Math.abs(f));
            float f3 = f2 - max;
            float f4 = (float) 2;
            float f5 = (((float) height) * f3) / f4;
            float f6 = (((float) width) * f3) / f4;
            if (f < ((float) 0)) {
                view.setTranslationX(f6 - (f5 / f4));
            } else {
                view.setTranslationX((-f6) + (f5 / f4));
            }
            view.setScaleX(max);
            view.setScaleY(max);
            view.setAlpha((((max - 0.9f) / 0.100000024f) * 0.5f) + 0.5f);
            return;
        }
        view.setAlpha(0.0f);
    }
}
