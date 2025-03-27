package com.bwi.onboard.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bwi.onboard.SupperSplashActivity;
import com.bwi.onboard.adapter.LanguageAdapter;
import com.bwi.onboard.ads.AdKeys;
import com.bwi.onboard.ads.NativeAdHelper;
import com.bwi.onboard.utils.Language;
import com.bwi.onboard.utils.PrfsKeys;

import java.util.List;
import java.util.Locale;

public class LanguageSelectionFragment extends Fragment {

    private FrameLayout adContainer;

    private RecyclerView recyclerView;
    private Language selectedLanguage;

    Button nextButton;
    private List<Language> languages;

    private static final String ARG_LAYOUT_ID = "layout_id";
    private static final String ARG_AD_CONTAINER_ID = "ad_container_id";
    private static final String ARG_AD_LAYOUT_ID_1 = "adLayoutId1";
    private static final String ARG_AD_LAYOUT_ID_2 = "adLayoutId2";
    private static final String ARG_NEXT_BUTTON_ID = "next_button_id";
    private static final String ARG_RECYCLER_VIEW_ID = "recycler_view_id";
    private static final String ARG_LAYOUT_ITEM_LANGUAGE_ID = "layout_item_language";

    int adLayoutId1,adLayoutId2;


    LanguageSelectionFragment(List<Language> languages){
        this.languages = languages;
    }
    public static LanguageSelectionFragment newInstance(int layoutId, int adContainerId, int adLayoutId1, int adLayoutId2, int nextButtonId,int recyclerViewId,int layoutItemLanguage,List<Language> languages) {
        LanguageSelectionFragment fragment = new LanguageSelectionFragment(languages);
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_ID, layoutId);
        args.putInt(ARG_AD_CONTAINER_ID, adContainerId);
        args.putInt(ARG_AD_LAYOUT_ID_1,adLayoutId1);
        args.putInt(ARG_AD_LAYOUT_ID_2,adLayoutId2);
        args.putInt(ARG_NEXT_BUTTON_ID, nextButtonId);
        args.putInt(ARG_RECYCLER_VIEW_ID, recyclerViewId);
        args.putInt(ARG_LAYOUT_ITEM_LANGUAGE_ID, layoutItemLanguage);
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
        int nextButtonId = args.getInt(ARG_NEXT_BUTTON_ID);
        int recyclerViewId = args.getInt(ARG_RECYCLER_VIEW_ID);
        int layoutItemLanguage = args.getInt(ARG_LAYOUT_ITEM_LANGUAGE_ID);
        adLayoutId1 = args.getInt(ARG_AD_LAYOUT_ID_1);
        adLayoutId2 = args.getInt(ARG_AD_LAYOUT_ID_2);


        View view = inflater.inflate(layoutId, container, false);
        adContainer = view.findViewById(adContainerId);
        nextButton = view.findViewById(nextButtonId);
        recyclerView = view.findViewById(recyclerViewId);

        nextButton.setOnClickListener(v -> {
            if (selectedLanguage != null) {
                // Save selected language and update locale
                saveSelectedLanguage(selectedLanguage);
                updateLocale(selectedLanguage.getCode());

                if (getActivity() instanceof SupperSplashActivity) {
                    ((SupperSplashActivity) getActivity()).moveToIntroductionSlider();
                }
            }
        });
        loadAndDisplayAd(AdKeys.LANGUAGE_NATIVE_AD_1,adLayoutId1);
        setupRecyclerView(layoutItemLanguage);

        return view;
    }

    private void setupRecyclerView(int layoutItemLanguage) {
        LanguageAdapter adapter = new LanguageAdapter(languages, layoutItemLanguage, language -> {
            if (selectedLanguage == null) {
                loadAndDisplayAd(AdKeys.LANGUAGE_NATIVE_AD_2,adLayoutId2);
            }
            selectedLanguage = language;
            nextButton.setEnabled(true); // Show the done button only after selection
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadAndDisplayAd(String key,int adLayoutId) {
        if (getActivity() instanceof SupperSplashActivity) {
            SupperSplashActivity supperSplashActivity = (SupperSplashActivity) getActivity();
            NativeAdHelper nativeAdHelper = supperSplashActivity.getNativeAdHelper(key);

            // Show the preloaded native ad
            nativeAdHelper.showNativeAd(supperSplashActivity, adContainer,adLayoutId);
        }
    }

    private void saveSelectedLanguage(Language language) {
        SharedPreferences preferences = requireContext().getSharedPreferences(PrfsKeys.PREF_NAME, getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PrfsKeys.KEY_LANG_CODE, language.getCode());
        editor.putString(PrfsKeys.KEY_LANG_NAME, language.getName());
        editor.apply();
    }

    private void updateLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        requireContext().getResources().updateConfiguration(config, requireContext().getResources().getDisplayMetrics());
    }
}
