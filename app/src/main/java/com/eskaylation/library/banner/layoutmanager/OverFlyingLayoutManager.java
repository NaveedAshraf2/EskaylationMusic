package com.eskaylation.library.banner.layoutmanager;

import android.graphics.PointF;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.view.View;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.Recycler;
import androidx.recyclerview.widget.RecyclerView.SmoothScroller.ScrollVectorProvider;
import androidx.recyclerview.widget.RecyclerView.State;

public class OverFlyingLayoutManager extends LayoutManager implements ScrollVectorProvider {
    public float angle;
    public int itemSpace;
    public int mDecoratedMeasurement;
    public int mDecoratedMeasurementInOther;
    public boolean mEnableBringCenterToFront;
    public boolean mInfinite;
    public boolean mIntegerDy;
    public float mInterval;
    public int mLeftItems;
    public int mMaxVisibleItemCount;
    public float mOffset;
    public int mOrientation;
    public OrientationHelper mOrientationHelper;
    public SavedState mPendingSavedState;
    public int mPendingScrollPosition;
    public boolean mRecycleChildrenOnDetach;
    public boolean mReverseLayout;
    public int mRightItems;
    public boolean mSmoothScrollbarEnabled;
    public int mSpaceInOther;
    public int mSpaceMain;
    public float minScale;

    public static class SavedState implements Parcelable {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public boolean isReverseLayout;
        public float offset;
        public int position;

        public int describeContents() {
            return 0;
        }

        public SavedState() {
        }

        public SavedState(Parcel parcel) {
            this.position = parcel.readInt();
            this.offset = parcel.readFloat();
            boolean z = true;
            if (parcel.readInt() != 1) {
                z = false;
            }
            this.isReverseLayout = z;
        }

