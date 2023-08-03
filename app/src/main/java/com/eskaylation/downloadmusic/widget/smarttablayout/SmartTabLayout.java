package com.eskaylation.downloadmusic.widget.smarttablayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.eskaylation.downloadmusic.R;

public class SmartTabLayout extends HorizontalScrollView {
    public boolean distributeEvenly;
    public InternalTabClickListener internalTabClickListener;
    public OnScrollChangeListener onScrollChangeListener;
    public OnTabClickListener onTabClickListener;
    public TabProvider tabProvider;
    public final SmartTabStrip tabStrip;
    public int tabViewBackgroundResId;
    public boolean tabViewTextAllCaps;
    public ColorStateList tabViewTextColors;
    public int tabViewTextHorizontalPadding;
    public int tabViewTextMinWidth;
    public float tabViewTextSize;
    public int titleOffset;
    public ViewPager viewPager;
    public OnPageChangeListener viewPagerPageChangeListener;

    public class InternalTabClickListener implements OnClickListener {
        public InternalTabClickListener() {
        }

        public void onClick(View view) {
            for (int i = 0; i < SmartTabLayout.this.tabStrip.getChildCount(); i++) {
                if (view == SmartTabLayout.this.tabStrip.getChildAt(i)) {
                    if (SmartTabLayout.this.onTabClickListener != null) {
                        SmartTabLayout.this.onTabClickListener.onTabClicked(i);
                    }
                    SmartTabLayout.this.viewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

    public class InternalViewPagerListener implements OnPageChangeListener {
        public int scrollState;

        public InternalViewPagerListener() {
        }

        public void onPageScrolled(int i, float f, int i2) {
            int childCount = SmartTabLayout.this.tabStrip.getChildCount();
            if (childCount != 0 && i >= 0 && i < childCount) {
                SmartTabLayout.this.tabStrip.onViewPagerPageChanged(i, f);
                SmartTabLayout.this.scrollToTab(i, f);
                if (SmartTabLayout.this.viewPagerPageChangeListener != null) {
                    SmartTabLayout.this.viewPagerPageChangeListener.onPageScrolled(i, f, i2);
                }
            }
        }

        public void onPageScrollStateChanged(int i) {
            this.scrollState = i;
            if (SmartTabLayout.this.viewPagerPageChangeListener != null) {
                SmartTabLayout.this.viewPagerPageChangeListener.onPageScrollStateChanged(i);
            }
        }

        public void onPageSelected(int i) {
            if (this.scrollState == 0) {
                SmartTabLayout.this.tabStrip.onViewPagerPageChanged(i, 0.0f);
                SmartTabLayout.this.scrollToTab(i, 0.0f);
            }
            int childCount = SmartTabLayout.this.tabStrip.getChildCount();
            int i2 = 0;
            while (i2 < childCount) {
                SmartTabLayout.this.tabStrip.getChildAt(i2).setSelected(i == i2);
                i2++;
            }
            if (SmartTabLayout.this.viewPagerPageChangeListener != null) {
                SmartTabLayout.this.viewPagerPageChangeListener.onPageSelected(i);
            }
        }
    }

    public interface OnScrollChangeListener {
        void onScrollChanged(int i, int i2);
    }

    public interface OnTabClickListener {
        void onTabClicked(int i);
    }

    public static class SimpleTabProvider implements TabProvider {
        public final LayoutInflater inflater;
        public final int tabViewLayoutId;
        public final int tabViewTextViewId;

        public SimpleTabProvider(Context context, int i, int i2) {
            this.inflater = LayoutInflater.from(context);
            this.tabViewLayoutId = i;
            this.tabViewTextViewId = i2;
        }

        public View createTabView(ViewGroup viewGroup, int i, PagerAdapter pagerAdapter) {
            int i2 = this.tabViewLayoutId;
            TextView textView = null;
            View inflate = i2 != -1 ? this.inflater.inflate(i2, viewGroup, false) : null;
            int i3 = this.tabViewTextViewId;
            if (!(i3 == -1 || inflate == null)) {
                textView = (TextView) inflate.findViewById(i3);
            }
            if (textView == null && TextView.class.isInstance(inflate)) {
                textView = (TextView) inflate;
            }
            if (textView != null) {
                textView.setText(pagerAdapter.getPageTitle(i));
            }
            return inflate;
        }
    }

    public interface TabColorizer {
        int getDividerColor(int i);

        int getIndicatorColor(int i);
    }

    public interface TabProvider {
        View createTabView(ViewGroup viewGroup, int i, PagerAdapter pagerAdapter);
    }

    public SmartTabLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SmartTabLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        Context context2 = context;
        AttributeSet attributeSet2 = attributeSet;

        setHorizontalScrollBarEnabled(false);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float f = displayMetrics.density;
        float applyDimension = TypedValue.applyDimension(2, 12.0f, displayMetrics);
        int i2 = (int) (16.0f * f);
        int i3 = (int) (0.0f * f);
        int i4 = (int) (f * 24.0f);
        TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(attributeSet2,
                R.styleable.stl_SmartTabLayout, i, 0);
        int resourceId = obtainStyledAttributes.getResourceId(3, -1);
        boolean z = obtainStyledAttributes.getBoolean(4, true);
        ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(5);
        float dimension = obtainStyledAttributes.getDimension(8, applyDimension);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(6, i2);
        int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(7, i3);
        int resourceId2 = obtainStyledAttributes.getResourceId(1, -1);
        int resourceId3 = obtainStyledAttributes.getResourceId(2, -1);
        boolean z2 = obtainStyledAttributes.getBoolean(9, false);
        boolean z3 = obtainStyledAttributes.getBoolean(0, true);
        int layoutDimension = obtainStyledAttributes.getLayoutDimension(26, i4);
        obtainStyledAttributes.recycle();
        this.titleOffset = layoutDimension;
        this.tabViewBackgroundResId = resourceId;
        this.tabViewTextAllCaps = z;
        if (colorStateList == null) {
            colorStateList = ColorStateList.valueOf(-67108864);
        }
        this.tabViewTextColors = colorStateList;
        this.tabViewTextSize = dimension;
        this.tabViewTextHorizontalPadding = dimensionPixelSize;
        this.tabViewTextMinWidth = dimensionPixelSize2;
        this.internalTabClickListener = z3 ? new InternalTabClickListener() : null;
        this.distributeEvenly = z2;
        if (resourceId2 != -1) {
            setCustomTabView(resourceId2, resourceId3);
        }
        this.tabStrip = new SmartTabStrip(context2, attributeSet2);
        if (!z2 || !this.tabStrip.isIndicatorAlwaysInCenter()) {
            setFillViewport(!this.tabStrip.isIndicatorAlwaysInCenter());
            addView(this.tabStrip, -1, -1);
            return;
        }
        throw new UnsupportedOperationException("'distributeEvenly' and 'indicatorAlwaysInCenter' both use does not support");
    }

    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        OnScrollChangeListener onScrollChangeListener2 = this.onScrollChangeListener;
        if (onScrollChangeListener2 != null) {
            onScrollChangeListener2.onScrollChanged(i, i3);
        }
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.tabStrip.isIndicatorAlwaysInCenter() && this.tabStrip.getChildCount() > 0) {
            View childAt = this.tabStrip.getChildAt(0);
            SmartTabStrip smartTabStrip = this.tabStrip;
            View childAt2 = smartTabStrip.getChildAt(smartTabStrip.getChildCount() - 1);
            int measuredWidth = ((i - Utils.getMeasuredWidth(childAt)) / 2) - Utils.getMarginStart(childAt);
            int measuredWidth2 = ((i - Utils.getMeasuredWidth(childAt2)) / 2) - Utils.getMarginEnd(childAt2);
            SmartTabStrip smartTabStrip2 = this.tabStrip;
            smartTabStrip2.setMinimumWidth(smartTabStrip2.getMeasuredWidth());
            ViewCompat.setPaddingRelative(this, measuredWidth, getPaddingTop(), measuredWidth2, getPaddingBottom());
            setClipToPadding(false);
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            ViewPager viewPager2 = this.viewPager;
            if (viewPager2 != null) {
                scrollToTab(viewPager2.getCurrentItem(), 0.0f);
            }
        }
    }

