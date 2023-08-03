package com.eskaylation.downloadmusic.widget.smarttablayout;

import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;

public final class Utils {
    public static int getMeasuredWidth(View view) {
        if (view == null) {
            return 0;
        }
        return view.getMeasuredWidth();
    }

    public static int getWidth(View view) {
        if (view == null) {
            return 0;
        }
        return view.getWidth();
    }

    public static int getWidthWithMargin(View view) {
        return getWidth(view) + getMarginHorizontally(view);
    }

    public static int getStart(View view) {
        return getStart(view, false);
    }

    public static int getStart(View view, boolean z) {
        if (view == null) {
            return 0;
        }
        if (isLayoutRtl(view)) {
            return z ? view.getRight() - getPaddingStart(view) : view.getRight();
        }
        return z ? view.getLeft() + getPaddingStart(view) : view.getLeft();
    }

    public static int getEnd(View view) {
        return getEnd(view, false);
    }

    public static int getEnd(View view, boolean z) {
        if (view == null) {
            return 0;
        }
        if (isLayoutRtl(view)) {
            return z ? view.getLeft() + getPaddingEnd(view) : view.getLeft();
        }
        return z ? view.getRight() - getPaddingEnd(view) : view.getRight();
    }

    public static int getPaddingStart(View view) {
        if (view == null) {
            return 0;
        }
        return ViewCompat.getPaddingStart(view);
    }

    public static int getPaddingEnd(View view) {
        if (view == null) {
            return 0;
        }
        return ViewCompat.getPaddingEnd(view);
    }

    public static int getPaddingHorizontally(View view) {
        if (view == null) {
            return 0;
        }
        return view.getPaddingLeft() + view.getPaddingRight();
    }

    public static int getMarginStart(View view) {
        if (view == null) {
            return 0;
        }
        return MarginLayoutParamsCompat.getMarginStart((MarginLayoutParams) view.getLayoutParams());
    }

    public static int getMarginEnd(View view) {
        if (view == null) {
            return 0;
        }
        return MarginLayoutParamsCompat.getMarginEnd((MarginLayoutParams) view.getLayoutParams());
    }

    public static int getMarginHorizontally(View view) {
        if (view == null) {
            return 0;
        }
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginStart(marginLayoutParams) + MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams);
    }

    public static boolean isLayoutRtl(View view) {
        return ViewCompat.getLayoutDirection(view) == 1;
    }
}
