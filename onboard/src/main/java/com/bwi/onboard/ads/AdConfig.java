package com.bwi.onboard.ads;

public class AdConfig {
    private final String adUnitId;
    private final boolean canShowAd;

    public AdConfig(String adUnitId,boolean canShowAd) {
        this.adUnitId = adUnitId;
        this.canShowAd = canShowAd;
    }

    public String getAdUnitId() {
        return adUnitId;
    }

    public boolean canShowAd(){
        return canShowAd;
    }
}
