package com.bwi.onboard.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Set;


public class FORemoteConfig {

    private final String TAG = FORemoteConfig.class.getSimpleName();
    private FirebaseRemoteConfig mRemoteConfig;
    private static volatile FORemoteConfig _instance;
    private SingletonListener mSingletonListener;

    public class Const {
        // Params
        public static final String AD_ENABLED = "AD_ENABLED";
        public static final String AD_TYPE_SPLASH = "AD_TYPE_SPLASH";
        public static final String AD_TYPE_INTRO = "AD_TYPE_INTRO";
        public static final String LAYOUT_NATIVE_AD_INTRO = "LAYOUT_NATIVE_AD_INTRO";
        public static final String LAYOUT_NATIVE_AD_LANGUAGE_1 = "LAYOUT_NATIVE_AD_LANGUAGE_1";
        public static final String LAYOUT_NATIVE_AD_LANGUAGE_2 = "LAYOUT_NATIVE_AD_LANGUAGE_2";
        public static final String LAYOUT_NATIVE_AD_SPLASH = "LAYOUT_NATIVE_AD_SPLASH";
        public static final String SHOW_SPLASH_AD = "SHOW_SPLASH_AD";
        public static final String SHOW_SPLASH_INTER_AD = "SHOW_SPLASH_INTER_AD";
        public static final String SHOW_LANGUAGE_NATIVE_AD_1 = "SHOW_LANGUAGE_NATIVE_AD_1";
        public static final String SHOW_LANGUAGE_NATIVE_AD_2 = "SHOW_LANGUAGE_NATIVE_AD_2";
        public static final String SHOW_INTRO_AD_1 = "SHOW_INTRO_AD_1";
        public static final String SHOW_INTRO_AD_2 = "SHOW_INTRO_AD_2";
        public static final String SHOW_INTRO_AD_3 = "SHOW_INTRO_AD_3";



    }


    public FORemoteConfig() {
        setupRemoteConfig();
        fetchRemoteConfig();
    }

    public synchronized static FORemoteConfig getInstance() {
        if (_instance == null) {
            synchronized (FORemoteConfig.class) {
                if (_instance == null) _instance = new FORemoteConfig();
            }
        }
        return _instance;
    }

