package com.bwi.onboard.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class InterstitialAdHelper {
    private static final String TAG = "InterstitialAdHelper";
    private InterstitialAd interstitialAd;
    private boolean isAdLoaded = false;

    public interface InterstitialAdListener {
        void onAdClosed();
    }

    public void loadAd(Context context, String adUnitId) {
        AdRequest request = new AdRequest.Builder().build();
        InterstitialAd.load(context, adUnitId, request, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd ad) {
                Log.d(TAG, "Inter Ad : Id : " + adUnitId + "Loaded");
                interstitialAd = ad;
                isAdLoaded = true;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError error) {
                Log.d(TAG, "Inter Ad : Id : " + adUnitId + "Failed : " + error.getMessage());
                interstitialAd = null;
                isAdLoaded = false;
            }
        });
    }

    public boolean isAdReady() {
        return isAdLoaded && interstitialAd != null;
    }

    public void show(Activity activity, InterstitialAdListener listener) {
        if (interstitialAd != null) {
            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    interstitialAd = null;
                    isAdLoaded = false;
                    listener.onAdClosed();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    interstitialAd = null;
                    isAdLoaded = false;
                    listener.onAdClosed();
                }
            });
            interstitialAd.show(activity);
        } else {
            listener.onAdClosed(); // Continue flow even if ad not shown
        }
    }
}
