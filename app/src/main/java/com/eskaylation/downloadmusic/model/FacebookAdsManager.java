package com.eskaylation.downloadmusic.model;


import android.app.Activity;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.R;
import com.eskaylation.downloadmusic.ui.activity.plash.LoadingActivity;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;

import java.util.ArrayList;
import java.util.List;


public class FacebookAdsManager {

    static AdView adViewF;
    static InterstitialAd interstitialAd;


    private static boolean isAdsAvailable = false;
    public interface AdCloseListener {
        void adClosed();
    }

    public static void showBannerAd(Activity activity, FrameLayout adContainer){


        try {
            adViewF = new AdView(activity, LoadingActivity.BANNER_ID_FACEBOOK, com.facebook.ads.AdSize.BANNER_HEIGHT_50);

            adContainer.addView(adViewF);
            AdListener adListener = new AdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {
// Ad error callback

                }

                @Override
                public void onAdLoaded(Ad ad) {

// Ad loaded callback
                }

                @Override
                public void onAdClicked(Ad ad) {
// Ad clicked callback
                }

                @Override
                public void onLoggingImpression(Ad ad) {
// Ad impression logged callback
                }
            };
            adViewF.loadAd(adViewF.buildLoadAdConfig().withAdListener(adListener).build());


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void showNext(final Activity activity,AdCloseListener adCloseListener ){

        try {

            ProgressDialog dialog = new ProgressDialog(activity);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            interstitialAd = new InterstitialAd(activity, LoadingActivity.INTERS_ID_FACEBOOK);
            // Create listeners for the Interstitial Ad
            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    // Interstitial ad displayed callback

                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    // Interstitial dismissed callback
                    adCloseListener.adClosed();


                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    // Ad error callback
                    dialog.dismiss();
                    adCloseListener.adClosed();
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Interstitial ad is loaded and ready to be displayed
                    // Show the ad
                    dialog.dismiss();
                    if(interstitialAd.isAdLoaded()){
                        interstitialAd.show();
                    }

                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Ad clicked callback

                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Ad impression logged callback

                }
            };

            // For auto play video ads, it's recommended to load the ad
            // at least 30 seconds before it is shown
            interstitialAd.loadAd(
                    interstitialAd.buildLoadAdConfig()
                            .withAdListener(interstitialAdListener)
                            .build());


        } catch (Exception e) {
            e.printStackTrace();
            adCloseListener.adClosed();
        }

    }

    private static NativeAdLayout nativeAdLayout;
    private static LinearLayout adView;
    private static NativeAd nativeAd;
    public static void showNativeAd(final Activity activity) {

        nativeAd = new NativeAd(activity, LoadingActivity.NATIVE_ID_FACEBOOK);
        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets

            }

            @Override
            public void onError(Ad ad, AdError adError) {

                // Native ad failed to load

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                inflateAd(nativeAd, activity);

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked

            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression

            }
        };

        // Request an ad
        nativeAd.loadAd(
                nativeAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());

    }

    private static void inflateAd(NativeAd nativeAd,Activity activity) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeAdLayout = activity.findViewById(R.id.native_ad_container);
        CardView cardView = activity.findViewById(R.id.card);
        cardView.setVisibility(View.VISIBLE);
        LayoutInflater inflater = LayoutInflater.from(activity);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.faceboob_native, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = activity.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView, nativeAdMedia, nativeAdIcon, clickableViews);
    }


}

