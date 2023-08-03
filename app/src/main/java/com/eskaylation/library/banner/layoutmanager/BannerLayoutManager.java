package com.eskaylation.library.banner.layoutmanager;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Interpolator;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import androidx.recyclerview.widget.RecyclerView.Recycler;
import androidx.recyclerview.widget.RecyclerView.State;
import java.util.ArrayList;

public class BannerLayoutManager extends LayoutManager {
    public float centerScale;
    public View currentFocusView;
    public int itemSpace;
    public int mDecoratedMeasurement;
    public int mDecoratedMeasurementInOther;
    public int mDistanceToBottom;
    public boolean mEnableBringCenterToFront;
    public boolean mInfinite;
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
    public boolean mShouldReverseLayout;
    public Interpolator mSmoothScrollInterpolator;
    public boolean mSmoothScrollbarEnabled;
    public int mSpaceInOther;
    public int mSpaceMain;
    public float moveSpeed;
    public OnPageChangeListener onPageChangeListener;
    public SparseArray<View> positionCache;

    public interface OnPageChangeListener {
        void onPageScrollStateChanged(int i);

        void onPageSelected(int i);
    }

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

    public View onFocusSearchFailed(View view, int i, Recycler recycler, State state) {
        return null;
    }

    public void setUp() {
    }

    public float setViewElevation(View view, float f) {
        return 0.0f;
    }

    public float getDistanceRatio() {
        float f = this.moveSpeed;
        if (f == 0.0f) {
            return Float.MAX_VALUE;
        }
        return 1.0f / f;
    }

    public float setInterval() {
        return (((float) this.mDecoratedMeasurement) * (((this.centerScale - 1.0f) / 2.0f) + 1.0f)) + ((float) this.itemSpace);
    }

    public void setItemSpace(int i) {
        this.itemSpace = i;
    }

    public void setCenterScale(float f) {
        this.centerScale = f;
    }

    public void setMoveSpeed(float f) {
        assertNotInLayoutOrScroll(null);
        if (this.moveSpeed != f) {
            this.moveSpeed = f;
        }
    }

    public void setItemViewProperty(View view, float f) {
        float calculateScale = calculateScale(f + ((float) this.mSpaceMain));
        view.setScaleX(calculateScale);
        view.setScaleY(calculateScale);
    }

    public final float calculateScale(float f) {
        float abs = Math.abs(f - (((float) (this.mOrientationHelper.getTotalSpace() - this.mDecoratedMeasurement)) / 2.0f));
        int i = this.mDecoratedMeasurement;
        float f2 = 0.0f;
        if (((float) i) - abs > 0.0f) {
            f2 = ((float) i) - abs;
        }
        return (((this.centerScale - 1.0f) / ((float) this.mDecoratedMeasurement)) * f2) + 1.0f;
    }

    public BannerLayoutManager(Context context, int i) {
        this(context, i, false);
    }

