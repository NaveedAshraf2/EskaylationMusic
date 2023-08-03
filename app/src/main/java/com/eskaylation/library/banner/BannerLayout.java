package com.eskaylation.library.banner;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.eskaylation.downloadmusic.R;
import com.eskaylation.library.banner.layoutmanager.BannerLayoutManager;
import com.eskaylation.library.banner.layoutmanager.CenterSnapHelper;


public class BannerLayout extends FrameLayout {
    public int WHAT_AUTO_PLAY;
    public int autoPlayDuration;
    public int bannerSize;
    public float centerScale;
    public int currentIndex;
    public boolean hasInit;
    public IndicatorAdapter indicatorAdapter;
    public RecyclerView indicatorContainer;
    public int indicatorMargin;
    public boolean isAutoPlaying;
    public boolean isPlaying;
    public int itemSpace;
    public Handler mHandler;
    public BannerLayoutManager mLayoutManager;
    public RecyclerView mRecyclerView;
    public Drawable mSelectedDrawable;
    public Drawable mUnselectedDrawable;
    public float moveSpeed;
    public boolean showIndicator;

    public class IndicatorAdapter extends Adapter {
        public int currentPosition = 0;

        public IndicatorAdapter() {
        }

        public void setPosition(int i) {
            this.currentPosition = i;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            ImageView imageView = new ImageView(BannerLayout.this.getContext());
            LayoutParams layoutParams = new LayoutParams(-2, -2);
            layoutParams.setMargins(BannerLayout.this.indicatorMargin, BannerLayout.this.indicatorMargin, BannerLayout.this.indicatorMargin, BannerLayout.this.indicatorMargin);
            imageView.setLayoutParams(layoutParams);
            return new ViewHolder(imageView) {
            };
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            ((ImageView) viewHolder.itemView).setImageDrawable(this.currentPosition == i ? BannerLayout.this.mSelectedDrawable : BannerLayout.this.mUnselectedDrawable);
        }

        public int getItemCount() {
            return BannerLayout.this.bannerSize;
        }
    }



