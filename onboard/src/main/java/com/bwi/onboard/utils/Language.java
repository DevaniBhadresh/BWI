package com.bwi.onboard.utils;

public class Language {
    private int flagResId; // Resource ID for the flag
    private String name;   // Name of the language
    private String code;    // Locale code for the language

    public Language(int flagResId, String name, String code) {
        this.flagResId = flagResId;
        this.name = name;
        this.code = code;
    }

    public int getFlagResId() {
        return flagResId;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
