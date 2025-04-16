package com.bwi.onboard.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrfsKeys {

    public static final String PREF_NAME = "FOPrfs";
    public static final String KEY_FIRST_LAUNCH = "IsFirstLaunch";
    public static final String KEY_LANG_CODE = "SelectedLanguageCode";
    public static final String KEY_LANG_NAME = "SelectedLanguageName";
    public static final String KEY_RC_FETCHED = "RcFetched";

    public static void setRemoteConfigFetched(Context context,boolean flag) {
        SharedPreferences preferences = context.getSharedPreferences(PrfsKeys.PREF_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PrfsKeys.KEY_RC_FETCHED, flag);
        editor.apply();
    }

    public static boolean isRemoteConfigFetched(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PrfsKeys.PREF_NAME, context.MODE_PRIVATE);
        return preferences.getBoolean(PrfsKeys.KEY_RC_FETCHED, false);
    }
}
