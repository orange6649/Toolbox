package com.example.toolbox.Model;

import android.graphics.drawable.Drawable;

public class MyappListModel {
    private Drawable appIcon;
    private String appName;
    private String packageName;

    public MyappListModel(Drawable appIcon, String appName, String packageName) {
        this.appIcon = appIcon;
        this.appName = appName;
        this.packageName = packageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }
}
