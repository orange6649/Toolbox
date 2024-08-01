package com.example.toolbox.Model;

public class PermissionsModel {
    private String permissionName;
    private boolean isEnabled;

    // 构造方法
    public PermissionsModel(String permissionName, boolean isEnabled, String 存储权限) {
        this.permissionName = permissionName;
        this.isEnabled = isEnabled;
    }

    // 获取权限名称的方法
    public String getName() {
        return permissionName;
    }

    // 设置权限名称的方法
    public void setName(String permissionName) {
        this.permissionName = permissionName;
    }

    // 获取是否启用的方法
    public boolean isEnabled() {
        return isEnabled;
    }

    // 设置是否启用的方法
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    // 获取权限名称的方法
    public String getPermissionName() {
        return permissionName;
    }
}
