package com.eskaylation.downloadmusic.widget.shadow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.eskaylation.downloadmusic.R;


import kotlin.jvm.internal.Intrinsics;


public final class ShadowView extends FrameLayout {
    public Bitmap bitmap;
    public final float blurRadius;
    public RenderScriptBlur blurScript;
    public final Canvas canvas;
    public final Paint paint;
    public float roundingHeightScaleFactor;
    public float roundingWidthScaleFactor;
    public final int shadowAlpha;
    public final float shadowDx;
    public final float shadowDy;

    public ShadowView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4);
    }

    public ShadowView(Context context, AttributeSet attributeSet, int i, int i2) {
        this(context, attributeSet, i);
        if ((i2 & 2) != 0) {
            attributeSet = null;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }

    }

    public ShadowView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Intrinsics.checkParameterIsNotNull(context, "context");

        this.roundingWidthScaleFactor = 1.0f;
        this.roundingHeightScaleFactor = 1.0f;
        this.canvas = new Canvas();
        this.paint = new Paint(2);
        setClipChildren(false);
        setClipToPadding(false);
        setWillNotDraw(false);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet,
                R.styleable.ShadowView, 0, 0);
        this.shadowDx = (float) obtainStyledAttributes.getDimensionPixelSize(1, 0);
        this.shadowDy = (float) obtainStyledAttributes.getDimensionPixelSize(2, 0);
        this.blurRadius = Math.min((float) obtainStyledAttributes.getDimensionPixelSize(3, 1), 25.0f);
        this.shadowAlpha = obtainStyledAttributes.getInt(0, 255);
        obtainStyledAttributes.recycle();
        this.paint.setAlpha(this.shadowAlpha);
    }

    public final RenderScriptBlur getBlurScript() {
        RenderScriptBlur renderScriptBlur = this.blurScript;
        if (renderScriptBlur != null) {
            return renderScriptBlur;
        }
        Intrinsics.throwUninitializedPropertyAccessException("blurScript");
        throw null;
    }

    public final void setBlurScript(RenderScriptBlur renderScriptBlur) {
        Intrinsics.checkParameterIsNotNull(renderScriptBlur, "<set-?>");
        this.blurScript = renderScriptBlur;
    }

    public final int downScaleSize(float f) {
        return (int) Math.ceil((double) (f / 16.0f));
    }

    public final int roundSize(int i) {
        int i2 = i % 64;
        return i2 == 0 ? i : (i - i2) + 64;
    }

    public final void setUpCanvasMatrix() {
        float f = this.roundingWidthScaleFactor * 16.0f;
        float f2 = this.roundingHeightScaleFactor * 16.0f;
        Canvas canvas2 = this.canvas;
        float f3 = this.blurRadius;
        canvas2.translate(f3, f3);
        this.canvas.scale(1.0f / f, 1.0f / f2);
    }

    public final Bitmap allocateBitmap(int i, int i2) {
        int downScaleSize = downScaleSize((float) i);
        int downScaleSize2 = downScaleSize((float) i2);
        int roundSize = roundSize(downScaleSize);
        int roundSize2 = roundSize(downScaleSize2);
        this.roundingHeightScaleFactor = ((float) downScaleSize2) / ((float) roundSize2);
        this.roundingWidthScaleFactor = ((float) downScaleSize) / ((float) roundSize);
        float f = this.blurRadius;
        Bitmap createBitmap = Bitmap.createBitmap(roundSize + (((int) f) * 2), roundSize2 + (((int) f) * 2), Config.ARGB_8888);
        Intrinsics.checkExpressionValueIsNotNull(createBitmap, "Bitmap.createBitmap(\n   …onfig.ARGB_8888\n        )");
        return createBitmap;
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.bitmap = allocateBitmap(i, i2);
        Canvas canvas2 = this.canvas;
        Bitmap bitmap2 = this.bitmap;
        if (bitmap2 != null) {
            canvas2.setBitmap(bitmap2);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("bitmap");
            throw null;
        }
    }

    public void onDraw(Canvas canvas2) {
        Intrinsics.checkParameterIsNotNull(canvas2, "canvas");
        View childAt = getChildAt(0);
        Intrinsics.checkExpressionValueIsNotNull(childAt, "getChildAt(0)");
        blurChild(childAt);
        throw null;
    }

    public void onDescendantInvalidated(View view, View view2) {
        Intrinsics.checkParameterIsNotNull(view, "child");
        Intrinsics.checkParameterIsNotNull(view2, "target");
        blurChild(view);
        throw null;
    }

    public ViewParent invalidateChildInParent(int[] iArr, Rect rect) {
        View childAt = getChildAt(0);
        Intrinsics.checkExpressionValueIsNotNull(childAt, "getChildAt(0)");
        blurChild(childAt);
        throw null;
    }

    public final void blurChild(View view) {
        this.canvas.save();
        Bitmap bitmap2 = this.bitmap;
        String str = "bitmap";
        if (bitmap2 != null) {
            bitmap2.eraseColor(0);
            setUpCanvasMatrix();
            view.draw(this.canvas);
            this.canvas.restore();
            RenderScriptBlur renderScriptBlur = this.blurScript;
            if (renderScriptBlur != null) {
                Bitmap bitmap3 = this.bitmap;
                if (bitmap3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException(str);
                    throw null;
                } else {
                    renderScriptBlur.blur(bitmap3, this.blurRadius);
                    throw null;
                }
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("blurScript");
                throw null;
            }
        } else {
            Intrinsics.throwUninitializedPropertyAccessException(str);
            throw null;
        }
    }
}
