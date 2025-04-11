package com.bwi.onboard.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.List;
import java.util.Set;


public class FORemoteConfig {

    private final String TAG = FORemoteConfig.class.getSimpleName();
    private FirebaseRemoteConfig mRemoteConfig;
    private static volatile FORemoteConfig _instance;
    private SingletonListener mSingletonListener;

    public class Const {
        // Params
        public static final String ADS_ENABLED = "ADS_ENABLED";
        public static final String SPLASH_NATIVE_AD = "SPLASH_NATIVE_AD";
        public static final String LANGUAGE_NATIVE_AD_1 = "LANGUAGE_NATIVE_AD_1";
        public static final String LANGUAGE_NATIVE_AD_2 = "LANGUAGE_NATIVE_AD_2";
        public static final String INTRO_NATIVE_AD_1 = "INTRO_NATIVE_AD_1";
        public static final String INTRO_NATIVE_AD_2 = "INTRO_NATIVE_AD_2";
        public static final String INTRO_NATIVE_AD_3 = "INTRO_NATIVE_AD_3";
        public static final String LAYOUT_INTRO_NATIVE_AD = "LAYOUT_INTRO_NATIVE_AD";
        public static final String LAYOUT_LANGUAGE_NATIVE_AD_1 = "LAYOUT_LANGUAGE_NATIVE_AD_1";
        public static final String LAYOUT_LANGUAGE_NATIVE_AD_2 = "LAYOUT_LANGUAGE_NATIVE_AD_2";
        public static final String LAYOUT_SPLASH_NATIVE_AD = "LAYOUT_SPLASH_NATIVE_AD";
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
                    Log.d(TAG, "Config params updated: " + updated + mRemoteConfig.getBoolean(Const.SPLASH_NATIVE_AD) + mRemoteConfig.getBoolean(Const.LANGUAGE_NATIVE_AD_1) + mRemoteConfig.getBoolean(Const.LANGUAGE_NATIVE_AD_2) + mRemoteConfig.getBoolean(Const.INTRO_NATIVE_AD_1) + mRemoteConfig.getBoolean(Const.INTRO_NATIVE_AD_2) + mRemoteConfig.getBoolean(Const.INTRO_NATIVE_AD_3) + mRemoteConfig.getBoolean(Const.ADS_ENABLED));

                    setSplashNativeAd(mRemoteConfig.getBoolean(Const.SPLASH_NATIVE_AD));
                    setLayoutSplashNativeAd(mRemoteConfig.getLong(Const.LAYOUT_SPLASH_NATIVE_AD));

                    setLanguageNativeAd1(mRemoteConfig.getBoolean(Const.LANGUAGE_NATIVE_AD_1));
                    setLayoutLanguageNativeAd1(mRemoteConfig.getLong(Const.LAYOUT_LANGUAGE_NATIVE_AD_1));

                    setLanguageNativeAd2(mRemoteConfig.getBoolean(Const.LANGUAGE_NATIVE_AD_2));
                    setLayoutLanguageNativeAd2(mRemoteConfig.getLong(Const.LAYOUT_LANGUAGE_NATIVE_AD_2));

                    setIntroNativeAd1(mRemoteConfig.getBoolean(Const.INTRO_NATIVE_AD_1));
                    setIntroNativeAd2(mRemoteConfig.getBoolean(Const.INTRO_NATIVE_AD_2));
                    setIntroNativeAd3(mRemoteConfig.getBoolean(Const.INTRO_NATIVE_AD_3));
                    setLayoutIntroNativeAd(mRemoteConfig.getLong(Const.LAYOUT_INTRO_NATIVE_AD));

                    setAdsEnabled(mRemoteConfig.getBoolean(Const.ADS_ENABLED));
                }
                if (task.isSuccessful()) {
                    Set<String> keys = mRemoteConfig.getKeysByPrefix(""); // Empty string = all keys
                    for (String key : keys) {
                        String value = mRemoteConfig.getString(key);
                        Log.d("RemoteConfig", "Key: " + key + ", Value: " + value);
                    }
                } else {
                    Log.e("RemoteConfig", "Fetch failed");
                }

                mSingletonListener.onRemoteFetch(true);
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

        setAdsEnabled(mRemoteConfig.getBoolean(Const.ADS_ENABLED));
        setSplashNativeAd(mRemoteConfig.getBoolean(Const.SPLASH_NATIVE_AD));
        setLanguageNativeAd1(mRemoteConfig.getBoolean(Const.LANGUAGE_NATIVE_AD_1));
        setLanguageNativeAd2(mRemoteConfig.getBoolean(Const.LANGUAGE_NATIVE_AD_2));
        setIntroNativeAd1(mRemoteConfig.getBoolean(Const.INTRO_NATIVE_AD_1));
        setIntroNativeAd2(mRemoteConfig.getBoolean(Const.INTRO_NATIVE_AD_2));
        setIntroNativeAd3(mRemoteConfig.getBoolean(Const.INTRO_NATIVE_AD_3));
        setLayoutSplashNativeAd(mRemoteConfig.getLong(Const.LAYOUT_SPLASH_NATIVE_AD));
        setLayoutLanguageNativeAd1(mRemoteConfig.getLong(Const.LAYOUT_LANGUAGE_NATIVE_AD_1));
        setLayoutLanguageNativeAd2(mRemoteConfig.getLong(Const.LAYOUT_LANGUAGE_NATIVE_AD_2));
        setLayoutIntroNativeAd(mRemoteConfig.getLong(Const.LAYOUT_INTRO_NATIVE_AD));

