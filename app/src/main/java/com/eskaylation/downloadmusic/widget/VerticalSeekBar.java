package com.eskaylation.downloadmusic.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.ProgressBar;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.view.ViewCompat;

import com.eskaylation.downloadmusic.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class VerticalSeekBar extends AppCompatSeekBar {
    public boolean mIsDragging;
    public Method mMethodSetProgressFromUser;
    public int mRotationAngle = 90;

    public static boolean isValidRotationAngle(int i) {
        return i == 90 || i == 270;
    }

    public VerticalSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(context, attributeSet, 0, 0);
    }

    public VerticalSeekBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialize(context, attributeSet, i, 0);
    }

    public final void initialize(Context context, AttributeSet attributeSet, int i, int i2) {
        ViewCompat.setLayoutDirection(this, 0);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet,
                    R.styleable.VerticalSeekBar, i, i2);
            int integer = obtainStyledAttributes.getInteger(0, 0);
            if (isValidRotationAngle(integer)) {
                this.mRotationAngle = integer;
            }
            obtainStyledAttributes.recycle();
        }
    }

    public void setThumb(Drawable drawable) {
        super.setThumb(drawable);
    }

    public void setProgressDrawable(Drawable drawable) {
        super.setProgressDrawable(drawable);
        invalidate();
    }

    public synchronized void onDraw(Canvas canvas) {
        if (!useViewRotation()) {
            int i = this.mRotationAngle;
            if (i == 90) {
                canvas.rotate(90.0f);
                canvas.translate(0.0f, (float) (-super.getWidth()));
            } else if (i == 270) {
                canvas.rotate(-90.0f);
                canvas.translate((float) (-super.getHeight()), 0.0f);
            }
        }
        super.onDraw(canvas);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (useViewRotation()) {
            return onTouchEventUseViewRotation(motionEvent);
        }
        return onTouchEventTraditionalRotation(motionEvent);
    }

    public final boolean onTouchEventTraditionalRotation(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            setPressed(true);
            onStartTrackingTouch();
            trackTouchEvent(motionEvent);
            attemptClaimDrag(true);
            invalidate();
        } else if (action == 1) {
            if (this.mIsDragging) {
                trackTouchEvent(motionEvent);
                onStopTrackingTouch();
                setPressed(false);
            } else {
                onStartTrackingTouch();
                trackTouchEvent(motionEvent);
                onStopTrackingTouch();
                attemptClaimDrag(false);
            }
            invalidate();
        } else if (action != 2) {
            if (action == 3) {
                if (this.mIsDragging) {
                    onStopTrackingTouch();
                    setPressed(false);
                }
                invalidate();
            }
        } else if (this.mIsDragging) {
            trackTouchEvent(motionEvent);
        }
        return true;
    }

    public final boolean onTouchEventUseViewRotation(MotionEvent motionEvent) {
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        if (onTouchEvent) {
            int action = motionEvent.getAction();
            if (action == 0) {
                attemptClaimDrag(true);
            } else if (action == 1 || action == 3) {
                attemptClaimDrag(false);
            }
        }
        return onTouchEvent;
    }

    public final void trackTouchEvent(MotionEvent motionEvent) {
        int paddingLeft = super.getPaddingLeft();
        int height = getHeight() - paddingLeft;
        int paddingRight = height - super.getPaddingRight();
        int y = (int) motionEvent.getY();
        int i = this.mRotationAngle;
        float f = 0.0f;
        float f2 = i != 90 ? i != 270 ? 0.0f : (float) (height - y) : (float) (y - paddingLeft);
        if (f2 >= 0.0f && paddingRight != 0) {
            float f3 = (float) paddingRight;
            f = f2 > f3 ? 1.0f : f2 / f3;
        }
        _setProgressFromUser((int) (f * ((float) getMax())), true);
    }

    public final void attemptClaimDrag(boolean z) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(z);
        }
    }

    public final void onStartTrackingTouch() {
        this.mIsDragging = true;
    }

    public final void onStopTrackingTouch() {
        this.mIsDragging = false;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (isEnabled()) {
            boolean z = false;
            switch (i) {
                case 19:
                    break;
                case 20:
                    break;
                case 21:
                case 22:
                    return false;
                default:
                    int i2 = 0;
                    break;
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    public synchronized void setProgress(int i) {
        super.setProgress(i);
        if (!useViewRotation()) {
            refreshThumb();
        }
    }


    public final synchronized void _setProgressFromUser(int i, boolean z) {
        if (this.mMethodSetProgressFromUser == null) {
            Method declaredMethod = null;
            try {
                declaredMethod = ProgressBar.class.getDeclaredMethod("setProgress", new Class[]{Integer.TYPE, Boolean.TYPE});
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            declaredMethod.setAccessible(true);
            this.mMethodSetProgressFromUser = declaredMethod;
        }
        if (this.mMethodSetProgressFromUser == null) {
            try {
                this.mMethodSetProgressFromUser.invoke(this, new Object[]{Integer.valueOf(i), Boolean.valueOf(z)});
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException unused) {
            }
        } else {
            super.setProgress(i);
        }
        refreshThumb();
    }

    public synchronized void onMeasure(int i, int i2) {
        if (useViewRotation()) {
            super.onMeasure(i, i2);
        } else {
            super.onMeasure(i2, i);
            LayoutParams layoutParams = getLayoutParams();
            if (!isInEditMode() || layoutParams == null || layoutParams.height < 0) {
                setMeasuredDimension(super.getMeasuredHeight(), super.getMeasuredWidth());
            } else {
                setMeasuredDimension(super.getMeasuredHeight(), layoutParams.height);
            }
        }
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        if (useViewRotation()) {
            super.onSizeChanged(i, i2, i3, i4);
        } else {
            super.onSizeChanged(i2, i, i4, i3);
        }
    }

    public int getRotationAngle() {
        return this.mRotationAngle;
    }

    public void setRotationAngle(int i) {
        if (!isValidRotationAngle(i)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid angle specified :");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        } else if (this.mRotationAngle != i) {
            this.mRotationAngle = i;
            if (useViewRotation()) {
                VerticalSeekBarWrapper wrapper = getWrapper();
                if (wrapper != null) {
                    wrapper.applyViewRotation();
                }
            } else {
                requestLayout();
            }
        }
    }

    public final void refreshThumb() {
        onSizeChanged(super.getWidth(), super.getHeight(), 0, 0);
    }

    public boolean useViewRotation() {
        return !isInEditMode();
    }

    private VerticalSeekBarWrapper getWrapper() {
        ViewParent parent = getParent();
        if (parent instanceof VerticalSeekBarWrapper) {
            return (VerticalSeekBarWrapper) parent;
        }
        return null;
    }
}
