package com.eskaylation.library.banner;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.eskaylation.downloadmusic.R;

import java.util.ArrayList;

public abstract class RecyclerViewBannerBase<L extends LayoutManager, A extends Adapter> extends FrameLayout {
    public int WHAT_AUTO_PLAY;
    public int autoPlayDuration;
    public int bannerSize;
    public int currentIndex;
    public boolean hasInit;
    public IndicatorAdapter indicatorAdapter;
    public RecyclerView indicatorContainer;
    public int indicatorMargin;
    public boolean isAutoPlaying;
    public boolean isPlaying;
    public Handler mHandler;
    public L mLayoutManager;
    public RecyclerView mRecyclerView;
    public Drawable mSelectedDrawable;
    public Drawable mUnselectedDrawable;
    public boolean showIndicator;

    public class IndicatorAdapter extends Adapter {
        public int currentPosition = 0;

        public IndicatorAdapter() {
        }



        @NonNull
        @Override
        public ViewHolder onCreateViewHolder( @NonNull ViewGroup viewGroup, int i) {
            ImageView imageView = new ImageView(RecyclerViewBannerBase.this.getContext());
            LayoutParams layoutParams = new LayoutParams(-2, -2);
            int i2 = RecyclerViewBannerBase.this.indicatorMargin;
            layoutParams.setMargins(i2, i2, i2, i2);
            imageView.setLayoutParams(layoutParams);
            return new ViewHolder(imageView) {
            };
        }



        public void setPosition(int i) {
            this.currentPosition = i;
        }



        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            ((ImageView) viewHolder.itemView).setImageDrawable(this.currentPosition == i ? RecyclerViewBannerBase.this.mSelectedDrawable : RecyclerViewBannerBase.this.mUnselectedDrawable);
        }