    public void setIndicationInterpolator(SmartTabIndicationInterpolator smartTabIndicationInterpolator) {
        this.tabStrip.setIndicationInterpolator(smartTabIndicationInterpolator);
    }

    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        this.tabStrip.setCustomTabColorizer(tabColorizer);
    }

    public void setDefaultTabTextColor(int i) {
        this.tabViewTextColors = ColorStateList.valueOf(i);
    }

    public void setDefaultTabTextColor(ColorStateList colorStateList) {
        this.tabViewTextColors = colorStateList;
    }

    public void setDistributeEvenly(boolean z) {
        this.distributeEvenly = z;
    }

    public void setSelectedIndicatorColors(int... iArr) {
        this.tabStrip.setSelectedIndicatorColors(iArr);
    }

    public void setDividerColors(int... iArr) {
        this.tabStrip.setDividerColors(iArr);
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.viewPagerPageChangeListener = onPageChangeListener;
    }

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener2) {
        this.onScrollChangeListener = onScrollChangeListener2;
    }

    public void setOnTabClickListener(OnTabClickListener onTabClickListener2) {
        this.onTabClickListener = onTabClickListener2;
    }

    public void setCustomTabView(int i, int i2) {
        this.tabProvider = new SimpleTabProvider(getContext(), i, i2);
    }

    public void setCustomTabView(TabProvider tabProvider2) {
        this.tabProvider = tabProvider2;
    }

    public void setViewPager(ViewPager viewPager2) {
        this.tabStrip.removeAllViews();
        this.viewPager = viewPager2;
        if (viewPager2 != null && viewPager2.getAdapter() != null) {
            viewPager2.addOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }

    public TextView createDefaultTabView(CharSequence charSequence) {
        TextView textView = new TextView(getContext());
        textView.setGravity(17);
        textView.setText(charSequence);
        textView.setTextColor(this.tabViewTextColors);
        textView.setTextSize(0, this.tabViewTextSize);
        textView.setLayoutParams(new LayoutParams(-2, -1));
        int i = this.tabViewBackgroundResId;
        if (i != -1) {
            textView.setBackgroundResource(i);
        } else {
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(16843534, typedValue, true);
            textView.setBackgroundResource(typedValue.resourceId);
        }
        if (VERSION.SDK_INT >= 14) {
            textView.setAllCaps(this.tabViewTextAllCaps);
        }
        int i2 = this.tabViewTextHorizontalPadding;
        textView.setPadding(i2, 0, i2, 0);
        int i3 = this.tabViewTextMinWidth;
        if (i3 > 0) {
            textView.setMinWidth(i3);
        }
        return textView;
    }

    public final void populateTabStrip() {
        View view;
        PagerAdapter adapter = this.viewPager.getAdapter();
        int i = 0;
        while (i < adapter.getCount()) {
            TabProvider tabProvider2 = this.tabProvider;
            if (tabProvider2 == null) {
                view = createDefaultTabView(adapter.getPageTitle(i));
            } else {
                view = tabProvider2.createTabView(this.tabStrip, i, adapter);
            }
            if (view != null) {
                if (this.distributeEvenly) {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                    layoutParams.width = 0;
                    layoutParams.weight = 1.0f;
                }
                InternalTabClickListener internalTabClickListener2 = this.internalTabClickListener;
                if (internalTabClickListener2 != null) {
                    view.setOnClickListener(internalTabClickListener2);
                }
                this.tabStrip.addView(view);
                if (i == this.viewPager.getCurrentItem()) {
                    view.setSelected(true);
                }
                i++;
            } else {
                throw new IllegalStateException("tabView is null.");
            }
        }
    }

    public final void scrollToTab(int i, float f) {
        int i2;
        int i3;
        int i4;
        int childCount = this.tabStrip.getChildCount();
        if (childCount != 0 && i >= 0 && i < childCount) {
            boolean isLayoutRtl = Utils.isLayoutRtl(this);
            View childAt = this.tabStrip.getChildAt(i);
            int width = (int) (((float) (Utils.getWidth(childAt) + Utils.getMarginHorizontally(childAt))) * f);
            if (this.tabStrip.isIndicatorAlwaysInCenter()) {
                if (0.0f < f && f < 1.0f) {
                    View childAt2 = this.tabStrip.getChildAt(i + 1);
                    width = Math.round(f * ((float) ((Utils.getWidth(childAt) / 2) + Utils.getMarginEnd(childAt) + (Utils.getWidth(childAt2) / 2) + Utils.getMarginStart(childAt2))));
                }
                View childAt3 = this.tabStrip.getChildAt(0);
                if (isLayoutRtl) {
                    i4 = (Utils.getEnd(childAt) - Utils.getMarginEnd(childAt)) - width;
                    i3 = ((Utils.getWidth(childAt3) + Utils.getMarginEnd(childAt3)) - (Utils.getWidth(childAt) + Utils.getMarginEnd(childAt))) / 2;
                } else {
                    i4 = (Utils.getStart(childAt) - Utils.getMarginStart(childAt)) + width;
                    i3 = ((Utils.getWidth(childAt3) + Utils.getMarginStart(childAt3)) - (Utils.getWidth(childAt) + Utils.getMarginStart(childAt))) / 2;
                }
                scrollTo(i4 - i3, 0);
                return;
            }
            if (this.titleOffset == -1) {
                if (0.0f < f && f < 1.0f) {
                    View childAt4 = this.tabStrip.getChildAt(i + 1);
                    width = Math.round(f * ((float) ((Utils.getWidth(childAt) / 2) + Utils.getMarginEnd(childAt) + (Utils.getWidth(childAt4) / 2) + Utils.getMarginStart(childAt4))));
                }
                i2 = isLayoutRtl ? (((-Utils.getWidthWithMargin(childAt)) / 2) + (getWidth() / 2)) - Utils.getPaddingStart(this) : ((Utils.getWidthWithMargin(childAt) / 2) - (getWidth() / 2)) + Utils.getPaddingStart(this);
            } else {
                if (isLayoutRtl) {
                    if (i > 0 || f > 0.0f) {
                        i2 = this.titleOffset;
                    }
                } else if (i > 0 || f > 0.0f) {
                    i2 = -this.titleOffset;
                }
                i2 = 0;
            }
            int start = Utils.getStart(childAt);
            int marginStart = Utils.getMarginStart(childAt);
            scrollTo(i2 + (isLayoutRtl ? (((start + marginStart) - width) - getWidth()) + Utils.getPaddingHorizontally(this) : (start - marginStart) + width), 0);
        }
    }
}
