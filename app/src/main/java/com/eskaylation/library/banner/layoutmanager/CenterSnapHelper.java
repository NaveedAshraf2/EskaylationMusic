package com.eskaylation.library.banner.layoutmanager;

import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.OnFlingListener;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import com.eskaylation.library.banner.layoutmanager.BannerLayoutManager.OnPageChangeListener;

public class CenterSnapHelper extends OnFlingListener {
    public Scroller mGravityScroller;
    public RecyclerView mRecyclerView;
    public final OnScrollListener mScrollListener = new OnScrollListener() {
        public boolean mScrolled = false;

        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            super.onScrollStateChanged(recyclerView, i);
            BannerLayoutManager bannerLayoutManager = (BannerLayoutManager) recyclerView.getLayoutManager();
            OnPageChangeListener onPageChangeListener = bannerLayoutManager.onPageChangeListener;
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageScrollStateChanged(i);
            }
            if (i == 0 && this.mScrolled) {
                this.mScrolled = false;
                if (!CenterSnapHelper.this.snapToCenter) {
                    CenterSnapHelper.this.snapToCenter = true;
                    CenterSnapHelper.this.snapToCenterView(bannerLayoutManager, onPageChangeListener);
                    return;
                }
                CenterSnapHelper.this.snapToCenter = false;
            }
        }

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            if (i != 0 || i2 != 0) {
                this.mScrolled = true;
            }
        }
    };
    public boolean snapToCenter = false;

    public boolean onFling(int i, int i2) {
        BannerLayoutManager bannerLayoutManager = (BannerLayoutManager) this.mRecyclerView.getLayoutManager();
        if (bannerLayoutManager == null || this.mRecyclerView.getAdapter() == null) {
            return false;
        }
        if (!bannerLayoutManager.getInfinite() && (bannerLayoutManager.mOffset == bannerLayoutManager.getMaxOffset() || bannerLayoutManager.mOffset == bannerLayoutManager.getMinOffset())) {
            return false;
        }
        int minFlingVelocity = this.mRecyclerView.getMinFlingVelocity();
        this.mGravityScroller.fling(0, 0, i, i2, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
        if (bannerLayoutManager.mOrientation != 1 || Math.abs(i2) <= minFlingVelocity) {
            if (bannerLayoutManager.mOrientation == 0 && Math.abs(i) > minFlingVelocity) {
                int currentPosition = bannerLayoutManager.getCurrentPosition();
                int finalX = (int) ((((float) this.mGravityScroller.getFinalX()) / bannerLayoutManager.mInterval) / bannerLayoutManager.getDistanceRatio());
                this.mRecyclerView.smoothScrollToPosition(bannerLayoutManager.getReverseLayout() ? currentPosition - finalX : currentPosition + finalX);
            }
            return true;
        }
        int currentPosition2 = bannerLayoutManager.getCurrentPosition();
        int finalY = (int) ((((float) this.mGravityScroller.getFinalY()) / bannerLayoutManager.mInterval) / bannerLayoutManager.getDistanceRatio());
        this.mRecyclerView.smoothScrollToPosition(bannerLayoutManager.getReverseLayout() ? currentPosition2 - finalY : currentPosition2 + finalY);
        return true;
    }

    public void attachToRecyclerView(RecyclerView recyclerView) throws IllegalStateException {
        RecyclerView recyclerView2 = this.mRecyclerView;
        if (recyclerView2 != recyclerView) {
            if (recyclerView2 != null) {
                destroyCallbacks();
            }
            this.mRecyclerView = recyclerView;
            RecyclerView recyclerView3 = this.mRecyclerView;
            if (recyclerView3 != null) {
                LayoutManager layoutManager = recyclerView3.getLayoutManager();
                if (layoutManager instanceof BannerLayoutManager) {
                    setupCallbacks();
                    this.mGravityScroller = new Scroller(this.mRecyclerView.getContext(), new DecelerateInterpolator());
                    BannerLayoutManager bannerLayoutManager = (BannerLayoutManager) layoutManager;
                    snapToCenterView(bannerLayoutManager, bannerLayoutManager.onPageChangeListener);
                }
            }
        }
    }

    public void snapToCenterView(BannerLayoutManager bannerLayoutManager, OnPageChangeListener onPageChangeListener) {
        int offsetToCenter = bannerLayoutManager.getOffsetToCenter();
        if (offsetToCenter == 0) {
            this.snapToCenter = false;
        } else if (bannerLayoutManager.getOrientation() == 1) {
            this.mRecyclerView.smoothScrollBy(0, offsetToCenter);
        } else {
            this.mRecyclerView.smoothScrollBy(offsetToCenter, 0);
        }
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(bannerLayoutManager.getCurrentPosition());
        }
    }

    public void setupCallbacks() throws IllegalStateException {
        if (this.mRecyclerView.getOnFlingListener() == null) {
            this.mRecyclerView.addOnScrollListener(this.mScrollListener);
            this.mRecyclerView.setOnFlingListener(this);
            return;
        }
        throw new IllegalStateException("An instance of OnFlingListener already set.");
    }

    public void destroyCallbacks() {
        this.mRecyclerView.removeOnScrollListener(this.mScrollListener);
        this.mRecyclerView.setOnFlingListener(null);
    }
}
