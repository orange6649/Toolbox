package com.example.toolbox.Model;

import android.graphics.drawable.Drawable;

public class ThreeListModel {
    private Drawable appIcon;
    private String appName;
    private String appSize;

    public ThreeListModel(Drawable appIcon, String appName, String appSize) {
        this.appIcon = appIcon;
        this.appName = appName;
        this.appSize = appSize;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppSize() {
        return appSize;
    }
}
