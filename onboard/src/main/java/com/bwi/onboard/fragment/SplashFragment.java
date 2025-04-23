package com.bwi.onboard.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bwi.onboard.SupperSplashActivity;
import com.bwi.onboard.ads.AdKeys;
import com.bwi.onboard.ads.AdType;
import com.bwi.onboard.ads.BannerAdHelper;
import com.bwi.onboard.ads.NativeAdHelper;
import com.facebook.shimmer.ShimmerFrameLayout;

public class SplashFragment extends Fragment {

    private static final String ARG_LAYOUT_ID = "layout_id";
    private static final String ARG_AD_TYPE = "adType";
    private static final String ARG_AD_CONTAINER_ID = "ad_container_id";
    private static final String ARG_AD_SHIMMER_ID = "adShimmerId";
    private static final String ARG_AD_LAYOUT_ID = "adLayoutId";

    private FrameLayout adContainer;
    private boolean isAdLoaded = false;
    private boolean isTimeoutCompleted = false;
    private boolean isNavigated = false;
    String adType;

    public static SplashFragment newInstance(int layoutId, String adType, int adContainerId,int adShimmerId, int adLayoutId) {
        SplashFragment fragment = new SplashFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_ID, layoutId);
        args.putString(ARG_AD_TYPE,adType);
        args.putInt(ARG_AD_CONTAINER_ID, adContainerId);
        args.putInt(ARG_AD_LAYOUT_ID,adLayoutId);
        args.putInt(ARG_AD_SHIMMER_ID,adShimmerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalArgumentException("Arguments cannot be null. Pass layout and view IDs using newInstance.");
        }

        int layoutId = args.getInt(ARG_LAYOUT_ID);
        adType = args.getString(ARG_AD_TYPE);
        int adContainerId = args.getInt(ARG_AD_CONTAINER_ID);
        int adLayoutId = args.getInt(ARG_AD_LAYOUT_ID);
        int shimmerId = args.getInt(ARG_AD_SHIMMER_ID);


        View view = inflater.inflate(layoutId, container, false);
        adContainer = view.findViewById(adContainerId);
        ShimmerFrameLayout  adShimmerId = (ShimmerFrameLayout) view.findViewById(shimmerId);
        adShimmerId.startShimmer();
        loadAndShowAd(adLayoutId);

        // Start a 5-second timer for the splash screen
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            isTimeoutCompleted = true;
            checkAndProceed();
        }, 5000); // 5-second delay

        return view;
    }

    private void loadAndShowAd(int adLayoutId) {
        SupperSplashActivity supperSplashActivity = (SupperSplashActivity) getActivity();
        if (supperSplashActivity != null) {
            // Set callback for when the ad is loaded
            supperSplashActivity.setSplashAdCallback(() -> {
                isAdLoaded = true;
                showAdInContainer(adLayoutId);
                checkAndProceed();
            });

            // Show the ad if it is already loaded
            if (supperSplashActivity.isSplashAdLoaded()) {
                isAdLoaded = true;
                showAdInContainer(adLayoutId);
            }
        }
    }

    private void showAdInContainer(int adLayoutId) {
        SupperSplashActivity supperSplashActivity = (SupperSplashActivity) getActivity();
        if (supperSplashActivity != null && adContainer != null) {
            if (adType.equals(AdType.AD_NATIVE)){
                NativeAdHelper nativeAdHelper = supperSplashActivity.getNativeAdHelper(AdKeys.SPLASH_AD);
                nativeAdHelper.showNativeAd(supperSplashActivity, adContainer,adLayoutId);
            }

            else if (adType.equals(AdType.AD_BANNER)){
                BannerAdHelper bannerAdHelper = supperSplashActivity.getBannerAdHelper(AdKeys.SPLASH_AD);
                bannerAdHelper.showBannerAd(adContainer);
            }
        }
    }

    private void checkAndProceed() {
        if (isAdLoaded && isTimeoutCompleted && !isNavigated) {
            isNavigated = true; // Prevent double navigation
            proceedToNext();
        }
    }

    private void proceedToNext() {
        SupperSplashActivity supperSplashActivity = (SupperSplashActivity) getActivity();
        if (supperSplashActivity != null) {
            supperSplashActivity.moveToLanguageSelection();
        }
    }

    public interface SplashFragmentCallback {
        void setSplashAdCallback(Runnable onAdLoadedCallback);
        boolean isSplashAdLoaded();
        void showAdInContainer(FrameLayout adContainer);
        void navigateToNextScreen();
    }
}
