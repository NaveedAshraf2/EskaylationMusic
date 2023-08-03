package com.eskaylation.downloadmusic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.drawerlayout.widget.DrawerLayout.LayoutParams;

public class FullDrawerLayout extends DrawerLayout {
    public FullDrawerLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public FullDrawerLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void onMeasure(int i, int i2) {
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        int size = MeasureSpec.getSize(i);
        int size2 = MeasureSpec.getSize(i2);
        if (mode == 1073741824 && mode2 == 1073741824) {
            setMeasuredDimension(size, size2);
            int childCount = getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = getChildAt(i3);
                if (childAt.getVisibility() != GONE) {
                    LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                    if (isContentView(childAt)) {
                        childAt.measure(MeasureSpec.makeMeasureSpec((size - layoutParams.leftMargin) - layoutParams.rightMargin, 1073741824), MeasureSpec.makeMeasureSpec((size2 - layoutParams.topMargin) - layoutParams.bottomMargin, 1073741824));
                    } else if (isDrawerView(childAt)) {
                        int drawerViewGravity = getDrawerViewGravity(childAt) & 7;
                        if ((0 & drawerViewGravity) == 0) {
                            childAt.measure(ViewGroup.getChildMeasureSpec(i, layoutParams.leftMargin + 0 + layoutParams.rightMargin, layoutParams.width), ViewGroup.getChildMeasureSpec(i2, layoutParams.topMargin + layoutParams.bottomMargin, layoutParams.height));
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Child drawer has absolute gravity ");
                            sb.append(gravityToString(drawerViewGravity));
                            sb.append(" but this already has a drawer view along that edge");
                            throw new IllegalStateException(sb.toString());
                        }
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Child ");
                        sb2.append(childAt);
                        sb2.append(" at index ");
                        sb2.append(i3);
                        sb2.append(" does not have a valid layout_gravity - must be Gravity.LEFT, Gravity.RIGHT or Gravity.NO_GRAVITY");
                        throw new IllegalStateException(sb2.toString());
                    }
                }
            }
            return;
        }
        throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
    }

    public boolean isContentView(View view) {
        return ((LayoutParams) view.getLayoutParams()).gravity == 0;
    }

    public boolean isDrawerView(View view) {
        return (Gravity.getAbsoluteGravity(((LayoutParams) view.getLayoutParams()).gravity, view.getLayoutDirection()) & 7) != 0;
    }

    public int getDrawerViewGravity(View view) {
        return Gravity.getAbsoluteGravity(((LayoutParams) view.getLayoutParams()).gravity, view.getLayoutDirection());
    }

    public static String gravityToString(int i) {
        if ((i & 3) == 3) {
            return "LEFT";
        }
        return (i & 5) == 5 ? "RIGHT" : Integer.toHexString(i);
    }
}