        Log.d(TAG, "Is Ads Enabled : " + isAdsEnabled());
        Log.d(TAG, "Splash Default Ad : " + getSplashNativeAd());
        Log.d(TAG, "Layout Splash Default Ad : " + getLayoutSplashNativeAd());
        Log.d(TAG, "Lang 1 Default Ad : " + getLanguageNativeAd1());
        Log.d(TAG, "Layout Lang 1 Default Ad : " + getLayoutLanguageNativeAd1());
        Log.d(TAG, "Lang 2 Default Ad : " + getLanguageNativeAd2());
        Log.d(TAG, "Layout Lang 2 Default Ad : " + getLayoutLanguageNativeAd2());
        Log.d(TAG, "Intro 1 Default Ad : " + getIntroNativeAd1());
        Log.d(TAG, "Intro 2 Default Ad : " + getIntroNativeAd2());
        Log.d(TAG, "Intro 3 Default Ad : " + getIntroNativeAd3());
        Log.d(TAG, "Layout Intro Default Ad : " + getLayoutIntroNativeAd());

    }


    // Getter / Setter
    // Params v1
    private boolean adsEnabled;
    private boolean splashNativeAd;
    private boolean languageNativeAd1;
    private boolean languageNativeAd2;
    private boolean introNativeAd1;
    private boolean introNativeAd2;
    private boolean introNativeAd3;

    private long layoutSplashNativeAd;
    private long layoutLanguageNativeAd1;
    private long layoutLanguageNativeAd2;
    private long layoutIntroNativeAd;

    public boolean isAdsEnabled() {
        return adsEnabled;
    }

    public void setAdsEnabled(boolean adsEnabled) {
        this.adsEnabled = adsEnabled;
    }

    public boolean getSplashNativeAd() {
        return splashNativeAd;
    }

    public void setSplashNativeAd(boolean splashNativeAd) {
        this.splashNativeAd = splashNativeAd;
    }

    public boolean getLanguageNativeAd1() {
        return languageNativeAd1;
    }

    public void setLanguageNativeAd1(boolean languageNativeAd1) {
        this.languageNativeAd1 = languageNativeAd1;
    }

    public boolean getLanguageNativeAd2() {
        return languageNativeAd2;
    }

    public void setLanguageNativeAd2(boolean languageNativeAd2) {
        this.languageNativeAd2 = languageNativeAd2;
    }

    public boolean getIntroNativeAd1() {
        return introNativeAd1;
    }

    public void setIntroNativeAd1(boolean introNativeAd1) {
        this.introNativeAd1 = introNativeAd1;
    }

    public boolean getIntroNativeAd2() {
        return introNativeAd2;
    }

    public void setIntroNativeAd2(boolean introNativeAd2) {
        this.introNativeAd2 = introNativeAd2;
    }

    public boolean getIntroNativeAd3() {
        return introNativeAd3;
    }

    public void setIntroNativeAd3(boolean introNativeAd3) {
        this.introNativeAd3 = introNativeAd3;
    }

    public long getLayoutSplashNativeAd() {
        return layoutSplashNativeAd;
    }

    public void setLayoutSplashNativeAd(long layoutSplashNativeAd) {
        this.layoutSplashNativeAd = layoutSplashNativeAd;
    }

    public long getLayoutLanguageNativeAd1() {
        return layoutLanguageNativeAd1;
    }

    public void setLayoutLanguageNativeAd1(long layoutLanguageNativeAd1) {
        this.layoutLanguageNativeAd1 = layoutLanguageNativeAd1;
    }

    public long getLayoutLanguageNativeAd2() {
        return layoutLanguageNativeAd2;
    }

    public void setLayoutLanguageNativeAd2(long layoutLanguageNativeAd2) {
        this.layoutLanguageNativeAd2 = layoutLanguageNativeAd2;
    }

    public long getLayoutIntroNativeAd() {
        return layoutIntroNativeAd;
    }

    public void setLayoutIntroNativeAd(long layoutIntroNativeAd) {
        this.layoutIntroNativeAd = layoutIntroNativeAd;
    }

    // Interface
    public interface SingletonListener {
        void onRemoteFetch(boolean isFetched);
    }

    public void setInterfaz(SingletonListener listener) {
        this.mSingletonListener = listener;
    }

}