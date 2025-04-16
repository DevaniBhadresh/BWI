package com.bwi.onboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.bwi.onboard.ads.AdConfig;
import com.bwi.onboard.ads.AdKeys;
import com.bwi.onboard.ads.NativeAdHelper;
import com.bwi.onboard.fragment.SplashFragment;
import com.bwi.onboard.remote.FORemoteConfig;
import com.bwi.onboard.utils.PrfsKeys;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;

import java.util.HashMap;
import java.util.Map;

public abstract class SupperSplashActivity extends AppCompatActivity implements FORemoteConfig.SingletonListener {

    private final String TAG = SupperSplashActivity.class.getSimpleName();
    private Map<String, NativeAdHelper> nativeAdHelpers = new HashMap<>();
    private boolean isSplashAdLoaded = false;
    private Runnable splashAdCallback;
    boolean isFirstLaunch = true;

    protected int FIRST_LAUNCH_INTERVAL = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayoutId());
        Log.d(TAG, "Interval : " + FIRST_LAUNCH_INTERVAL);
        SharedPreferences preferences = getSharedPreferences(PrfsKeys.PREF_NAME, MODE_PRIVATE);
        isFirstLaunch = preferences.getBoolean(PrfsKeys.KEY_FIRST_LAUNCH, true);

        if (PrfsKeys.isRemoteConfigFetched(SupperSplashActivity.this))
            FIRST_LAUNCH_INTERVAL = 0;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // if (isFirstLaunch) {
                // Load SplashFragment dynamically
                loadFragment(getSplashFragment());
                //} else {
                //    completeOnboarding();
                //}

                // Load Ads
                configureAndLoadAds();
            }
        }, FIRST_LAUNCH_INTERVAL); // 2000 milliseconds = 2 seconds


    }

    @Override
    public void onRemoteFetch(boolean isFetched) {

        if (isFetched) {
            PrfsKeys.setRemoteConfigFetched(SupperSplashActivity.this, true);
            configureAndLoadAds();
            Log.d(TAG, "Remote Config (Splash) : Successfully fetched");
            Log.d(TAG, FORemoteConfig.Const.ADS_ENABLED + " : " + FORemoteConfig.getInstance().isAdsEnabled());
            Log.d(TAG, AdKeys.SPLASH_NATIVE_AD + " : " + FORemoteConfig.getInstance().getSplashNativeAd());
            Log.d(TAG, AdKeys.LANGUAGE_NATIVE_AD_1 + " : " + FORemoteConfig.getInstance().getLanguageNativeAd1());
            Log.d(TAG, AdKeys.LANGUAGE_NATIVE_AD_2 + " : " + FORemoteConfig.getInstance().getLanguageNativeAd2());
            Log.d(TAG, AdKeys.INTRO_NATIVE_AD_1 + " : " + FORemoteConfig.getInstance().getIntroNativeAd1());
            Log.d(TAG, AdKeys.INTRO_NATIVE_AD_2 + " : " + FORemoteConfig.getInstance().getIntroNativeAd2());
            Log.d(TAG, AdKeys.INTRO_NATIVE_AD_3 + " : " + FORemoteConfig.getInstance().getIntroNativeAd3());
            Log.d(TAG, FORemoteConfig.Const.LAYOUT_SPLASH_NATIVE_AD + " : " + FORemoteConfig.getInstance().getLayoutSplashNativeAd());
            Log.d(TAG, FORemoteConfig.Const.LAYOUT_LANGUAGE_NATIVE_AD_1 + " : " + FORemoteConfig.getInstance().getLayoutLanguageNativeAd1());
            Log.d(TAG, FORemoteConfig.Const.LAYOUT_LANGUAGE_NATIVE_AD_2 + " : " + FORemoteConfig.getInstance().getLayoutLanguageNativeAd1());
            Log.d(TAG, FORemoteConfig.Const.LAYOUT_INTRO_NATIVE_AD + " : " + FORemoteConfig.getInstance().getLayoutIntroNativeAd());
        } else {
            Log.d(TAG, "Remote Config (Splash) : Failed to fetched");
        }
    }

    private void loadFragment(Fragment fragmentClass) {


        int slideInLeft = R.anim.slide_in_left;
        int slideOutLeft = R.anim.slide_out_left;

        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (!(fragmentClass instanceof SplashFragment)) {
                transaction.setCustomAnimations(slideInLeft, slideOutLeft);
            }

            transaction.replace(R.id.fragment_container, fragmentClass);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void configureAndLoadAds() {
        for (Map.Entry<String, AdConfig> entry : getAdConfigurations().entrySet()) {
            String key = entry.getKey();
            AdConfig config = entry.getValue();

            NativeAdHelper adHelper = new NativeAdHelper();
            nativeAdHelpers.put(key, adHelper);
            //Log.d(SupperSplashActivity.class.getSimpleName(), "canShowAd : " + config.canShowAd());
            if (config.canShowAd()) {
                adHelper.loadNativeAd(this, config.getAdUnitId(), new NativeAdHelper.NativeAdListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd ad) {
                        if (key.equals(AdKeys.SPLASH_NATIVE_AD)) {
                            isSplashAdLoaded = true;
                            if (splashAdCallback != null) splashAdCallback.run();
                        }
                    }

                    @Override
                    public void onNativeAdFailedToLoad(LoadAdError adError) {
                        if (key.equals(AdKeys.SPLASH_NATIVE_AD)) isSplashAdLoaded = false;
                    }
                });
            }

        }
    }

    public NativeAdHelper getNativeAdHelper(String key) {
        //Log.d(SupperSplashActivity.class.getSimpleName(), "Ad Key : " + key + " Ad ID : " + getAdConfigurations().get(key).getAdUnitId());
        return nativeAdHelpers.get(key); // Retrieve the helper from the map
    }

    protected abstract Map<String, AdConfig> getAdConfigurations();

    /*protected abstract Class<? extends Fragment> getSplashFragmentClass();*/

    protected abstract SplashFragment getSplashFragment();

    protected abstract Fragment getLanguageFragmentClass();

    protected abstract Fragment getIntroFragmentClass();

    @LayoutRes
    protected abstract int getActivityLayoutId();

    public void moveToLanguageSelection() {
        if (isFirstLaunch) {
            // Load SplashFragment dynamically
            loadFragment(getLanguageFragmentClass());
        } else {
            completeOnboarding();
        }


    }

    public void moveToIntroductionSlider() {
        loadFragment(getIntroFragmentClass());
    }

    public abstract void completeOnboarding();


    public boolean isSplashAdLoaded() {
        return isSplashAdLoaded;
    }

    public void setSplashAdCallback(Runnable callback) {
        splashAdCallback = callback;
        if (isSplashAdLoaded) {
            callback.run();
        } else {
            callback.run();
        }
    }


}