        public int getItemCount() {
            return RecyclerViewBannerBase.this.bannerSize;
        }
    }

    public abstract L getLayoutManager(Context context, int i);

    public void onBannerScrollStateChanged(RecyclerView recyclerView, int i) {
    }

    public void onBannerScrolled(RecyclerView recyclerView, int i, int i2) {
    }

    public RecyclerViewBannerBase(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RecyclerViewBannerBase(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.autoPlayDuration =4000;
        this.WHAT_AUTO_PLAY = 1000;
        this.bannerSize = 1;
        new ArrayList();
        this.mHandler = new Handler(new Callback() {
            public boolean handleMessage(Message message) {
                int i = message.what;
                RecyclerViewBannerBase recyclerViewBannerBase = RecyclerViewBannerBase.this;
                if (i == recyclerViewBannerBase.WHAT_AUTO_PLAY) {
                    RecyclerView recyclerView = recyclerViewBannerBase.mRecyclerView;
                    int i2 = recyclerViewBannerBase.currentIndex + 1;
                    recyclerViewBannerBase.currentIndex = i2;
                    recyclerView.smoothScrollToPosition(i2);
                    RecyclerViewBannerBase.this.refreshIndicator();
                    RecyclerViewBannerBase recyclerViewBannerBase2 = RecyclerViewBannerBase.this;
                    recyclerViewBannerBase2.mHandler.sendEmptyMessageDelayed(recyclerViewBannerBase2.WHAT_AUTO_PLAY, (long) recyclerViewBannerBase2.autoPlayDuration);
                }
                return false;
            }
        });
        initView(context, attributeSet);
    }

    public void initView(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.RecyclerViewBannerBase);
        int i = 1;
        this.showIndicator = obtainStyledAttributes.getBoolean(R.styleable.RecyclerViewBannerBase_showIndicator, true);
        this.autoPlayDuration = obtainStyledAttributes.getInt(R.styleable.RecyclerViewBannerBase_interval,
                4000);
        this.isAutoPlaying = obtainStyledAttributes.getBoolean(R.styleable.RecyclerViewBannerBase_autoPlaying, true);
        this.mSelectedDrawable = obtainStyledAttributes.getDrawable(R.styleable.RecyclerViewBannerBase_indicatorSelectedSrc);
        this.mUnselectedDrawable = obtainStyledAttributes.getDrawable(R.styleable.RecyclerViewBannerBase_indicatorUnselectedSrc);
        if (this.mSelectedDrawable == null) {
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(1);
            gradientDrawable.setColor(-65536);
            gradientDrawable.setSize(dp2px(5), dp2px(5));
            gradientDrawable.setCornerRadius((float) (dp2px(5) / 2));
            this.mSelectedDrawable = new LayerDrawable(new Drawable[]{gradientDrawable});
        }
        if (this.mUnselectedDrawable == null) {
            GradientDrawable gradientDrawable2 = new GradientDrawable();
            gradientDrawable2.setShape(1);
            gradientDrawable2.setColor(-7829368);
            gradientDrawable2.setSize(dp2px(5), dp2px(5));
            gradientDrawable2.setCornerRadius((float) (dp2px(5) / 2));
            this.mUnselectedDrawable = new LayerDrawable(new Drawable[]{gradientDrawable2});
        }
        this.indicatorMargin = obtainStyledAttributes.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorSpace, dp2px(4));
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorMarginLeft, dp2px(16));
        int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorMarginRight, dp2px(0));
        int dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(R.styleable.RecyclerViewBannerBase_indicatorMarginBottom, dp2px(11));
        int i2 = obtainStyledAttributes.getInt(R.styleable.RecyclerViewBannerBase_indicatorGravity, 0);
        int i3 = i2 == 0 ? 8388611 : i2 == 2 ? 8388613 : 17;
        int i4 = obtainStyledAttributes.getInt(R.styleable.RecyclerViewBannerBase_orientation, 0);
        if (i4 == 0 || i4 != 1) {
            i = 0;
        }
        obtainStyledAttributes.recycle();
        this.mRecyclerView = new RecyclerView(context);
        new PagerSnapHelper().attachToRecyclerView(this.mRecyclerView);
        this.mLayoutManager = getLayoutManager(context, i);
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mRecyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                RecyclerViewBannerBase.this.onBannerScrolled(recyclerView, i, i2);
            }

            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                RecyclerViewBannerBase.this.onBannerScrollStateChanged(recyclerView, i);
            }
        });
        addView(this.mRecyclerView, new LayoutParams(-1, -1));
        this.indicatorContainer = new RecyclerView(context);
        this.indicatorContainer.setLayoutManager(new LinearLayoutManager(context, i, false));
        this.indicatorAdapter = new IndicatorAdapter();
        this.indicatorContainer.setAdapter(this.indicatorAdapter);
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = i3 | 80;
        layoutParams.setMargins(dimensionPixelSize, 0, dimensionPixelSize2, dimensionPixelSize3);
        addView(this.indicatorContainer, layoutParams);
        if (!this.showIndicator) {
            this.indicatorContainer.setVisibility(GONE);
        }
    }

    public void setIndicatorInterval(int i) {
        this.autoPlayDuration = i;
    }

    public synchronized void setPlaying(boolean z) {
        if (this.isAutoPlaying && this.hasInit) {
            if (!this.isPlaying && z) {
                this.mHandler.sendEmptyMessageDelayed(this.WHAT_AUTO_PLAY, (long) this.autoPlayDuration);
                this.isPlaying = true;
            } else if (this.isPlaying && !z) {
                this.mHandler.removeMessages(this.WHAT_AUTO_PLAY);
                this.isPlaying = false;
            }
        }
    }

    public void setAutoPlaying(boolean z) {
        this.isAutoPlaying = z;
        setPlaying(this.isAutoPlaying);
    }

    public void setShowIndicator(boolean z) {
        this.showIndicator = z;
        this.indicatorContainer.setVisibility(z ? VISIBLE : GONE);
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            setPlaying(false);
        } else if (action == 1 || action == 3) {
            setPlaying(true);
        }
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        try {
            return super.onTouchEvent(motionEvent);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        try {
            return super.onInterceptTouchEvent(motionEvent);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setPlaying(true);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setPlaying(false);
    }

    public void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (i == 0) {
            setPlaying(true);
        } else {
            setPlaying(false);
        }
    }

    public synchronized void refreshIndicator() {
        if (this.showIndicator && this.bannerSize > 1) {
            this.indicatorAdapter.setPosition(this.currentIndex % this.bannerSize);
            this.indicatorAdapter.notifyDataSetChanged();
        }
    }

    public int dp2px(int i) {
        return (int) TypedValue.applyDimension(1, (float) i, Resources.getSystem().getDisplayMetrics());
    }
}
