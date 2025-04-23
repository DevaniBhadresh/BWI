package com.bwi.onboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.bwi.onboard.ads.AdConfig;
import com.bwi.onboard.ads.AdKeys;
import com.bwi.onboard.ads.AdType;
import com.bwi.onboard.ads.BannerAdHelper;
import com.bwi.onboard.ads.GoogleMobileAdsConsentManager;
import com.bwi.onboard.ads.InterstitialAdHelper;
import com.bwi.onboard.ads.NativeAdHelper;
import com.bwi.onboard.fragment.SplashFragment;
import com.bwi.onboard.remote.FORemoteConfig;
import com.bwi.onboard.utils.DialogUtils;
import com.bwi.onboard.utils.NetworkDialogContent;
import com.bwi.onboard.utils.NetworkUtils;
import com.bwi.onboard.utils.PrfsKeys;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.nativead.NativeAd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SupperSplashActivity extends AppCompatActivity implements FORemoteConfig.SingletonListener {

    private final String TAG = SupperSplashActivity.class.getSimpleName();
    private Map<String, NativeAdHelper> nativeAdHelpers = new HashMap<>();
    private Map<String,BannerAdHelper> bannerAdHelpers = new HashMap<>();
    private final Map<String, InterstitialAdHelper> interstitialAdHelpers = new HashMap<>();

    private boolean isSplashAdLoaded = false;
    private Runnable splashAdCallback;
    boolean isFirstLaunch = true;

    protected int FIRST_LAUNCH_INTERVAL = 1000;

    protected String GOOGLE_TEST_DEVICE = "";

    private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActivityLayoutId());
        Log.d(TAG, "Interval : " + FIRST_LAUNCH_INTERVAL);

        checkNetworkAndProceed();

    }

    private void checkNetworkAndProceed() {
        View customDialogView = LayoutInflater.from(this).inflate(getNoInternetDialog().getLayout(), null);

        if (!NetworkUtils.isInternetAvailable(this)) {
            DialogUtils.showNoInternetDialog(this, customDialogView,
                    getNoInternetDialog().getTitle(),
                    getNoInternetDialog().getMessage(),
                    () -> checkNetworkAndProceed());
            return;
        }
        getConcent();
    }

    private void initializeSplashFlow() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return;
        }

        List<String> testDevices = new ArrayList<>();
        testDevices.add(AdRequest.DEVICE_ID_EMULATOR);
        testDevices.add(GOOGLE_TEST_DEVICE);
        RequestConfiguration requestConfiguration = new RequestConfiguration.Builder().setTestDeviceIds(testDevices).build();
        MobileAds.setRequestConfiguration(requestConfiguration);

        new Thread(() -> {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this, initializationStatus -> {
            });

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences preferences = getSharedPreferences(PrfsKeys.PREF_NAME, MODE_PRIVATE);
                    isFirstLaunch = preferences.getBoolean(PrfsKeys.KEY_FIRST_LAUNCH, true);

                    if (PrfsKeys.isRemoteConfigFetched(SupperSplashActivity.this))
                        FIRST_LAUNCH_INTERVAL = 0;
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadFragment(getSplashFragment());
                            configureAndLoadAds();
                        }
                    }, FIRST_LAUNCH_INTERVAL); // 2000 milliseconds = 2 seconds
                }
            });
        }).start();


    }

    private void getConcent() {
        GoogleMobileAdsConsentManager.getInstance(this).gatherConsent(this, GOOGLE_TEST_DEVICE, consentError -> {
            if (consentError != null) {
                // Consent not obtained in current session.
                Log.w(TAG, String.format("%s: %s", consentError.getErrorCode(), consentError.getMessage()));
            }


            if (GoogleMobileAdsConsentManager.getInstance(this).canRequestAds()) {
                Log.d(TAG, "canRequestAds : 2");
                initializeSplashFlow();

            }
        });

        // This sample attempts to load ads using consent obtained in the previous session.
        if (GoogleMobileAdsConsentManager.getInstance(this).canRequestAds()) {
            //createTimer(COUNTER_TIME_MILLISECONDS);
            Log.d(TAG, "canRequestAds : 3");
            initializeSplashFlow();
        }
    }

    @Override
    public void onRemoteFetch(boolean isFetched) {

        if (isFetched) {
            PrfsKeys.setRemoteConfigFetched(SupperSplashActivity.this, true);
            //configureAndLoadAds();
            Log.d(TAG, "Remote Config (Splash) : Successfully fetched");
            Log.d(TAG, FORemoteConfig.Const.AD_ENABLED + " : " + FORemoteConfig.getInstance().isAdsEnabled());
            Log.d(TAG, AdKeys.SPLASH_AD + " : " + FORemoteConfig.getInstance().getShowSplashAd());
            Log.d(TAG, AdKeys.LANGUAGE_NATIVE_AD_1 + " : " + FORemoteConfig.getInstance().getShowLanguageNativeAd1());
            Log.d(TAG, AdKeys.LANGUAGE_NATIVE_AD_2 + " : " + FORemoteConfig.getInstance().getShowLanguageNativeAd2());
            Log.d(TAG, AdKeys.INTRO_AD_1 + " : " + FORemoteConfig.getInstance().getShowIntroAd1());
            Log.d(TAG, AdKeys.INTRO_AD_2 + " : " + FORemoteConfig.getInstance().getShowIntroAd2());
            Log.d(TAG, AdKeys.INTRO_AD_3 + " : " + FORemoteConfig.getInstance().getShowIntroAd3());
            Log.d(TAG, FORemoteConfig.Const.LAYOUT_NATIVE_AD_SPLASH + " : " + FORemoteConfig.getInstance().getLayoutNativeAdSplash());
            Log.d(TAG, FORemoteConfig.Const.LAYOUT_NATIVE_AD_LANGUAGE_1 + " : " + FORemoteConfig.getInstance().getLayoutNativeAdLanguage1());
            Log.d(TAG, FORemoteConfig.Const.LAYOUT_NATIVE_AD_LANGUAGE_2 + " : " + FORemoteConfig.getInstance().getLayoutNativeAdLanguage1());
            Log.d(TAG, FORemoteConfig.Const.LAYOUT_NATIVE_AD_INTRO + " : " + FORemoteConfig.getInstance().getLayoutNativeAdIntro());
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
        Log.d(TAG, "configure Size : " + getAdConfigurations().size());
        for (Map.Entry<String, AdConfig> entry : getAdConfigurations().entrySet()) {
            String key = entry.getKey();
            AdConfig config = entry.getValue();

            // On subsequent launches — only load SPLASH_AD and SPLASH_INTER_AD
            if (!isFirstLaunch && !(key.equals(AdKeys.SPLASH_AD) || key.equals(AdKeys.SPLASH_INTER_AD))) {
                continue;
            }

            switch (config.getAdType()) {
                case AdType.AD_NATIVE:
                    NativeAdHelper adHelper = new NativeAdHelper();
                    nativeAdHelpers.put(key, adHelper);
                    //Log.d(SupperSplashActivity.class.getSimpleName(), "canShowAd : " + config.canShowAd());
                    if (config.canShowAd() && GoogleMobileAdsConsentManager.getInstance(this).canRequestAds()) {
                        adHelper.loadNativeAd(this, config.getAdUnitId(), new NativeAdHelper.NativeAdListener() {
                            @Override
                            public void onNativeAdLoaded(NativeAd ad) {
                                if (key.equals(AdKeys.SPLASH_AD)) {
                                    isSplashAdLoaded = true;
                                    if (splashAdCallback != null) splashAdCallback.run();
                                }
                            }

                            @Override
                            public void onNativeAdFailedToLoad(LoadAdError adError) {
                                if (key.equals(AdKeys.SPLASH_AD)) isSplashAdLoaded = false;
                            }
                        });
                    }
                    break;

                case (AdType.AD_INTERSTITIAL):
                    InterstitialAdHelper interstitialHelper = new InterstitialAdHelper();
                    interstitialAdHelpers.put(key, interstitialHelper);
                    if (config.canShowAd() && GoogleMobileAdsConsentManager.getInstance(this).canRequestAds())
                        interstitialHelper.loadAd(this, config.getAdUnitId());
                    break;

                case AdType.AD_BANNER:
                    BannerAdHelper bannerHelper = new BannerAdHelper();
                    bannerAdHelpers.put(key, bannerHelper);
                    if (config.canShowAd() && GoogleMobileAdsConsentManager.getInstance(this).canRequestAds()) {

                        bannerHelper.loadBannerAd(this, config.getAdUnitId(), new BannerAdHelper.BannerAdListener() {
                            @Override
                            public void onBannerAdLoaded() {
                                Log.d(TAG, "Banner ad loaded for key: " + key);
                            }

                            @Override
                            public void onBannerAdFailedToLoad(LoadAdError adError) {
                                Log.e(TAG, "Failed to load banner ad for key: " + key + ". Error: " + adError.getMessage());
                            }
                        });
                    } else {
                        Log.e(TAG, "Ad container not found for key: " + key);
                    }

                    break;
            }

        }
    }

    public NativeAdHelper getNativeAdHelper(String key) {
        return nativeAdHelpers.get(key); // Retrieve the helper from the map
    }

    public BannerAdHelper getBannerAdHelper(String key) {
        return bannerAdHelpers.get(key); // Retrieve the helper from the map
    }

    protected abstract Map<String, AdConfig> getAdConfigurations();

    /*protected abstract Class<? extends Fragment> getSplashFragmentClass();*/

    protected abstract SplashFragment getSplashFragment();

    protected abstract Fragment getLanguageFragmentClass();

    protected abstract Fragment getIntroFragmentClass();
    protected abstract NetworkDialogContent getNoInternetDialog();

    @LayoutRes
    protected abstract int getActivityLayoutId();

    public void moveToLanguageSelection() {
        if (isFirstLaunch) {
            // Load SplashFragment dynamically
            loadFragment(getLanguageFragmentClass());
        } else {
            //completeOnboarding();
            showInterstitial(
                    AdKeys.SPLASH_INTER_AD,
                    this::completeOnboarding
            );
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

    public void showInterstitial(String key, Runnable onFinish) {
        InterstitialAdHelper helper = interstitialAdHelpers.get(key);
        if (helper != null && helper.isAdReady()) {
            helper.show(this, onFinish::run);
        } else {
            onFinish.run(); // fallback if ad not ready
        }
    }


}