    public BannerLayoutManager(Context context, int i, boolean z) {
        this.positionCache = new SparseArray<>();
        this.mReverseLayout = false;
        this.mShouldReverseLayout = false;
        this.mSmoothScrollbarEnabled = true;
        this.mPendingScrollPosition = -1;
        this.mPendingSavedState = null;
        this.mInfinite = true;
        this.mMaxVisibleItemCount = -1;
        this.mDistanceToBottom = Integer.MAX_VALUE;
        this.itemSpace = 20;
        this.centerScale = 1.2f;
        this.moveSpeed = 1.0f;
        setEnableBringCenterToFront(true);
        setMaxVisibleItemCount(3);
        setOrientation(i);
        setReverseLayout(z);
        setAutoMeasureEnabled(true);
        setItemPrefetchEnabled(false);
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
        savedState2.isReverseLayout = this.mShouldReverseLayout;
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

    public void setOrientation(int i) {
        if (i == 0 || i == 1) {
            assertNotInLayoutOrScroll(null);
            if (i != this.mOrientation) {
                this.mOrientation = i;
                this.mOrientationHelper = null;
                this.mDistanceToBottom = Integer.MAX_VALUE;
                removeAllViews();
                return;
            }
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("invalid orientation:");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }

    public void setMaxVisibleItemCount(int i) {
        assertNotInLayoutOrScroll(null);
        if (this.mMaxVisibleItemCount != i) {
            this.mMaxVisibleItemCount = i;
            removeAllViews();
        }
    }

    public final void resolveShouldLayoutReverse() {
        if (this.mOrientation == 0 && getLayoutDirection() == 1) {
            this.mReverseLayout = !this.mReverseLayout;
        }
    }

    public boolean getReverseLayout() {
        return this.mReverseLayout;
    }

    public void setReverseLayout(boolean z) {
        assertNotInLayoutOrScroll(null);
        if (z != this.mReverseLayout) {
            this.mReverseLayout = z;
            removeAllViews();
        }
    }

    public void smoothScrollToPosition(RecyclerView recyclerView, State state, int i) {
        int offsetToPosition = getOffsetToPosition(i);
        if (this.mOrientation == 1) {
            recyclerView.smoothScrollBy(0, offsetToPosition, this.mSmoothScrollInterpolator);
        } else {
            recyclerView.smoothScrollBy(offsetToPosition, 0, this.mSmoothScrollInterpolator);
        }
    }

    public void scrollToPosition(int i) {
        if (this.mInfinite || (i >= 0 && i < getItemCount())) {
            this.mPendingScrollPosition = i;
            this.mOffset = ((float) i) * (this.mShouldReverseLayout ? -this.mInterval : this.mInterval);
            requestLayout();
        }
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
        if (this.mDistanceToBottom == Integer.MAX_VALUE) {
            this.mSpaceInOther = (getTotalSpaceInOther() - this.mDecoratedMeasurementInOther) / 2;
        } else {
            this.mSpaceInOther = (getTotalSpaceInOther() - this.mDecoratedMeasurementInOther) - this.mDistanceToBottom;
        }
        this.mInterval = setInterval();
        setUp();
        this.mLeftItems = ((int) Math.abs(minRemoveOffset() / this.mInterval)) + 1;
        this.mRightItems = ((int) Math.abs(maxRemoveOffset() / this.mInterval)) + 1;
        SavedState savedState = this.mPendingSavedState;
        if (savedState != null) {
            this.mShouldReverseLayout = savedState.isReverseLayout;
            this.mPendingScrollPosition = savedState.position;
            this.mOffset = savedState.offset;
        }
        int i = this.mPendingScrollPosition;
        if (i != -1) {
            this.mOffset = ((float) i) * (this.mShouldReverseLayout ? -this.mInterval : this.mInterval);
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

    public boolean onAddFocusables(RecyclerView recyclerView, ArrayList<View> arrayList, int i, int i2) {
        int currentPosition = getCurrentPosition();
        View findViewByPosition = findViewByPosition(currentPosition);
        if (findViewByPosition == null) {
            return true;
        }
        if (recyclerView.hasFocus()) {
            int movement = getMovement(i);
            if (movement != -1) {
                recyclerView.smoothScrollToPosition(movement == 1 ? currentPosition - 1 : currentPosition + 1);
            }
        } else {
            findViewByPosition.addFocusables(arrayList, i, i2);
        }
        return true;
    }

    public final int getMovement(int i) {
        if (this.mOrientation == 1) {
            if (i == 33) {
                return this.mShouldReverseLayout ^ true ? 1 : 0;
            }
            if (i == 130) {
                return this.mShouldReverseLayout ? 1 : 0;
            }
            return -1;
        } else if (i == 17) {
            return this.mShouldReverseLayout ^ true ? 1 : 0;
        } else {
            if (i == 66) {
                return this.mShouldReverseLayout ? 1 : 0;
            }
            return -1;
        }
    }

    public void ensureLayoutState() {
        if (this.mOrientationHelper == null) {
            this.mOrientationHelper = OrientationHelper.createOrientationHelper(this, this.mOrientation);
        }
    }

    public final float getProperty(int i) {
        return ((float) i) * (this.mShouldReverseLayout ? -this.mInterval : this.mInterval);
    }

    public void onAdapterChanged(Adapter adapter, Adapter adapter2) {
        removeAllViews();
        this.mOffset = 0.0f;
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
            return !this.mShouldReverseLayout ? getCurrentPosition() : (getItemCount() - getCurrentPosition()) - 1;
        }
        float offsetOfRightAdapterPosition = getOffsetOfRightAdapterPosition();
        return !this.mShouldReverseLayout ? (int) offsetOfRightAdapterPosition : (int) ((((float) (getItemCount() - 1)) * this.mInterval) + offsetOfRightAdapterPosition);
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
        if (getChildCount() == 0 || i == 0) {
            return 0;
        }
        ensureLayoutState();
        float f = (float) i;
        float distanceRatio = f / getDistanceRatio();
        if (Math.abs(distanceRatio) < 1.0E-8f) {
            return 0;
        }
        float f2 = this.mOffset + distanceRatio;
        if (!this.mInfinite && f2 < getMinOffset()) {
            i = (int) (f - ((f2 - getMinOffset()) * getDistanceRatio()));
        } else if (!this.mInfinite && f2 > getMaxOffset()) {
            i = (int) ((getMaxOffset() - this.mOffset) * getDistanceRatio());
        }
        this.mOffset += ((float) i) / getDistanceRatio();
        layoutItems(recycler);
        return i;
    }

    public final void layoutItems(Recycler recycler) {
        int i;
        int i2;
        int i3;
        int i4;
        detachAndScrapAttachedViews(recycler);
        this.positionCache.clear();
        int itemCount = getItemCount();
        if (itemCount != 0) {
            int currentPositionOffset = this.mShouldReverseLayout ? -getCurrentPositionOffset() : getCurrentPositionOffset();
            int i5 = currentPositionOffset - this.mLeftItems;
            int i6 = this.mRightItems + currentPositionOffset;
            if (useMaxVisibleCount()) {
                if (this.mMaxVisibleItemCount % 2 == 0) {
                    i4 = this.mMaxVisibleItemCount / 2;
                    i = (currentPositionOffset - i4) + 1;
                } else {
                    i4 = (this.mMaxVisibleItemCount - 1) / 2;
                    i = currentPositionOffset - i4;
                }
                i2 = i4 + currentPositionOffset + 1;
            } else {
                i = i5;
                i2 = i6;
            }
            if (!this.mInfinite) {
                if (i < 0) {
                    if (useMaxVisibleCount()) {
                        i2 = this.mMaxVisibleItemCount;
                    }
                    i = 0;
                }
                if (i2 > itemCount) {
                    i2 = itemCount;
                }
            }
            float f = Float.MIN_VALUE;
            while (i < i2) {
                if (useMaxVisibleCount() || !removeCondition(getProperty(i) - this.mOffset)) {
                    if (i >= itemCount) {
                        i3 = i % itemCount;
                    } else if (i < 0) {
                        int i7 = (-i) % itemCount;
                        if (i7 == 0) {
                            i7 = itemCount;
                        }
                        i3 = itemCount - i7;
                    } else {
                        i3 = i;
                    }
                    View viewForPosition = recycler.getViewForPosition(i3);
                    measureChildWithMargins(viewForPosition, 0, 0);
                    resetViewProperty(viewForPosition);
                    float property = getProperty(i) - this.mOffset;
                    layoutScrap(viewForPosition, property);
                    float viewElevation = this.mEnableBringCenterToFront ? setViewElevation(viewForPosition, property) : (float) i3;
                    if (viewElevation > f) {
                        addView(viewForPosition);
                    } else {
                        addView(viewForPosition, 0);
                    }
                    if (i == currentPositionOffset) {
                        this.currentFocusView = viewForPosition;
                    }
                    this.positionCache.put(i, viewForPosition);
                    f = viewElevation;
                }
                i++;
            }
            this.currentFocusView.requestFocus();
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

    public float getMaxOffset() {
        if (!this.mShouldReverseLayout) {
            return ((float) (getItemCount() - 1)) * this.mInterval;
        }
        return 0.0f;
    }

    public float getMinOffset() {
        if (!this.mShouldReverseLayout) {
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

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0046  */
    public int getCurrentPosition() {
        int i;
        int i2;
        if (getItemCount() == 0) {
            return 0;
        }
        int currentPositionOffset = getCurrentPositionOffset();
        if (!this.mInfinite) {
            return Math.abs(currentPositionOffset);
        }
        if (!this.mShouldReverseLayout) {
            if (currentPositionOffset >= 0) {
                i = currentPositionOffset % getItemCount();
                if (i == getItemCount()) {
                    i = 0;
                }
                return i;
            }
            i2 = getItemCount() + (currentPositionOffset % getItemCount());
        } else if (currentPositionOffset > 0) {
            i2 = getItemCount() - (currentPositionOffset % getItemCount());
        } else {
            i = (-currentPositionOffset) % getItemCount();
            if (i == getItemCount()) {
            }
            return i;
        }
        i = i2;
        if (i == getItemCount()) {
        }
        return i;
    }

    public View findViewByPosition(int i) {
        int itemCount = getItemCount();
        if (itemCount == 0) {
            return null;
        }
        for (int i2 = 0; i2 < this.positionCache.size(); i2++) {
            int keyAt = this.positionCache.keyAt(i2);
            if (keyAt < 0) {
                int i3 = keyAt % itemCount;
                if (i3 == 0) {
                    i3 = -itemCount;
                }
                if (i3 + itemCount == i) {
                    return (View) this.positionCache.valueAt(i2);
                }
            } else if (i == keyAt % itemCount) {
                return (View) this.positionCache.valueAt(i2);
            }
        }
        return null;
    }

    public final int getCurrentPositionOffset() {
        return Math.round(this.mOffset / this.mInterval);
    }

    public final float getOffsetOfRightAdapterPosition() {
        float f;
        float f2;
        if (this.mShouldReverseLayout) {
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

    public int getOffsetToCenter() {
        float currentPosition;
        float distanceRatio;
        if (this.mInfinite) {
            currentPosition = (((float) getCurrentPositionOffset()) * this.mInterval) - this.mOffset;
            distanceRatio = getDistanceRatio();
        } else {
            currentPosition = (((float) getCurrentPosition()) * (!this.mShouldReverseLayout ? this.mInterval : -this.mInterval)) - this.mOffset;
            distanceRatio = getDistanceRatio();
        }
        return (int) (currentPosition * distanceRatio);
    }

    public int getOffsetToPosition(int i) {
        float f;
        float distanceRatio;
        if (this.mInfinite) {
            f = (((float) (getCurrentPositionOffset() + (!this.mShouldReverseLayout ? i - getCurrentPosition() : getCurrentPosition() - i))) * this.mInterval) - this.mOffset;
            distanceRatio = getDistanceRatio();
        } else {
            f = (((float) i) * (!this.mShouldReverseLayout ? this.mInterval : -this.mInterval)) - this.mOffset;
            distanceRatio = getDistanceRatio();
        }
        return (int) (f * distanceRatio);
    }

    public void setInfinite(boolean z) {
        assertNotInLayoutOrScroll(null);
        if (z != this.mInfinite) {
            this.mInfinite = z;
            requestLayout();
        }
    }

    public boolean getInfinite() {
        return this.mInfinite;
    }

    public void setEnableBringCenterToFront(boolean z) {
        assertNotInLayoutOrScroll(null);
        if (this.mEnableBringCenterToFront != z) {
            this.mEnableBringCenterToFront = z;
            requestLayout();
        }
    }
}
