package com.example.toolbox.Model;

import android.graphics.drawable.Drawable;

public class TwoListModel {
    private String name;
    private String packageName;
    private Drawable icon;
    private int iconResourceId; // 用于保存图标资源 ID 的属性
    private boolean installButtonClicked; // 用于表示安装按钮的点击状态

    public TwoListModel(String name, String packageName, Drawable icon) {
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
        // 设置图标资源 ID
        this.iconResourceId = -1; // 默认值，表示没有指定资源 ID
        this.installButtonClicked = false; // 默认情况下，安装按钮未点击
    }

    // 获取应用程序名称
    public String getName() {
        return name;
    }

    // 设置应用程序名称
    public void setName(String name) {
        this.name = name;
    }

    // 获取应用程序包名
    public String getPackageName() {
        return packageName;
    }

    // 设置应用程序包名
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    // 获取图标 Drawable 对象
    public Drawable getIconDrawable() {
        return icon;
    }

    // 设置图标 Drawable 对象
    public void setIconDrawable(Drawable icon) {
        this.icon = icon;
    }

    // 获取图标资源 ID
    public int getIconResourceId() {
        return iconResourceId;
    }

    // 设置图标资源 ID
    public void setIconResourceId(int iconResourceId) {
        this.iconResourceId = iconResourceId;
    }

    // 获取图标 Drawable 对象
    public Drawable getIcon() {
        return icon;
    }

    // 设置图标 Drawable 对象
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    // 获取安装按钮点击状态
    public boolean isInstallButtonClicked() {
        return installButtonClicked;
    }

    // 设置安装按钮点击状态
    public void setInstallButtonClicked(boolean installButtonClicked) {
        this.installButtonClicked = installButtonClicked;
    }

    // 获取应用程序名称
    public String getAppName() {
        return name;
    }
}
