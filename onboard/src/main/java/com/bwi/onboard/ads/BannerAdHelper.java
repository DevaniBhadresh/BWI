package com.bwi.onboard.ads;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

public class BannerAdHelper {

    private static final String TAG = "BannerAdHelper";
    private AdView adView;
    private FrameLayout adContainer;

    /**
     * Load adaptive banner ad only — without adding to view hierarchy.
     */
    public void loadBannerAd(Activity activity, String adUnitId, final BannerAdListener listener) {
        adView = new AdView(activity);
        adView.setAdUnitId(adUnitId);
        adView.setAdSize(getAdSize(activity));

        AdRequest adRequest = new AdRequest.Builder().build();

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(TAG, "Banner Ad : Id : " + adUnitId + "Loaded");
                if (listener != null) listener.onBannerAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.d(TAG, "Inter Ad : Id : " + adUnitId + "Failed" + adError.getMessage());
                if (listener != null) listener.onBannerAdFailedToLoad(adError);
            }
        });

        adView.loadAd(adRequest);
    }

    /**
     * Show banner ad by adding it to the given container.
     * Make sure loadBannerAd() is called before this.
     */
    public void showBannerAd(FrameLayout container) {
        if (adView != null) {
            this.adContainer = container;
            adContainer.removeAllViews();
            adContainer.addView(adView);
        } else {
        }
    }

    /**
     * Calculate adaptive AdSize based on device width.
     */
    private AdSize getAdSize(Activity activity) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);

        float density = outMetrics.density;
        int adWidthPixels = outMetrics.widthPixels;
        int adWidth = (int) (adWidthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    /**
     * Clean up adView to prevent memory leaks.
     */
    public void destroy() {
        if (adView != null) {
            adView.destroy();
        }
    }

    /**
     * Banner Ad Listener Interface.
     */
    public interface BannerAdListener {
        void onBannerAdLoaded();
        void onBannerAdFailedToLoad(LoadAdError adError);
    }
}
