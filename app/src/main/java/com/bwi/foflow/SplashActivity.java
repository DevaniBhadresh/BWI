package com.bwi.foflow;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.bwi.onboard.SupperSplashActivity;
import com.bwi.onboard.ads.AdConfig;
import com.bwi.onboard.ads.AdKeys;
import com.bwi.onboard.ads.AdType;
import com.bwi.onboard.fragment.IntroductionSliderFragment;
import com.bwi.onboard.fragment.LanguageSelectionFragment;
import com.bwi.onboard.fragment.SplashFragment;
import com.bwi.onboard.remote.FORemoteConfig;
import com.bwi.onboard.utils.Language;
import com.bwi.onboard.utils.NetworkDialogContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashActivity extends SupperSplashActivity {

    private final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FIRST_LAUNCH_INTERVAL = 500;
        GOOGLE_TEST_DEVICE = "EE46CDBD07D246897BC6035C8066D757";
        super.onCreate(savedInstanceState);

        App.foRemoteConfig.setInterfaz(this);
        Log.d(TAG, "Theme : " + getDefaultDeviceTheme());
    }

    @Override
    public void onRemoteFetch(boolean isFetched) {
        super.onRemoteFetch(isFetched);
        Log.d(TAG, "Remote config fetch successfully");
        getAdConfigurations();
    }

    @Override
    protected Map<String, AdConfig> getAdConfigurations() {
        Map<String, AdConfig> adConfigs = new HashMap<>();
        if (FORemoteConfig.getInstance().isAdsEnabled()) {
            if (FORemoteConfig.getInstance().getAdTypeSplash().equals(AdType.AD_NATIVE)) {
                adConfigs.put(AdKeys.SPLASH_AD, new AdConfig("ca-app-pub-3940256099942544/2247696110", FORemoteConfig.getInstance().getShowSplashAd(), AdType.AD_NATIVE));
            } else {
                adConfigs.put(AdKeys.SPLASH_AD, new AdConfig("ca-app-pub-3940256099942544/9214589741", FORemoteConfig.getInstance().getShowSplashAd(), AdType.AD_BANNER));
            }

            adConfigs.put(AdKeys.LANGUAGE_NATIVE_AD_1, new AdConfig("ca-app-pub-3940256099942544/2247696110", FORemoteConfig.getInstance().getShowLanguageNativeAd1(), AdType.AD_NATIVE));
            adConfigs.put(AdKeys.LANGUAGE_NATIVE_AD_2, new AdConfig("ca-app-pub-3940256099942544/2247696110", FORemoteConfig.getInstance().getShowLanguageNativeAd2(), AdType.AD_NATIVE));

            if (FORemoteConfig.getInstance().getAdTypeIntro().equals(AdType.AD_NATIVE)) {
                adConfigs.put(AdKeys.INTRO_AD_1, new AdConfig("ca-app-pub-3940256099942544/2247696110", FORemoteConfig.getInstance().getShowIntroAd1(), AdType.AD_NATIVE));
                adConfigs.put(AdKeys.INTRO_AD_2, new AdConfig("ca-app-pub-3940256099942544/2247696110", FORemoteConfig.getInstance().getShowIntroAd2(), AdType.AD_NATIVE));
                adConfigs.put(AdKeys.INTRO_AD_3, new AdConfig("ca-app-pub-3940256099942544/2247696110", FORemoteConfig.getInstance().getShowIntroAd3(), AdType.AD_NATIVE));
            } else {
                adConfigs.put(AdKeys.INTRO_AD_1, new AdConfig("ca-app-pub-3940256099942544/9214589741", FORemoteConfig.getInstance().getShowIntroAd1(), AdType.AD_BANNER));
                adConfigs.put(AdKeys.INTRO_AD_2, new AdConfig("ca-app-pub-3940256099942544/9214589741", FORemoteConfig.getInstance().getShowIntroAd2(), AdType.AD_BANNER));
                adConfigs.put(AdKeys.INTRO_AD_3, new AdConfig("ca-app-pub-3940256099942544/9214589741", FORemoteConfig.getInstance().getShowIntroAd3(), AdType.AD_BANNER));
            }

            adConfigs.put(AdKeys.SPLASH_INTER_AD, new AdConfig("ca-app-pub-3940256099942544/1033173712", FORemoteConfig.getInstance().getShowSplashInterAd(), AdType.AD_INTERSTITIAL));
        } else {
            if (FORemoteConfig.getInstance().getAdTypeSplash().equals(AdType.AD_NATIVE)) {
                adConfigs.put(AdKeys.SPLASH_AD, new AdConfig("ca-app-pub-3940256099942544/2247696110", FORemoteConfig.getInstance().isAdsEnabled(), AdType.AD_NATIVE));
            } else {
                adConfigs.put(AdKeys.SPLASH_AD, new AdConfig("ca-app-pub-3940256099942544/9214589741", FORemoteConfig.getInstance().isAdsEnabled(), AdType.AD_BANNER));
            }

            adConfigs.put(AdKeys.LANGUAGE_NATIVE_AD_1, new AdConfig("ca-app-pub-3940256099942544/2247696110", FORemoteConfig.getInstance().isAdsEnabled(), AdType.AD_NATIVE));
            adConfigs.put(AdKeys.LANGUAGE_NATIVE_AD_2, new AdConfig("ca-app-pub-3940256099942544/2247696110", FORemoteConfig.getInstance().isAdsEnabled(), AdType.AD_NATIVE));

            if (FORemoteConfig.getInstance().getAdTypeIntro().equals(AdType.AD_NATIVE)) {
                adConfigs.put(AdKeys.INTRO_AD_1, new AdConfig("ca-app-pub-3940256099942544/2247696110", FORemoteConfig.getInstance().isAdsEnabled(), AdType.AD_NATIVE));
                adConfigs.put(AdKeys.INTRO_AD_2, new AdConfig("ca-app-pub-3940256099942544/2247696110", FORemoteConfig.getInstance().isAdsEnabled(), AdType.AD_NATIVE));
                adConfigs.put(AdKeys.INTRO_AD_3, new AdConfig("ca-app-pub-3940256099942544/2247696110", FORemoteConfig.getInstance().isAdsEnabled(), AdType.AD_NATIVE));
            } else {
                adConfigs.put(AdKeys.INTRO_AD_1, new AdConfig("ca-app-pub-3940256099942544/9214589741", FORemoteConfig.getInstance().isAdsEnabled(), AdType.AD_BANNER));
                adConfigs.put(AdKeys.INTRO_AD_2, new AdConfig("ca-app-pub-3940256099942544/9214589741", FORemoteConfig.getInstance().isAdsEnabled(), AdType.AD_BANNER));
                adConfigs.put(AdKeys.INTRO_AD_3, new AdConfig("ca-app-pub-3940256099942544/9214589741", FORemoteConfig.getInstance().isAdsEnabled(), AdType.AD_BANNER));
            }
            adConfigs.put(AdKeys.SPLASH_INTER_AD, new AdConfig("ca-app-pub-3940256099942544/1033173712", FORemoteConfig.getInstance().isAdsEnabled(), AdType.AD_INTERSTITIAL));
        }

        return adConfigs;
    }


    @Override
    protected SplashFragment getSplashFragment() {
        SplashFragment splashFragment = SplashFragment.newInstance(
                R.layout.fragment_splash, // Layout ID
                FORemoteConfig.getInstance().getAdTypeSplash(),
                R.id.ad_container,         // Ad container ID
                R.id.shimmer_container_native,
                getNativeAdsLayout(FORemoteConfig.getInstance().getLayoutNativeAdSplash())
        );

        return splashFragment;
    }

    private int getNativeAdsLayout(long layoutType) {
        return layoutType == 1 ? R.layout.native_ad_layout : R.layout.native_ad_layout_2;
    }

    @Override
    protected Fragment getLanguageFragmentClass() {
        return LanguageSelectionFragment
                .newInstance(
                        R.layout.fragment_language_selection,
                        R.id.ad_container,
                        getNativeAdsLayout(FORemoteConfig.getInstance().getLayoutNativeAdLanguage1()), //R.layout.native_ad_layout,
                        getNativeAdsLayout(FORemoteConfig.getInstance().getLayoutNativeAdLanguage2()), //R.layout.native_ad_layout_2,
                        R.id.btn_done,
                        R.id.recycler_view_languages,
                        R.layout.item_language,
                        getLanguages()

                );
    }

    @Override
    protected Fragment getIntroFragmentClass() {
        return IntroductionSliderFragment
                .newInstance(
                        R.layout.fragment_introduction_slider,
                        R.id.ad_container,
                        FORemoteConfig.getInstance().getAdTypeIntro(),
                        getNativeAdsLayout(FORemoteConfig.getInstance().getLayoutNativeAdIntro()), //R.layout.native_ad_layout,
                        R.id.introductionSlider,
                        R.layout.slide_1,
                        R.layout.slide_1,
                        R.layout.slide_1
                );
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected NetworkDialogContent getNoInternetDialog() {
        return new NetworkDialogContent(
                "Connection Problem",
                "You're offline. Check your internet connection.",
                com.bwi.onboard.R.layout.custom_no_internet_dialog);
    }

    @Override
    public void completeOnboarding() {
        // Navigate to the desired activity
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }

    private List<Language> getLanguages() {
        // Sample languages
        List<Language> languages = new ArrayList<>();
        languages.add(new Language(R.drawable.flag_us_en, "English", "en"));
        languages.add(new Language(R.drawable.flag_france_fr, "French", "fr"));
        languages.add(new Language(R.drawable.flag_spain_sp, "Spanish", "es"));
        languages.add(new Language(R.drawable.flag_india_hi, "Hindi", "hi"));
        languages.add(new Language(R.drawable.flag_indonesia_indo, "Indonesian", "id"));
        languages.add(new Language(R.drawable.flag_portugal_po, "Portugal", "pt"));
        // Add more languages as needed
        return languages;
    }


    private String getDefaultDeviceTheme() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            return "dark";
        } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
            return "light";
        } else {
            return "unknown"; // Handle cases where the theme is undetermined
        }
    }
}
