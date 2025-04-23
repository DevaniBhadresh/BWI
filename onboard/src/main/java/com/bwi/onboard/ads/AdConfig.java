package com.bwi.onboard.ads;

public class AdConfig {
    private final String adUnitId;
    private final boolean canShowAd;

    private final  String adType;

    public AdConfig(String adUnitId,boolean canShowAd,String adType) {
        this.adUnitId = adUnitId;
        this.canShowAd = canShowAd;
        this.adType = adType;
    }

    public String getAdUnitId() {
        return adUnitId;
    }

    public boolean canShowAd(){
        return canShowAd;
    }

    public String getAdType() {
        return adType;
    }
}