    private void fetchRemoteConfig() {
        mRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    boolean updated = task.getResult();
                    Log.d(TAG, "Config params updated: " + updated + mRemoteConfig.getBoolean(Const.SHOW_SPLASH_AD) + mRemoteConfig.getBoolean(Const.SHOW_LANGUAGE_NATIVE_AD_1) + mRemoteConfig.getBoolean(Const.SHOW_LANGUAGE_NATIVE_AD_2) + mRemoteConfig.getBoolean(Const.SHOW_INTRO_AD_1) + mRemoteConfig.getBoolean(Const.SHOW_INTRO_AD_2) + mRemoteConfig.getBoolean(Const.SHOW_INTRO_AD_3) + mRemoteConfig.getBoolean(Const.AD_ENABLED));

                    setShowSplashAd(mRemoteConfig.getBoolean(Const.SHOW_SPLASH_AD));
                    setShowSplashInterAd(mRemoteConfig.getBoolean(Const.SHOW_SPLASH_INTER_AD));
                    setLayoutNativeAdSplash(mRemoteConfig.getLong(Const.LAYOUT_NATIVE_AD_SPLASH));

                    setShowLanguageNativeAd1(mRemoteConfig.getBoolean(Const.SHOW_LANGUAGE_NATIVE_AD_1));
                    setLayoutNativeAdLanguage1(mRemoteConfig.getLong(Const.LAYOUT_NATIVE_AD_LANGUAGE_1));

                    setShowLanguageNativeAd2(mRemoteConfig.getBoolean(Const.SHOW_LANGUAGE_NATIVE_AD_2));
                    setLayoutNativeAdLanguage2(mRemoteConfig.getLong(Const.LAYOUT_NATIVE_AD_LANGUAGE_2));

                    setShowIntroAd1(mRemoteConfig.getBoolean(Const.SHOW_INTRO_AD_1));
                    setShowIntroAd2(mRemoteConfig.getBoolean(Const.SHOW_INTRO_AD_2));
                    setShowIntroAd3(mRemoteConfig.getBoolean(Const.SHOW_INTRO_AD_3));
                    setAdTypeIntro(mRemoteConfig.getString(Const.AD_TYPE_INTRO));
                    setAdTypeSplash(mRemoteConfig.getString(Const.AD_TYPE_SPLASH));

                    setLayoutNativeAdIntro(mRemoteConfig.getLong(Const.LAYOUT_NATIVE_AD_INTRO));

                    setAdsEnabled(mRemoteConfig.getBoolean(Const.AD_ENABLED));
                }
                if (task.isSuccessful()) {
                    Set<String> keys = mRemoteConfig.getKeysByPrefix(""); // Empty string = all keys
                    for (String key : keys) {
                        String value = mRemoteConfig.getString(key);
                        Log.d("RemoteConfig", "Key: " + key + ", Value: " + value);
                    }
                    mSingletonListener.onRemoteFetch(true);
                } else {
                    Log.e("RemoteConfig", "Fetch failed");
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mSingletonListener.onRemoteFetch(false);


            }
        });
    }

    private void setupRemoteConfig() {
        mRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        mRemoteConfig.setConfigSettingsAsync(configSettings);
        //mRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        setAdsEnabled(mRemoteConfig.getBoolean(Const.AD_ENABLED));
        setShowSplashAd(mRemoteConfig.getBoolean(Const.SHOW_SPLASH_AD));
        setShowSplashInterAd(mRemoteConfig.getBoolean(Const.SHOW_SPLASH_INTER_AD));
        setShowLanguageNativeAd1(mRemoteConfig.getBoolean(Const.SHOW_LANGUAGE_NATIVE_AD_1));
        setShowLanguageNativeAd2(mRemoteConfig.getBoolean(Const.SHOW_LANGUAGE_NATIVE_AD_2));
        setShowIntroAd1(mRemoteConfig.getBoolean(Const.SHOW_INTRO_AD_1));
        setShowIntroAd2(mRemoteConfig.getBoolean(Const.SHOW_INTRO_AD_2));
        setShowIntroAd3(mRemoteConfig.getBoolean(Const.SHOW_INTRO_AD_3));
        setLayoutNativeAdSplash(mRemoteConfig.getLong(Const.LAYOUT_NATIVE_AD_SPLASH));
        setLayoutNativeAdLanguage1(mRemoteConfig.getLong(Const.LAYOUT_NATIVE_AD_LANGUAGE_1));
        setLayoutNativeAdLanguage2(mRemoteConfig.getLong(Const.LAYOUT_NATIVE_AD_LANGUAGE_2));
        setLayoutNativeAdIntro(mRemoteConfig.getLong(Const.LAYOUT_NATIVE_AD_INTRO));
        setAdTypeIntro(mRemoteConfig.getString(Const.AD_TYPE_INTRO));
        setAdTypeSplash(mRemoteConfig.getString(Const.AD_TYPE_SPLASH));

        Log.d(TAG, "Is Ads Enabled : " + isAdsEnabled());
        Log.d(TAG, "Splash Default Ad : " + getShowSplashAd());
        Log.d(TAG, "Layout Splash Default Ad : " + getLayoutNativeAdSplash());
        Log.d(TAG, "Lang 1 Default Ad : " + getShowLanguageNativeAd1());
        Log.d(TAG, "Layout Lang 1 Default Ad : " + getLayoutNativeAdLanguage1());
        Log.d(TAG, "Lang 2 Default Ad : " + getShowLanguageNativeAd2());
        Log.d(TAG, "Layout Lang 2 Default Ad : " + getLayoutNativeAdLanguage2());
        Log.d(TAG, "Intro 1 Default Ad : " + getShowIntroAd1());
        Log.d(TAG, "Intro 2 Default Ad : " + getShowIntroAd2());
        Log.d(TAG, "Intro 3 Default Ad : " + getShowIntroAd3());
        Log.d(TAG, "Layout Intro Default Ad : " + getLayoutNativeAdIntro());

    }


    // Getter / Setter
    // Params v1
    private boolean adsEnabled;
    private String adTypeSplash;
    private String adTypeIntro;
    private long layoutNativeAdIntro;
    private long layoutNativeAdLanguage1;
    private long layoutNativeAdLanguage2;

    private long layoutNativeAdSplash;

    private boolean showSplashAd;
    private boolean showSplashInterAd;


    private boolean showLanguageNativeAd1;
    private boolean showLanguageNativeAd2;
    private boolean showIntroAd1;
    private boolean showIntroAd2;
    private boolean showIntroAd3;



    public boolean isAdsEnabled() {
        return adsEnabled;
    }

    public void setAdsEnabled(boolean adsEnabled) {
        this.adsEnabled = adsEnabled;
    }

    public boolean getShowSplashAd() {
        return showSplashAd;
    }

    public void setShowSplashAd(boolean showSplashAd) {
        this.showSplashAd = showSplashAd;
    }

    public String getAdTypeSplash() {
        return adTypeSplash;
    }

    public void setAdTypeSplash(String adTypeSplash) {
        this.adTypeSplash = adTypeSplash;
    }

    public boolean getShowSplashInterAd() {
        return showSplashInterAd;
    }

    public void setShowSplashInterAd(boolean showSplashInterAd) {
        this.showSplashInterAd = showSplashInterAd;
    }

    public boolean getShowLanguageNativeAd1() {
        return showLanguageNativeAd1;
    }

    public void setShowLanguageNativeAd1(boolean showLanguageNativeAd1) {
        this.showLanguageNativeAd1 = showLanguageNativeAd1;
    }

    public boolean getShowLanguageNativeAd2() {
        return showLanguageNativeAd2;
    }

    public void setShowLanguageNativeAd2(boolean showLanguageNativeAd2) {
        this.showLanguageNativeAd2 = showLanguageNativeAd2;
    }

    public boolean getShowIntroAd1() {
        return showIntroAd1;
    }

    public void setShowIntroAd1(boolean showIntroAd1) {
        this.showIntroAd1 = showIntroAd1;
    }

    public boolean getShowIntroAd2() {
        return showIntroAd2;
    }

    public void setShowIntroAd2(boolean showIntroAd2) {
        this.showIntroAd2 = showIntroAd2;
    }

    public boolean getShowIntroAd3() {
        return showIntroAd3;
    }

    public void setShowIntroAd3(boolean showIntroAd3) {
        this.showIntroAd3 = showIntroAd3;
    }

    public long getLayoutNativeAdSplash() {
        return layoutNativeAdSplash;
    }

    public void setLayoutNativeAdSplash(long layoutNativeAdSplash) {
        this.layoutNativeAdSplash = layoutNativeAdSplash;
    }

    public long getLayoutNativeAdLanguage1() {
        return layoutNativeAdLanguage1;
    }

    public void setLayoutNativeAdLanguage1(long layoutNativeAdLanguage1) {
        this.layoutNativeAdLanguage1 = layoutNativeAdLanguage1;
    }

    public long getLayoutNativeAdLanguage2() {
        return layoutNativeAdLanguage2;
    }

    public void setLayoutNativeAdLanguage2(long layoutNativeAdLanguage2) {
        this.layoutNativeAdLanguage2 = layoutNativeAdLanguage2;
    }

    public long getLayoutNativeAdIntro() {
        return layoutNativeAdIntro;
    }

    public void setLayoutNativeAdIntro(long layoutNativeAdIntro) {
        this.layoutNativeAdIntro = layoutNativeAdIntro;
    }

    public String getAdTypeIntro() {
        return adTypeIntro;
    }

    public void setAdTypeIntro(String adTypeIntro) {
        this.adTypeIntro = adTypeIntro;
    }

    // Interface
    public interface SingletonListener {
        void onRemoteFetch(boolean isFetched);
    }

    public void setInterfaz(SingletonListener listener) {
        this.mSingletonListener = listener;
    }

}