    public BannerLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BannerLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.WHAT_AUTO_PLAY = 1000;
        this.bannerSize = 1;
        this.isPlaying = false;
        this.isAutoPlaying = true;
        this.mHandler = new Handler(new Callback() {
            public boolean handleMessage(Message message) {
                if (message.what == BannerLayout.this.WHAT_AUTO_PLAY && BannerLayout.this.currentIndex == BannerLayout.this.mLayoutManager.getCurrentPosition()) {
                    BannerLayout.this.mRecyclerView.smoothScrollToPosition(BannerLayout.this.currentIndex);
                    BannerLayout bannerLayout = BannerLayout.this;
                    bannerLayout.mHandler.sendEmptyMessageDelayed(bannerLayout.WHAT_AUTO_PLAY, (long) BannerLayout.this.autoPlayDuration);
                    BannerLayout.this.refreshIndicator();
                }
                return false;
            }
        });
        initView(context, attributeSet);
    }

    public void initView(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.BannerLayout);
        int i = 1;
        this.showIndicator = obtainStyledAttributes.getBoolean(R.styleable.RecyclerViewBannerBase_showIndicator, true);
        this.autoPlayDuration = obtainStyledAttributes.getInt(R.styleable.RecyclerViewBannerBase_interval, 4000);
        this.isAutoPlaying = obtainStyledAttributes.getBoolean(R.styleable.RecyclerViewBannerBase_autoPlaying, true);
        this.itemSpace = obtainStyledAttributes.getInt(R.styleable.BannerLayout_itemSpace, 20);
        this.centerScale = obtainStyledAttributes.getFloat(R.styleable.BannerLayout_centerScale, 1.2f);
        this.moveSpeed = obtainStyledAttributes.getFloat(R.styleable.BannerLayout_moveSpeed, 1.0f);
        if (this.mSelectedDrawable == null) {
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(1);
            gradientDrawable.setColor(Color.parseColor("#1ea1f3"));
            gradientDrawable.setSize(dp2px(5), dp2px(5));
            gradientDrawable.setCornerRadius((float) (dp2px(5) / 2));
            this.mSelectedDrawable = new LayerDrawable(new Drawable[]{gradientDrawable});
        }
        if (this.mUnselectedDrawable == null) {
            GradientDrawable gradientDrawable2 = new GradientDrawable();
            gradientDrawable2.setShape(1);
            gradientDrawable2.setColor(Color.parseColor("#47648e"));
            gradientDrawable2.setSize(dp2px(5), dp2px(5));
            gradientDrawable2.setCornerRadius((float) (dp2px(5) / 2));
            this.mUnselectedDrawable = new LayerDrawable(new Drawable[]{gradientDrawable2});
        }
        this.indicatorMargin = dp2px(4);
        int dp2px = dp2px(16);
        int dp2px2 = dp2px(0);
        int dp2px3 = dp2px(11);
        int i2 = obtainStyledAttributes.getInt(R.styleable.RecyclerViewBannerBase_orientation, 0);
        if (i2 == 0 || i2 != 1) {
            i = 0;
        }
        obtainStyledAttributes.recycle();
        this.mRecyclerView = new RecyclerView(context);
        addView(this.mRecyclerView, new LayoutParams(-1, -1));
        this.mLayoutManager = new BannerLayoutManager(getContext(), i);
        this.mLayoutManager.setItemSpace(this.itemSpace);
        this.mLayoutManager.setCenterScale(this.centerScale);
        this.mLayoutManager.setMoveSpeed(this.moveSpeed);
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
        new CenterSnapHelper().attachToRecyclerView(this.mRecyclerView);
        this.indicatorContainer = new RecyclerView(context);
        this.indicatorContainer.setLayoutManager(new LinearLayoutManager(context, i, false));
        this.indicatorAdapter = new IndicatorAdapter();
        this.indicatorContainer.setAdapter(this.indicatorAdapter);
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = 81;
        layoutParams.setMargins(dp2px, dp2px3, dp2px2, 0);
        addView(this.indicatorContainer, layoutParams);
        if (!this.showIndicator) {
            this.indicatorContainer.setVisibility(GONE);
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

    public void setCenterScale(float f) {
        this.centerScale = f;
        this.mLayoutManager.setCenterScale(f);
    }

    public void setMoveSpeed(float f) {
        this.moveSpeed = f;
        this.mLayoutManager.setMoveSpeed(f);
    }

    public void setItemSpace(int i) {
        this.itemSpace = i;
        this.mLayoutManager.setItemSpace(i);
    }

    public void setAutoPlayDuration(int i) {
        this.autoPlayDuration = i;
    }

    public void setOrientation(int i) {
        this.mLayoutManager.setOrientation(i);
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

    public void setAdapter(Adapter adapter) {
        boolean z = false;
        this.hasInit = false;
        this.mRecyclerView.setAdapter(adapter);
        this.bannerSize = adapter.getItemCount();
        BannerLayoutManager bannerLayoutManager = this.mLayoutManager;
        if (this.bannerSize >= 3) {
            z = true;
        }
        bannerLayoutManager.setInfinite(z);
        setPlaying(true);
        this.mRecyclerView.addOnScrollListener(new OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                if (i != 0) {
                    BannerLayout.this.setPlaying(false);
                }
            }

            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                int currentPosition = BannerLayout.this.mLayoutManager.getCurrentPosition();
                Log.d("xxx", "onScrollStateChanged");
                if (BannerLayout.this.currentIndex != currentPosition) {
                    BannerLayout.this.currentIndex = currentPosition;
                }
                if (i == 0) {
                    BannerLayout.this.setPlaying(true);
                }
                BannerLayout.this.refreshIndicator();
            }
        });
        this.hasInit = true;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            setPlaying(false);
        } else if (action == 1 || action == 3) {
            setPlaying(true);
        }
        return super.dispatchTouchEvent(motionEvent);
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

    public int dp2px(int i) {
        return (int) TypedValue.applyDimension(1, (float) i, Resources.getSystem().getDisplayMetrics());
    }

    public synchronized void refreshIndicator() {
        if (this.showIndicator && this.bannerSize > 1) {
            this.indicatorAdapter.setPosition(this.currentIndex % this.bannerSize);
            this.indicatorAdapter.notifyDataSetChanged();
        }
    }
}
