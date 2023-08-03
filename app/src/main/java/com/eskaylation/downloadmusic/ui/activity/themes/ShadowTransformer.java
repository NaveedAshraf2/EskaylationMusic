package com.eskaylation.downloadmusic.ui.activity.themes;

import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import androidx.viewpager.widget.ViewPager.PageTransformer;
import com.eskaylation.downloadmusic.R;
public class ShadowTransformer implements OnPageChangeListener, PageTransformer {
    public CardAdapter cardAdapter;
    public float lastOffset;
    public boolean scalingEnabled;
    public ViewPager viewPager;

    public void onPageScrollStateChanged(int i) {
    }

    public void onPageSelected(int i) {
    }

    public void transformPage(View view, float f) {
    }

    public ShadowTransformer(ViewPager viewPager2, CardAdapter cardAdapter2) {
        this.viewPager = viewPager2;
        viewPager2.addOnPageChangeListener(this);
        this.cardAdapter = cardAdapter2;
    }

    public void enableScaling(boolean z) {
        if (this.scalingEnabled && !z) {
            ConstraintLayout cardViewAt = this.cardAdapter.getCardViewAt(this.viewPager.getCurrentItem());
            if (cardViewAt != null) {
                cardViewAt.animate().scaleY(1.0f);
                cardViewAt.animate().scaleX(1.0f);
            }
        } else if (!this.scalingEnabled && z) {
            ConstraintLayout cardViewAt2 = this.cardAdapter.getCardViewAt(this.viewPager.getCurrentItem());
            if (cardViewAt2 != null) {
                cardViewAt2.animate().scaleY(1.1f);
                cardViewAt2.animate().scaleX(1.1f);
            }
        }
        this.scalingEnabled = z;
    }

    public void onPageScrolled(int i, float f, int i2) {
        int i3;
        float f2;
        this.cardAdapter.getBaseElevation();
        if (this.lastOffset > f) {
            i3 = i + 1;
            f2 = 1.0f - f;
        } else {
            f2 = f;
            i3 = i;
            i++;
        }
        if (i <= this.cardAdapter.getCount() - 1 && i3 <= this.cardAdapter.getCount() - 1) {
            ConstraintLayout cardViewAt = this.cardAdapter.getCardViewAt(i3);
            if (cardViewAt != null && this.scalingEnabled) {
                float f3 = (float) ((((double) (1.0f - f2)) * 0.1d) + 1.0d);
                cardViewAt.setScaleX(f3);
                cardViewAt.setScaleY(f3);
            }
            ConstraintLayout cardViewAt2 = this.cardAdapter.getCardViewAt(i);
            if (cardViewAt2 != null && this.scalingEnabled) {
                float f4 = (float) ((((double) f2) * 0.1d) + 1.0d);
                cardViewAt2.setScaleX(f4);
                cardViewAt2.setScaleY(f4);
            }
            this.lastOffset = f;
        }
    }
}
