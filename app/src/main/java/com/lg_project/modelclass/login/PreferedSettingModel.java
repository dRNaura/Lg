package com.lg_project.modelclass.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PreferedSettingModel {


    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("user")
    @Expose
    private Integer user;

    @SerializedName("stealth_code")
    @Expose
    private Integer stealth_code;

    @SerializedName("stealth_mode")
    @Expose
    private Integer stealth_mode;

    @SerializedName("app_icon")
    @Expose
    private String app_icon;

    @SerializedName("date")
    @Expose
    private String date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Integer getStealth_code() {
        return stealth_code;
    }

    public void setStealth_code(Integer stealth_code) {
        this.stealth_code = stealth_code;
    }

    public Integer getStealth_mode() {
        return stealth_mode;
    }

    public void setStealth_mode(Integer stealth_mode) {
        this.stealth_mode = stealth_mode;
    }

    public String getApp_icon() {
        return app_icon;
    }

    public void setApp_icon(String app_icon) {
        this.app_icon = app_icon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
