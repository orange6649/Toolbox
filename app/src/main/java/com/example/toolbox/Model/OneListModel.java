package com.example.toolbox.Model;

import android.graphics.drawable.Drawable;



public class OneListModel {
    private String name; // 应用名称
    private String packageName; // 应用包名
    private Drawable icon; // 应用图标
    private int iconResourceId; // 图标资源 ID
    private String apkFilePath; // APK 文件路径
    private boolean selected; // 新增选中状态字段

    /**
     * 构造函数
     * @param name 应用名称
     * @param packageName 应用包名
     * @param icon 应用图标
     * @param apkFilePath APK 文件路径
     */
    public OneListModel(String name, String packageName, Drawable icon, String apkFilePath) {
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
        this.iconResourceId = -1; // 默认值，表示没有指定资源 ID
        this.apkFilePath = apkFilePath;
        this.selected = false; // 默认为未选中状态
    }

    /**
     * 获取应用名称
     * @return 应用名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置应用名称
     * @param name 应用名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取应用包名
     * @return 应用包名
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * 设置应用包名
     * @param packageName 应用包名
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * 获取应用图标
     * @return 应用图标
     */
    public Drawable getIcon() {
        return icon;
    }

    /**
     * 设置应用图标
     * @param icon 应用图标
     */
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    /**
     * 获取图标资源 ID
     * @return 图标资源 ID
     */
    public int getIconResourceId() {
        return iconResourceId;
    }

    /**
     * 设置图标资源 ID
     * @param iconResourceId 图标资源 ID
     */
    public void setIconResourceId(int iconResourceId) {
        this.iconResourceId = iconResourceId;
    }

    /**
     * 获取 APK 文件路径
     * @return APK 文件路径
     */
    public String getApkFilePath() {
        return apkFilePath;
    }

    /**
     * 设置 APK 文件路径
     * @param apkFilePath APK 文件路径
     */
    public void setApkFilePath(String apkFilePath) {
        this.apkFilePath = apkFilePath;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
