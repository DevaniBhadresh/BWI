package com.bwi.onboard.utils;

public class NetworkDialogContent {

    private String title;
    private String message;
    private int layout;

    public NetworkDialogContent(String title, String message, int layout) {
        this.title = title;
        this.message = message;
        this.layout = layout;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public int getLayout() {
        return layout;
    }
}
