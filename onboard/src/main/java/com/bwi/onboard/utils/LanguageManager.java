package com.bwi.onboard.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

public class LanguageManager {


    public static void saveSelectedLanguage(Context context, String languageCode) {
        SharedPreferences preferences = context.getSharedPreferences(PrfsKeys.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PrfsKeys.KEY_LANG_CODE, languageCode);
        editor.apply();
    }

    public static String getSelectedLanguageCode(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PrfsKeys.PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(PrfsKeys.KEY_LANG_CODE, Locale.getDefault().getLanguage());
    }

    public static void applyLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static void applySavedLocale(Context context) {
        String languageCode = getSelectedLanguageCode(context);
        applyLocale(context, languageCode);
    }
}
