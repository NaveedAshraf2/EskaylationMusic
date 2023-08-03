package com.eskaylation.downloadmusic.widget.smarttablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.widget.smarttablayout.SmartTabLayout.TabColorizer;
import com.google.android.exoplayer2.text.cea.Cea608Decoder;

public class SmartTabStrip extends LinearLayout {
    public final Paint borderPaint;
    public final int bottomBorderColor;
    public final int bottomBorderThickness;
    public TabColorizer customTabColorizer;
    public final SimpleTabColorizer defaultTabColorizer;
    public final float dividerHeight;
    public final Paint dividerPaint;
    public final int dividerThickness;
    public final boolean drawDecorationAfterTab;
    public SmartTabIndicationInterpolator indicationInterpolator;
    public final boolean indicatorAlwaysInCenter;
    public final float indicatorCornerRadius;
    public final int indicatorGravity;
    public final boolean indicatorInFront;
    public final Paint indicatorPaint;
    public final RectF indicatorRectF = new RectF();
    public final int indicatorThickness;
    public final int indicatorWidth;
    public final boolean indicatorWithoutPadding;
    public int lastPosition;
    public int selectedPosition;
    public float selectionOffset;
    public final int topBorderColor;
    public final int topBorderThickness;

    public static class SimpleTabColorizer implements TabColorizer {
        public int[] dividerColors;
        public int[] indicatorColors;

        public SimpleTabColorizer() {
        }

        public final int getIndicatorColor(int i) {
            int[] iArr = this.indicatorColors;
            return iArr[i % iArr.length];
        }

        public final int getDividerColor(int i) {
            int[] iArr = this.dividerColors;
            return iArr[i % iArr.length];
        }

        public void setIndicatorColors(int... iArr) {
            this.indicatorColors = iArr;
        }

        public void setDividerColors(int... iArr) {
            this.dividerColors = iArr;
        }
    }

