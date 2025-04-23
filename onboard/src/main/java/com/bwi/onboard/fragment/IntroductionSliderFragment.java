package com.bwi.onboard.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bwi.onboard.SupperSplashActivity;
import com.bwi.onboard.ads.AdKeys;
import com.bwi.onboard.ads.AdType;
import com.bwi.onboard.ads.BannerAdHelper;
import com.bwi.onboard.ads.NativeAdHelper;
import com.bwi.onboard.slider.IntroductionSlider;
import com.bwi.onboard.slider.OnSliderChangeListener;

import java.util.Arrays;

public class IntroductionSliderFragment extends Fragment implements OnSliderChangeListener {
    private static final String ARG_LAYOUT_ID = "layout_id";
    private static final String ARG_AD_CONTAINER_ID = "ad_container_id";
    private static final String ARG_AD_TYPE = "adType";
    private static final String ARG_AD_LAYOUT_ID = "adLayoutId";
    private static final String ARG_INTRODUCTION_SLIDER = "introduction_slider";
    private static final String ARG_LAYOUT_SLIDE_1="slide_1";
    private static final String ARG_LAYOUT_SLIDE_2="slide_2";
    private static final String ARG_LAYOUT_SLIDE_3="slide_3";

    private FrameLayout adContainer;
    private String defaultKey = AdKeys.INTRO_AD_1;
    IntroductionSlider introductionSlider;
    int adLayoutId;

    String adType;
    public static IntroductionSliderFragment newInstance(int layoutId, int adContainerId, String adType,int adLayoutId,int introduction_slider, int layout_slide_1,int layout_slide_2,int layout_slide_3) {
        IntroductionSliderFragment fragment = new IntroductionSliderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_ID, layoutId);
        args.putInt(ARG_AD_CONTAINER_ID, adContainerId);
        args.putString(ARG_AD_TYPE,adType);
        args.putInt(ARG_AD_LAYOUT_ID, adLayoutId);
        args.putInt(ARG_INTRODUCTION_SLIDER,introduction_slider);
        args.putInt(ARG_LAYOUT_SLIDE_1,layout_slide_1);
        args.putInt(ARG_LAYOUT_SLIDE_2,layout_slide_2);
        args.putInt(ARG_LAYOUT_SLIDE_3,layout_slide_3);
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
        int adContainerId = args.getInt(ARG_AD_CONTAINER_ID);
        adType = args.getString(ARG_AD_TYPE);
        adLayoutId = args.getInt(ARG_AD_LAYOUT_ID);
        int introduction_slider = args.getInt(ARG_INTRODUCTION_SLIDER);
        int layout_slide_1 = args.getInt(ARG_LAYOUT_SLIDE_1);
        int layout_slide_2 = args.getInt(ARG_LAYOUT_SLIDE_2);
        int layout_slide_3 = args.getInt(ARG_LAYOUT_SLIDE_3);


        View view = inflater.inflate(layoutId, container, false);
        adContainer = view.findViewById(adContainerId);
        introductionSlider = view.findViewById(introduction_slider);
        introductionSlider.setOnSliderChangeListener(this);

        // Pass the slide layouts
        introductionSlider.setSlides(Arrays.asList(
                layout_slide_1,
                layout_slide_2,
                layout_slide_3
        ));
        Log.d(IntroductionSliderFragment.class.getSimpleName(), "onCreateView: ");
        adContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                adContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                loadAndDisplayAd(defaultKey); // Call this only after the layout is ready
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadAndDisplayAd(String key) {
        if (getActivity() instanceof SupperSplashActivity) {
            SupperSplashActivity supperSplashActivity = (SupperSplashActivity) getActivity();
            if (adType.equals(AdType.AD_NATIVE)){

                NativeAdHelper nativeAdHelper = supperSplashActivity.getNativeAdHelper(key);
                nativeAdHelper.showNativeAd(supperSplashActivity, adContainer,adLayoutId);
            } else if (adType.equals(AdType.AD_BANNER)){

                BannerAdHelper bannerAdHelper = supperSplashActivity.getBannerAdHelper(key);
                bannerAdHelper.showBannerAd(adContainer);
            }

        }
    }


    @Override
    public void onSliderItemChanged(int position) {
        switch (position) {
            case 1:
                loadAndDisplayAd(AdKeys.INTRO_AD_2);
                break;
            case 2:
                loadAndDisplayAd(AdKeys.INTRO_AD_3);
                break;
            default:
                loadAndDisplayAd(defaultKey);
        }
    }
}