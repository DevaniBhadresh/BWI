package com.bwi.onboard.ads;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bwi.onboard.R;
import com.bwi.onboard.SupperSplashActivity;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

public class NativeAdHelper {

    private static final String TAG = "NativeAdHelper";
    private NativeAd nativeAd;

    // Load native ad
    public void loadNativeAd(Context context, String adUnitId, final NativeAdListener listener) {
        AdLoader adLoader = new AdLoader.Builder(context, adUnitId)
                .forNativeAd(ad -> {
                    Log.d(TAG, "Native ad loaded successfully");
                    nativeAd = ad;
                    if (listener != null) {
                        listener.onNativeAdLoaded(ad);
                    }
                })
                .withAdListener(new com.google.android.gms.ads.AdListener() {
                    @Override
                    public void onAdFailedToLoad(LoadAdError adError) {
                        Log.e(TAG, "Native ad failed to load: " + adError.getMessage());
                        if (listener != null) {
                            listener.onNativeAdFailedToLoad(adError);
                        }
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder().build())
                .build();

        // Load the ad
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    // Show native ad in a specified container
    /*public void showNativeAd(Context context, FrameLayout adContainer) {
        if (nativeAd != null) {
            NativeAdView adView = (NativeAdView) ((SupperSplashActivity) context).getLayoutInflater().inflate(R.layout.lib_native_ad_layout, null);
            populateNativeAdView(nativeAd, adView);
            adContainer.removeAllViews();
            adContainer.addView(adView);
        } else {
            adContainer.removeAllViews();
            Log.e(TAG, "Native ad is not loaded yet.");
        }
    }*/

    public void showNativeAd(Context context, FrameLayout adContainer,int adLayoutId) {
        if (nativeAd != null) {
            Log.d(TAG, "Native ad is loaded. ");
            NativeAdView adView = (NativeAdView) ((SupperSplashActivity) context).getLayoutInflater().inflate(adLayoutId, null);
            populateNativeAdView(nativeAd, adView);
            adContainer.removeAllViews();
            adContainer.addView(adView);
        } else {
            adContainer.removeAllViews();
            Log.e(TAG, "Native ad is not loaded yet.");
        }
    }

    // Populate the native ad view with content
    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        if (adView.findViewById(R.id.ad_title) != null){
            adView.setHeadlineView(adView.findViewById(R.id.ad_title));
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        }

        if (adView.findViewById(R.id.ad_body) != null){
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (adView.findViewById(R.id.ad_cta) != null){
            adView.setCallToActionView(adView.findViewById(R.id.ad_cta));
            ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (adView.findViewById(R.id.ad_icon) != null){
            adView.setIconView(adView.findViewById(R.id.ad_icon));
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
        }

        if (adView.findViewById(R.id.ad_media) != null){
            adView.setMediaView((adView.findViewById(R.id.ad_media)));
        }


        // Populate the content
/*

        if (nativeAd.getIcon() != null) {

        } else {
            adView.getIconView().setVisibility(View.GONE);
        }
*/


        adView.setNativeAd(nativeAd);
    }

    // Native Ad Listener Interface
    public interface NativeAdListener {
        void onNativeAdLoaded(NativeAd ad);

        void onNativeAdFailedToLoad(LoadAdError adError);
    }
}