    public SmartTabStrip(Context context, AttributeSet attributeSet) {
        super(context);
        int i;
        int[] iArr;
        int[] iArr2;

        setWillNotDraw(false);
        float f = getResources().getDisplayMetrics().density;
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(16842800, typedValue, true);
        int i2 = typedValue.data;
        int i3 = (int) (8.0f * f);
        float f2 = 0.0f * f;
        int colorAlpha = setColorAlpha(i2, (byte) 0x26);
        int i4 = (int) f2;
        int colorAlpha2 = setColorAlpha(i2, (byte) 0x26);
        int i5 = (int) (2.0f * f);
        int colorAlpha3 = setColorAlpha(i2, (byte) 32);
        int i6 = (int) (f * 1.0f);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet,
                R.styleable.stl_SmartTabLayout);
        boolean z = obtainStyledAttributes.getBoolean(14, false);
        boolean z2 = obtainStyledAttributes.getBoolean(23, false);
        boolean z3 = obtainStyledAttributes.getBoolean(19, false);
        int i7 = obtainStyledAttributes.getInt(20, 0);
        int i8 = obtainStyledAttributes.getInt(18, 0);
        int i9 = i7;
        int color = obtainStyledAttributes.getColor(15, -13388315);
        int i10 = i8;
        int resourceId = obtainStyledAttributes.getResourceId(16, -1);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(21, i3);
        int layoutDimension = obtainStyledAttributes.getLayoutDimension(22, -1);
        float dimension = obtainStyledAttributes.getDimension(17, f2);
        int color2 = obtainStyledAttributes.getColor(24, colorAlpha);
        int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(25, i4);
        int color3 = obtainStyledAttributes.getColor(27, colorAlpha2);
        int dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(28, i5);
        int color4 = obtainStyledAttributes.getColor(10, colorAlpha3);
        float f3 = dimension;
        int resourceId2 = obtainStyledAttributes.getResourceId(11, -1);
        int dimensionPixelSize4 = obtainStyledAttributes.getDimensionPixelSize(12, i6);
        boolean z4 = obtainStyledAttributes.getBoolean(13, false);
        obtainStyledAttributes.recycle();
        if (resourceId == -1) {
            i = 1;
            iArr = new int[]{color};
        } else {
            i = 1;
            iArr = getResources().getIntArray(resourceId);
        }
        if (resourceId2 == -1) {
            iArr2 = new int[i];
            iArr2[0] = color4;
        } else {
            iArr2 = getResources().getIntArray(resourceId2);
        }
        this.defaultTabColorizer = new SimpleTabColorizer();
        this.defaultTabColorizer.setIndicatorColors(iArr);
        this.defaultTabColorizer.setDividerColors(iArr2);
        this.topBorderThickness = dimensionPixelSize2;
        this.topBorderColor = color2;
        this.bottomBorderThickness = dimensionPixelSize3;
        this.bottomBorderColor = color3;
        this.borderPaint = new Paint(1);
        this.indicatorAlwaysInCenter = z;
        this.indicatorWithoutPadding = z2;
        this.indicatorInFront = z3;
        this.indicatorThickness = dimensionPixelSize;
        this.indicatorWidth = layoutDimension;
        this.indicatorPaint = new Paint(1);
        this.indicatorCornerRadius = f3;
        this.indicatorGravity = i10;
        this.dividerHeight = 0.5f;
        this.dividerPaint = new Paint(1);
        int i11 = dimensionPixelSize4;
        this.dividerPaint.setStrokeWidth((float) i11);
        this.dividerThickness = i11;
        this.drawDecorationAfterTab = z4;
        this.indicationInterpolator = SmartTabIndicationInterpolator.of(i9);
    }

    public static int setColorAlpha(int i, byte b) {
        return Color.argb(b, Color.red(i), Color.green(i), Color.blue(i));
    }

    public static int blendColors(int i, int i2, float f) {
        float f2 = 1.0f - f;
        return Color.rgb((int) ((((float) Color.red(i)) * f) + (((float) Color.red(i2)) * f2)), (int) ((((float) Color.green(i)) * f) + (((float) Color.green(i2)) * f2)), (int) ((((float) Color.blue(i)) * f) + (((float) Color.blue(i2)) * f2)));
    }

    public void setIndicationInterpolator(SmartTabIndicationInterpolator smartTabIndicationInterpolator) {
        this.indicationInterpolator = smartTabIndicationInterpolator;
        invalidate();
    }

    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        this.customTabColorizer = tabColorizer;
        invalidate();
    }

    public void setSelectedIndicatorColors(int... iArr) {
        this.customTabColorizer = null;
        this.defaultTabColorizer.setIndicatorColors(iArr);
        invalidate();
    }

    public void setDividerColors(int... iArr) {
        this.customTabColorizer = null;
        this.defaultTabColorizer.setDividerColors(iArr);
        invalidate();
    }

    public void onViewPagerPageChanged(int i, float f) {
        this.selectedPosition = i;
        this.selectionOffset = f;
        if (f == 0.0f) {
            int i2 = this.lastPosition;
            int i3 = this.selectedPosition;
            if (i2 != i3) {
                this.lastPosition = i3;
            }
        }
        invalidate();
    }

    public boolean isIndicatorAlwaysInCenter() {
        return this.indicatorAlwaysInCenter;
    }

    public TabColorizer getTabColorizer() {
        TabColorizer tabColorizer = this.customTabColorizer;
        return tabColorizer != null ? tabColorizer : this.defaultTabColorizer;
    }

    public void onDraw(Canvas canvas) {
        if (!this.drawDecorationAfterTab) {
            drawDecoration(canvas);
        }
    }

    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.drawDecorationAfterTab) {
            drawDecoration(canvas);
        }
    }

    public final void drawDecoration(Canvas canvas) {
        int i;
        int i2;
        Canvas canvas2 = canvas;
        int height = getHeight();
        int width = getWidth();
        int childCount = getChildCount();
        TabColorizer tabColorizer = getTabColorizer();
        boolean isLayoutRtl = Utils.isLayoutRtl(this);
        if (this.indicatorInFront) {
            drawOverline(canvas2, 0, width);
            drawUnderline(canvas2, 0, width, height);
        }
        if (childCount > 0) {
            View childAt = getChildAt(this.selectedPosition);
            int start = Utils.getStart(childAt, this.indicatorWithoutPadding);
            int end = Utils.getEnd(childAt, this.indicatorWithoutPadding);
            if (!isLayoutRtl) {
                int i3 = start;
                start = end;
                end = i3;
            }
            int indicatorColor = tabColorizer.getIndicatorColor(this.selectedPosition);
            float f = (float) this.indicatorThickness;
            if (this.selectionOffset > 0.0f && this.selectedPosition < getChildCount() - 1) {
                int indicatorColor2 = tabColorizer.getIndicatorColor(this.selectedPosition + 1);
                if (indicatorColor != indicatorColor2) {
                    indicatorColor = blendColors(indicatorColor2, indicatorColor, this.selectionOffset);
                }
                float leftEdge = this.indicationInterpolator.getLeftEdge(this.selectionOffset);
                float rightEdge = this.indicationInterpolator.getRightEdge(this.selectionOffset);
                float thickness = this.indicationInterpolator.getThickness(this.selectionOffset);
                View childAt2 = getChildAt(this.selectedPosition + 1);
                int start2 = Utils.getStart(childAt2, this.indicatorWithoutPadding);
                int end2 = Utils.getEnd(childAt2, this.indicatorWithoutPadding);
                if (isLayoutRtl) {
                    i = (int) ((((float) end2) * rightEdge) + ((1.0f - rightEdge) * ((float) end)));
                    i2 = (int) ((((float) start2) * leftEdge) + ((1.0f - leftEdge) * ((float) start)));
                } else {
                    i = (int) ((((float) start2) * leftEdge) + ((1.0f - leftEdge) * ((float) end)));
                    i2 = (int) ((((float) end2) * rightEdge) + ((1.0f - rightEdge) * ((float) start)));
                }
                f *= thickness;
                start = i2;
                end = i;
            }
            drawIndicator(canvas, end, start, height, f, indicatorColor);
        }
        if (!this.indicatorInFront) {
            drawOverline(canvas2, 0, width);
            drawUnderline(canvas2, 0, getWidth(), height);
        }
        drawSeparator(canvas2, height, childCount);
    }

    public final void drawSeparator(Canvas canvas, int i, int i2) {
        int i3 = i;
        if (this.dividerThickness > 0) {
            int min = (int) (Math.min(Math.max(0.0f, this.dividerHeight), 1.0f) * ((float) i3));
            TabColorizer tabColorizer = getTabColorizer();
            int i4 = (i3 - min) / 2;
            int i5 = min + i4;
            boolean isLayoutRtl = Utils.isLayoutRtl(this);
            for (int i6 = 0; i6 < i2 - 1; i6++) {
                View childAt = getChildAt(i6);
                int end = Utils.getEnd(childAt);
                int marginEnd = Utils.getMarginEnd(childAt);
                int i7 = isLayoutRtl ? end - marginEnd : end + marginEnd;
                this.dividerPaint.setColor(tabColorizer.getDividerColor(i6));
                float f = (float) i7;
                canvas.drawLine(f, (float) i4, f, (float) i5, this.dividerPaint);
            }
        }
    }

    public final void drawIndicator(Canvas canvas, int i, int i2, int i3, float f, int i4) {
        float f2;
        float f3;
        int i5 = this.indicatorThickness;
        if (i5 > 0 && this.indicatorWidth != 0) {
            int i6 = this.indicatorGravity;
            if (i6 == 1) {
                f2 = (float) i5;
                f3 = f2 / 2.0f;
            } else if (i6 != 2) {
                f3 = ((float) i3) - (((float) i5) / 2.0f);
            } else {
                f2 = (float) i3;
                f3 = f2 / 2.0f;
            }
            float f4 = f / 2.0f;
            float f5 = f3 - f4;
            float f6 = f3 + f4;
            this.indicatorPaint.setColor(i4);
            if (this.indicatorWidth == -1) {
                this.indicatorRectF.set((float) i, f5, (float) i2, f6);
            } else {
                float abs = ((float) (Math.abs(i - i2) - this.indicatorWidth)) / 2.0f;
                this.indicatorRectF.set(((float) i) + abs, f5, ((float) i2) - abs, f6);
            }
            float f7 = this.indicatorCornerRadius;
            if (f7 > 0.0f) {
                canvas.drawRoundRect(this.indicatorRectF, f7, f7, this.indicatorPaint);
            } else {
                canvas.drawRect(this.indicatorRectF, this.indicatorPaint);
            }
        }
    }

    public final void drawOverline(Canvas canvas, int i, int i2) {
        if (this.topBorderThickness > 0) {
            this.borderPaint.setColor(this.topBorderColor);
            canvas.drawRect((float) i, 0.0f, (float) i2, (float) this.topBorderThickness, this.borderPaint);
        }
    }

    public final void drawUnderline(Canvas canvas, int i, int i2, int i3) {
        if (this.bottomBorderThickness > 0) {
            this.borderPaint.setColor(this.bottomBorderColor);
            canvas.drawRect((float) i, (float) (i3 - this.bottomBorderThickness), (float) i2, (float) i3, this.borderPaint);
        }
    }
}
