package com.eskaylation.downloadmusic.model;


import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.window.SplashScreen;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.adapter.MaxAdapter;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.ui.activity.plash.LoadingActivity;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class AdsManager {

    static MaxAdView adView;
    private static MaxInterstitialAd interstitialAdAPL;
    private AdCloseListener adCloseListener;
    private static boolean isAdsAvailable = false;
    public interface AdCloseListener {
        void onAdClosed();
    }
    public static void init(final Activity activity, Intent i) {
        AudienceNetworkAds.initialize(activity);
        if(LoadingActivity.Networkxx.equalsIgnoreCase("admob")){
            MobileAds.initialize(activity, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                    //  loadADMOB(activity);

                    AdsApplication.getInstance().appOpenManager.setOpenAdsListener(new AppOpenManager.OpenAdsListener() {
                        @Override
                        public void onLoaded() {
                            android.util.Log.e("AAAAAAAAA", "onLoaded: ");
                            AdsApplication.getInstance().appOpenManager.showAdIfAvailable();
                        }

                        @Override
                        public void onError() {
                            android.util.Log.e("AAAAAAAAA", "onError: ");
                            activity.startActivity(i);
                            load(activity);
                            activity.finish();


                        }

                        @Override
                        public void onClosed() {
                            android.util.Log.e("AAAAAAAAA", "onClosed: ");
                            activity.startActivity(i);
                            AdsManager.load(activity);
                            activity.finish();


                        }
                    });

                }
            });


        }
        else {

            AppLovinSdk.getInstance(activity).setMediationProvider("max");
            AppLovinSdk.initializeSdk(activity, new AppLovinSdk.SdkInitializationListener() {
                @Override
                public void onSdkInitialized(AppLovinSdkConfiguration config) {
                    AdsManager.load(activity);

                }
            });


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    activity.startActivity(i);
                    //AdsManager.load(activity);
                    activity.finish();
                }
            },3000);
        }



    }


    public static void showBanner(Activity activity,FrameLayout adContainer) {


        if (LoadingActivity.Networkxx.equalsIgnoreCase("admob")) {
            Admob_AdsManager.showBannerAd(activity, adContainer);

        } else if (LoadingActivity.Networkxx.equalsIgnoreCase("applovin")) {
            if (LoadingActivity.BANNER_ID_APPLOVIN != null) {

                adContainer.setVisibility(View.GONE);
                adView = new MaxAdView(LoadingActivity.BANNER_ID_APPLOVIN, activity);
                adView.setListener(new MaxAdViewAdListener() {
                    @Override
                    public void onAdExpanded(MaxAd ad) {

                    }

                    @Override
                    public void onAdCollapsed(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoaded(MaxAd ad) {
                        adContainer.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAdDisplayed(MaxAd ad) {

                    }

                    @Override
                    public void onAdHidden(MaxAd ad) {

                    }

                    @Override
                    public void onAdClicked(MaxAd ad) {

                    }

                    @Override
                    public void onAdLoadFailed(String adUnitId, MaxError error) {

                    }

                    @Override
                    public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                    }
                });
                adContainer.addView(adView);
                adView.loadAd();

            }

        } else if (LoadingActivity.Networkxx.equalsIgnoreCase("facebook")) {
            FacebookAdsManager.showBannerAd(activity, adContainer);
        }
        else{
            adContainer.setVisibility(View.GONE);
        }
    }

    public static void load(final Activity activity) {

        if(LoadingActivity.Networkxx.equalsIgnoreCase("admob")){

            Admob_AdsManager.loadInterstitialAd(activity);
        }else if(LoadingActivity.Networkxx.equalsIgnoreCase("applovin")){

            if (!isAdsAvailable) {
                if (LoadingActivity.INTERS_ID_APPLOVIN != null) {
                    interstitialAdAPL = new MaxInterstitialAd(LoadingActivity.INTERS_ID_APPLOVIN
                            , activity);
                    interstitialAdAPL.setListener(new MaxAdViewAdListener() {
                        @Override
                        public void onAdExpanded(MaxAd ad) {

                        }

                        @Override
                        public void onAdCollapsed(MaxAd ad) {


                        }


                        @Override
                        public void onAdLoaded(MaxAd ad) {
                            // TODO
                            isAdsAvailable = true;
                        }

                        @Override
                        public void onAdDisplayed(MaxAd ad) {

                        }

                        @Override
                        public void onAdHidden(MaxAd ad) {


                        }

                        @Override
                        public void onAdClicked(MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(String adUnitId, MaxError error) {

                            isAdsAvailable = false;
                        }

                        @Override
                        public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                        }
                    });
                    interstitialAdAPL.loadAd();
                }

            }
        }else if(LoadingActivity.Networkxx.equalsIgnoreCase("facebook")){

        }

    }

    public static void showNext(final Activity activity, final AdCloseListener adCloseListener) {

        try {

            if(LoadingActivity.Networkxx.equalsIgnoreCase("admob")){

                Admob_AdsManager.showNext(activity, new Admob_AdsManager.AdCloseListener() {
                    @Override
                    public void onAdClosed() {
                        adCloseListener.onAdClosed();
                    }
                });

            }else if(LoadingActivity.Networkxx.equalsIgnoreCase("facebook")){

                FacebookAdsManager.showNext(activity, new FacebookAdsManager.AdCloseListener() {
                    @Override
                    public void adClosed() {
                        adCloseListener.onAdClosed();
                    }
                });


            } else if(LoadingActivity.Networkxx.equalsIgnoreCase("applovin")){

                if (interstitialAdAPL.isReady() && isAdsAvailable) {
                    interstitialAdAPL.showAd();
                    interstitialAdAPL.setListener(new MaxAdViewAdListener() {
                        @Override
                        public void onAdExpanded(MaxAd ad) {

                        }

                        @Override
                        public void onAdCollapsed(MaxAd ad) {


                        }


                        @Override
                        public void onAdLoaded(MaxAd ad) {

                        }

                        @Override
                        public void onAdDisplayed(MaxAd ad) {

                        }

                        @Override
                        public void onAdHidden(MaxAd ad) {

                            isAdsAvailable = false;
                            adCloseListener.onAdClosed();
                            load(activity);

                        }

                        @Override
                        public void onAdClicked(MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(String adUnitId, MaxError error) {

                        }

                        @Override
                        public void onAdDisplayFailed(MaxAd ad, MaxError error) {


                        }
                    });
                } else {

                    isAdsAvailable = false;
                    load(activity);
                    adCloseListener.onAdClosed();
                }

            }
            else {
               // load(activity);
                adCloseListener.onAdClosed();
            }

        } catch (Exception e) {
            e.printStackTrace();
            isAdsAvailable = false;
            load(activity);
            adCloseListener.onAdClosed();
        }


    }
    static MaxAd nativeAd;
    static MaxNativeAdLoader nativeAdLoader;
    public static void showNative(final Activity activity) {


        if(LoadingActivity.Networkxx.equalsIgnoreCase("admob")){
            Admob_AdsManager.showNativeBanner(activity);

        } else if(LoadingActivity.Networkxx.equalsIgnoreCase("facebook")){
            FacebookAdsManager.showNativeAd(activity);
        }
        else if(LoadingActivity.Networkxx.equalsIgnoreCase("applovin")){

            if (LoadingActivity.NATIVE_ID_APPLOVIN != null) {

                final FrameLayout nativeAdContainer  = activity.findViewById(R.id.applovin_native);
                final CardView cardView  = activity.findViewById(R.id.card);


                nativeAdLoader = new MaxNativeAdLoader( LoadingActivity.NATIVE_ID_APPLOVIN, activity );
                nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                    @Override
                    public void onNativeAdLoaded(MaxNativeAdView maxNativeAdView, MaxAd maxAd) {
                        super.onNativeAdLoaded(maxNativeAdView, maxAd);

                        // Cleanup any pre-existing native ad to prevent memory leaks.
                        nativeAdContainer .setVisibility(View.VISIBLE);
                        cardView .setVisibility(View.VISIBLE);

                        if ( nativeAd != null )
                        {
                            nativeAdLoader.destroy( nativeAd );
                        }

                        // Save ad for cleanup.
                        nativeAd = maxAd;

                        // Add ad view to view.
                        nativeAdContainer.removeAllViews();
                        nativeAdContainer.addView( maxNativeAdView );

                    }

                    @Override
                    public void onNativeAdLoadFailed(String s, MaxError maxError) {
                        super.onNativeAdLoadFailed(s, maxError);

                    }
                });

                nativeAdLoader.loadAd();



            }
        }
        
    }
    public static void showNativeDialog(final Activity activity,View view) {


        if(LoadingActivity.Networkxx.equalsIgnoreCase("admob")){
            Admob_AdsManager.showNativeBanner(activity,view);

        }else if(LoadingActivity.Networkxx.equalsIgnoreCase("applovin")){
            if (LoadingActivity.NATIVE_ID_APPLOVIN != null) {

                final FrameLayout nativeAdContainer  = view.findViewById(R.id.applovin_native);
                final CardView cardView  = view.findViewById(R.id.card);

                nativeAdLoader = new MaxNativeAdLoader( LoadingActivity.NATIVE_ID_APPLOVIN, activity );
                nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                    @Override
                    public void onNativeAdLoaded(MaxNativeAdView maxNativeAdView, MaxAd maxAd) {
                        super.onNativeAdLoaded(maxNativeAdView, maxAd);

                        // Cleanup any pre-existing native ad to prevent memory leaks.
                        nativeAdContainer .setVisibility(View.VISIBLE);
                        cardView .setVisibility(View.VISIBLE);
                        if ( nativeAd != null )
                        {
                            nativeAdLoader.destroy( nativeAd );
                        }

                        // Save ad for cleanup.
                        nativeAd = maxAd;

                        // Add ad view to view.
                        nativeAdContainer.removeAllViews();
                        nativeAdContainer.addView( maxNativeAdView );
                    }

                    @Override
                    public void onNativeAdLoadFailed(String s, MaxError maxError) {
                        super.onNativeAdLoadFailed(s, maxError);
                    }
                });

                nativeAdLoader.loadAd();



            }

        }





    }

}

