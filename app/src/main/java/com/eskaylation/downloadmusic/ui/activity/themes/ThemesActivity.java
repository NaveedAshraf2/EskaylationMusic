package com.eskaylation.downloadmusic.ui.activity.themes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.eskaylation.downloadmusic.base.BaseActivity;
import com.eskaylation.downloadmusic.model.AdsManager;
import com.eskaylation.downloadmusic.ui.activity.main.MainActivity;



import com.eskaylation.downloadmusic.utils.PreferenceUtils;
import com.eskaylation.downloadmusic.widget.ZoomOutPageTransformer;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;
import com.eskaylation.downloadmusic.R;
public class ThemesActivity extends BaseActivity {
    @BindView(R.id.btnSetTheme)
    public Button btnSetTheme;
    @BindView(R.id.coordinator)
    public CoordinatorLayout coordinator;
    @BindView(R.id.imgPreview)
    public ImageView imgPreview;
    public CardFragmentPagerAdapter mImageAdapter;
    @BindView(R.id.pagerIndicator)
    public WormDotsIndicator pagerIndicator;
    public PreferenceUtils preferenceUtils;
    @BindView(R.id.toolbarTheme)
    public Toolbar toolbarTheme;
    @BindView(R.id.viewPagerHome)
    public ViewPager viewPager;
   // public Button btnSetTheme

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_themes);
        init();
    }

    public void init() {
        ButterKnife.bind(this);
        this.preferenceUtils = PreferenceUtils.getInstance(this);
        this.mImageAdapter = new CardFragmentPagerAdapter(this, getSupportFragmentManager(), 8.0f);
        this.viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        ShadowTransformer shadowTransformer = new ShadowTransformer(this.viewPager, this.mImageAdapter);
        shadowTransformer.enableScaling(true);
        this.viewPager.setAdapter(this.mImageAdapter);
        this.viewPager.setPageTransformer(false, shadowTransformer);
        this.viewPager.setOffscreenPageLimit(this.mImageAdapter.getCount());
        btnSetTheme.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClick1();
            }
        });
        this.pagerIndicator.setViewPager(this.viewPager);
        this.viewPager.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
                ThemesActivity themesActivity = ThemesActivity.this;
                themesActivity.setBackgroundBlur(themesActivity, themesActivity.coordinator, i);
            }
        });
        this.viewPager.setCurrentItem(this.preferenceUtils.getThemesPosition(), true);
        this.toolbarTheme.setNavigationOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                ThemesActivity.this.lambda$init$0$ThemesActivity(view);
            }
        });
    }

    public /* synthetic */ void lambda$init$0$ThemesActivity(View view) {
        super.onBackPressed();
    }

    public void OnClick1() {
        this.preferenceUtils.setThemesPosition(this.viewPager.getCurrentItem());

        ThemesActivity themesActivity = ThemesActivity.this;
        themesActivity.showMessage(themesActivity.getString(R.string.done));
        ThemesActivity themesActivity2 = ThemesActivity.this;
        themesActivity2.startActivity(new Intent(themesActivity2, MainActivity.class));
        ThemesActivity.this.finish();
        
    }
}
