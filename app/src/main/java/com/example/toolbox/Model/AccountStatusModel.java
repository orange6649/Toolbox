// AccountStatusModel.java
package com.example.toolbox.Model;

public class AccountStatusModel {
    private long id;
    private String dateTime;
    private String phoneModel;
    private String loginStatus;
    private String loginArea;

    public AccountStatusModel() {
    }

    public AccountStatusModel(String dateTime, String phoneModel, String loginStatus, String loginArea) {
        this.dateTime = dateTime;
        this.phoneModel = phoneModel;
        this.loginStatus = loginStatus;
        this.loginArea = loginArea;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getLoginArea() {
        return loginArea;
    }

    public void setLoginArea(String loginArea) {
        this.loginArea = loginArea;
    }
}
