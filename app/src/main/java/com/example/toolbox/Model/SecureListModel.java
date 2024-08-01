package com.example.toolbox.Model;

public class SecureListModel {
    private String title;
    private int iconResId;

    public SecureListModel(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    public String getTitle() {
        return title;
    }

    public int getIconResId() {
        return iconResId;
    }
}