        public SavedState(SavedState savedState) {
            this.position = savedState.position;
            this.offset = savedState.offset;
            this.isReverseLayout = savedState.isReverseLayout;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.position);
            parcel.writeFloat(this.offset);
            parcel.writeInt(this.isReverseLayout ? 1 : 0);
        }
    }

    public float getDistanceRatio() {
        return 1.0f;
    }

    public void setUp() {
    }

    public float setInterval() {
        return (float) (this.mDecoratedMeasurement - this.itemSpace);
    }

    public void setItemViewProperty(View view, float f) {
        float calculateScale = calculateScale(((float) this.mSpaceMain) + f);
        view.setScaleX(calculateScale);
        view.setScaleY(calculateScale);
        if (VERSION.SDK_INT >= 21) {
            view.setElevation(0.0f);
        }
        float calRotation = calRotation(f);
        if (getOrientation() == 0) {
            view.setRotationY(calRotation);
        } else {
            view.setRotationX(-calRotation);
        }
    }

    public final float calRotation(float f) {
        return ((-this.angle) / this.mInterval) * f;
    }

    public final float calculateScale(float f) {
        return (((this.minScale - 1.0f) * Math.abs(f - (((float) (this.mOrientationHelper.getTotalSpace() - this.mDecoratedMeasurement)) / 2.0f))) / (((float) this.mOrientationHelper.getTotalSpace()) / 2.0f)) + 1.0f;
    }

    public float setViewElevation(View view, float f) {
        return view.getScaleX() * 5.0f;
    }

    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    public void onDetachedFromWindow(RecyclerView recyclerView, Recycler recycler) {
        super.onDetachedFromWindow(recyclerView, recycler);
        if (this.mRecycleChildrenOnDetach) {
            removeAndRecycleAllViews(recycler);
            recycler.clear();
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = this.mPendingSavedState;
        if (savedState != null) {
            return new SavedState(savedState);
        }
        SavedState savedState2 = new SavedState();
        savedState2.position = this.mPendingScrollPosition;
        savedState2.offset = this.mOffset;
        savedState2.isReverseLayout = this.mReverseLayout;
        return savedState2;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            this.mPendingSavedState = new SavedState((SavedState) parcelable);
            requestLayout();
        }
    }

    public boolean canScrollHorizontally() {
        return this.mOrientation == 0;
    }

    public boolean canScrollVertically() {
        return this.mOrientation == 1;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public final void resolveShouldLayoutReverse() {
        if (this.mOrientation == 0 && getLayoutDirection() == 1) {
            this.mReverseLayout = !this.mReverseLayout;
        }
    }

    public void smoothScrollToPosition(RecyclerView recyclerView, State state, int i) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext());
        linearSmoothScroller.setTargetPosition(i);
        startSmoothScroll(linearSmoothScroller);
    }

    public PointF computeScrollVectorForPosition(int i) {
        if (getChildCount() == 0) {
            return null;
        }
        boolean z = false;
        if (i < getPosition(getChildAt(0))) {
            z = true;
        }
        float distanceRatio = (z == (this.mReverseLayout ^ true) ? -1.0f : 1.0f) / getDistanceRatio();
        if (this.mOrientation == 0) {
            return new PointF(distanceRatio, 0.0f);
        }
        return new PointF(0.0f, distanceRatio);
    }

    public void onLayoutChildren(Recycler recycler, State state) {
        if (state.getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            this.mOffset = 0.0f;
            return;
        }
        ensureLayoutState();
        resolveShouldLayoutReverse();
        View viewForPosition = recycler.getViewForPosition(0);
        measureChildWithMargins(viewForPosition, 0, 0);
        this.mDecoratedMeasurement = this.mOrientationHelper.getDecoratedMeasurement(viewForPosition);
        this.mDecoratedMeasurementInOther = this.mOrientationHelper.getDecoratedMeasurementInOther(viewForPosition);
        this.mSpaceMain = (this.mOrientationHelper.getTotalSpace() - this.mDecoratedMeasurement) / 2;
        this.mSpaceInOther = (getTotalSpaceInOther() - this.mDecoratedMeasurementInOther) / 2;
        this.mInterval = setInterval();
        setUp();
        this.mLeftItems = ((int) Math.abs(minRemoveOffset() / this.mInterval)) + 1;
        this.mRightItems = ((int) Math.abs(maxRemoveOffset() / this.mInterval)) + 1;
        SavedState savedState = this.mPendingSavedState;
        if (savedState != null) {
            this.mReverseLayout = savedState.isReverseLayout;
            this.mPendingScrollPosition = savedState.position;
            this.mOffset = savedState.offset;
        }
        int i = this.mPendingScrollPosition;
        if (i != -1) {
            this.mOffset = ((float) i) * (this.mReverseLayout ? -this.mInterval : this.mInterval);
        }
        detachAndScrapAttachedViews(recycler);
        layoutItems(recycler);
    }

    public int getTotalSpaceInOther() {
        int width;
        int paddingRight;
        if (this.mOrientation == 0) {
            width = getHeight() - getPaddingTop();
            paddingRight = getPaddingBottom();
        } else {
            width = getWidth() - getPaddingLeft();
            paddingRight = getPaddingRight();
        }
        return width - paddingRight;
    }

    public void onLayoutCompleted(State state) {
        super.onLayoutCompleted(state);
        this.mPendingSavedState = null;
        this.mPendingScrollPosition = -1;
    }

    public void ensureLayoutState() {
        if (this.mOrientationHelper == null) {
            this.mOrientationHelper = OrientationHelper.createOrientationHelper(this, this.mOrientation);
        }
    }

    public final float getProperty(int i) {
        return ((float) i) * (this.mReverseLayout ? -this.mInterval : this.mInterval);
    }

    public void onAdapterChanged(Adapter adapter, Adapter adapter2) {
        removeAllViews();
        this.mOffset = 0.0f;
    }

    public void scrollToPosition(int i) {
        this.mPendingScrollPosition = i;
        this.mOffset = ((float) i) * (this.mReverseLayout ? -this.mInterval : this.mInterval);
        requestLayout();
    }

    public int computeHorizontalScrollOffset(State state) {
        return computeScrollOffset();
    }

    public int computeVerticalScrollOffset(State state) {
        return computeScrollOffset();
    }

    public int computeHorizontalScrollExtent(State state) {
        return computeScrollExtent();
    }

    public int computeVerticalScrollExtent(State state) {
        return computeScrollExtent();
    }

    public int computeHorizontalScrollRange(State state) {
        return computeScrollRange();
    }

    public int computeVerticalScrollRange(State state) {
        return computeScrollRange();
    }

    public final int computeScrollOffset() {
        if (getChildCount() == 0) {
            return 0;
        }
        if (!this.mSmoothScrollbarEnabled) {
            return !this.mReverseLayout ? getCurrentPosition() : (getItemCount() - getCurrentPosition()) - 1;
        }
        float offsetOfRightAdapterPosition = getOffsetOfRightAdapterPosition();
        return !this.mReverseLayout ? (int) offsetOfRightAdapterPosition : (int) ((((float) (getItemCount() - 1)) * this.mInterval) + offsetOfRightAdapterPosition);
    }

    public final int computeScrollExtent() {
        if (getChildCount() == 0) {
            return 0;
        }
        if (!this.mSmoothScrollbarEnabled) {
            return 1;
        }
        return (int) this.mInterval;
    }

    public final int computeScrollRange() {
        if (getChildCount() == 0) {
            return 0;
        }
        if (!this.mSmoothScrollbarEnabled) {
            return getItemCount();
        }
        return (int) (((float) getItemCount()) * this.mInterval);
    }

    public int scrollHorizontallyBy(int i, Recycler recycler, State state) {
        if (this.mOrientation == 1) {
            return 0;
        }
        return scrollBy(i, recycler, state);
    }

    public int scrollVerticallyBy(int i, Recycler recycler, State state) {
        if (this.mOrientation == 0) {
            return 0;
        }
        return scrollBy(i, recycler, state);
    }

    public final int scrollBy(int i, Recycler recycler, State state) {
        float f;
        if (getChildCount() == 0 || i == 0) {
            return 0;
        }
        ensureLayoutState();
        float f2 = (float) i;
        float distanceRatio = f2 / getDistanceRatio();
        if (Math.abs(distanceRatio) < 1.0E-8f) {
            return 0;
        }
        float f3 = this.mOffset + distanceRatio;
        if (!this.mInfinite && f3 < getMinOffset()) {
            i = (int) (f2 - ((f3 - getMinOffset()) * getDistanceRatio()));
        } else if (!this.mInfinite && f3 > getMaxOffset()) {
            i = (int) ((getMaxOffset() - this.mOffset) * getDistanceRatio());
        }
        if (this.mIntegerDy) {
            f = (float) ((int) (((float) i) / getDistanceRatio()));
        } else {
            f = ((float) i) / getDistanceRatio();
        }
        this.mOffset += f;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            View childAt = getChildAt(i2);
            layoutScrap(childAt, propertyChangeWhenScroll(childAt) - f);
        }
        layoutItems(recycler);
        return i;
    }

    public final void layoutItems(Recycler recycler) {
        int i;
        int i2;
        int i3;
        detachAndScrapAttachedViews(recycler);
        int currentPositionOffset = this.mReverseLayout ? -getCurrentPositionOffset() : getCurrentPositionOffset();
        int i4 = currentPositionOffset - this.mLeftItems;
        int i5 = this.mRightItems + currentPositionOffset;
        if (useMaxVisibleCount()) {
            if (this.mMaxVisibleItemCount % 2 == 0) {
                i3 = this.mMaxVisibleItemCount / 2;
                i = (currentPositionOffset - i3) + 1;
            } else {
                i3 = (this.mMaxVisibleItemCount - 1) / 2;
                i = currentPositionOffset - i3;
            }
            i5 = 1 + currentPositionOffset + i3;
        } else {
            i = i4;
        }
        int itemCount = getItemCount();
        if (!this.mInfinite) {
            if (i < 0) {
                if (useMaxVisibleCount()) {
                    i5 = this.mMaxVisibleItemCount;
                }
                i = 0;
            }
            if (i5 > itemCount) {
                i5 = itemCount;
            }
        }
        float f = Float.MIN_VALUE;
        while (i < i5) {
            if (useMaxVisibleCount() || !removeCondition(getProperty(i) - this.mOffset)) {
                if (i >= itemCount) {
                    i2 = i % itemCount;
                } else if (i < 0) {
                    int i6 = (-i) % itemCount;
                    if (i6 == 0) {
                        i6 = itemCount;
                    }
                    i2 = itemCount - i6;
                } else {
                    i2 = i;
                }
                View viewForPosition = recycler.getViewForPosition(i2);
                measureChildWithMargins(viewForPosition, 0, 0);
                resetViewProperty(viewForPosition);
                float property = getProperty(i) - this.mOffset;
                layoutScrap(viewForPosition, property);
                float viewElevation = this.mEnableBringCenterToFront ? setViewElevation(viewForPosition, property) : (float) i2;
                if (viewElevation > f) {
                    addView(viewForPosition);
                } else {
                    addView(viewForPosition, 0);
                }
                f = viewElevation;
            }
            i++;
        }
    }

    public final boolean useMaxVisibleCount() {
        return this.mMaxVisibleItemCount != -1;
    }

    public final boolean removeCondition(float f) {
        return f > maxRemoveOffset() || f < minRemoveOffset();
    }

    public final void resetViewProperty(View view) {
        view.setRotation(0.0f);
        view.setRotationY(0.0f);
        view.setRotationX(0.0f);
        view.setScaleX(1.0f);
        view.setScaleY(1.0f);
        view.setAlpha(1.0f);
    }

    public final float getMaxOffset() {
        if (!this.mReverseLayout) {
            return ((float) (getItemCount() - 1)) * this.mInterval;
        }
        return 0.0f;
    }

    public final float getMinOffset() {
        if (!this.mReverseLayout) {
            return 0.0f;
        }
        return ((float) (-(getItemCount() - 1))) * this.mInterval;
    }

    public final void layoutScrap(View view, float f) {
        int calItemLeft = calItemLeft(view, f);
        int calItemTop = calItemTop(view, f);
        if (this.mOrientation == 1) {
            int i = this.mSpaceInOther;
            int i2 = i + calItemLeft;
            int i3 = this.mSpaceMain;
            layoutDecorated(view, i2, i3 + calItemTop, i + calItemLeft + this.mDecoratedMeasurementInOther, i3 + calItemTop + this.mDecoratedMeasurement);
        } else {
            int i4 = this.mSpaceMain;
            int i5 = i4 + calItemLeft;
            int i6 = this.mSpaceInOther;
            layoutDecorated(view, i5, i6 + calItemTop, i4 + calItemLeft + this.mDecoratedMeasurement, i6 + calItemTop + this.mDecoratedMeasurementInOther);
        }
        setItemViewProperty(view, f);
    }

    public int calItemLeft(View view, float f) {
        if (this.mOrientation == 1) {
            return 0;
        }
        return (int) f;
    }

    public int calItemTop(View view, float f) {
        if (this.mOrientation == 1) {
            return (int) f;
        }
        return 0;
    }

    public float maxRemoveOffset() {
        return (float) (this.mOrientationHelper.getTotalSpace() - this.mSpaceMain);
    }

    public float minRemoveOffset() {
        return (float) (((-this.mDecoratedMeasurement) - this.mOrientationHelper.getStartAfterPadding()) - this.mSpaceMain);
    }

    public float propertyChangeWhenScroll(View view) {
        int left;
        int i;
        if (this.mOrientation == 1) {
            left = view.getTop();
            i = this.mSpaceMain;
        } else {
            left = view.getLeft();
            i = this.mSpaceMain;
        }
        return (float) (left - i);
    }

    public int getCurrentPosition() {
        int i;
        int currentPositionOffset = getCurrentPositionOffset();
        if (!this.mInfinite) {
            return Math.abs(currentPositionOffset);
        }
        if (!this.mReverseLayout) {
            if (currentPositionOffset >= 0) {
                i = currentPositionOffset % getItemCount();
            } else {
                i = (currentPositionOffset % getItemCount()) + getItemCount();
            }
        } else if (currentPositionOffset > 0) {
            i = getItemCount() - (currentPositionOffset % getItemCount());
        } else {
            i = (-currentPositionOffset) % getItemCount();
        }
        return i;
    }

    public final int getCurrentPositionOffset() {
        return Math.round(this.mOffset / this.mInterval);
    }

    public final float getOffsetOfRightAdapterPosition() {
        float f;
        float f2;
        if (this.mReverseLayout) {
            if (this.mInfinite) {
                float f3 = this.mOffset;
                if (f3 <= 0.0f) {
                    f2 = f3 % (this.mInterval * ((float) getItemCount()));
                } else {
                    float itemCount = (float) getItemCount();
                    float f4 = this.mInterval;
                    f2 = (itemCount * (-f4)) + (this.mOffset % (f4 * ((float) getItemCount())));
                }
            } else {
                f2 = this.mOffset;
            }
            return f2;
        }
        if (this.mInfinite) {
            float f5 = this.mOffset;
            if (f5 >= 0.0f) {
                f = f5 % (this.mInterval * ((float) getItemCount()));
            } else {
                float itemCount2 = (float) getItemCount();
                float f6 = this.mInterval;
                f = (itemCount2 * f6) + (this.mOffset % (f6 * ((float) getItemCount())));
            }
        } else {
            f = this.mOffset;
        }
        return f;
    }
}
