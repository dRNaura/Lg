package com.lg_project.modelclass.login;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Value {

    @SerializedName("credit")
    @Expose
    private Double credit;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user")
    @Expose
    private Integer user;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("country")
    @Expose
    private Integer country;
    @SerializedName("active_pin")
    @Expose
    private String activePin;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("prefered_notification")
    @Expose
    private List<Object> preferedNotification = null;
    @SerializedName("prefered_settings")
    @Expose
    private List<Object> preferedSettings = null;
    @SerializedName("favourite_artist")
    @Expose
    private List<Object> favouriteArtist = null;

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public String getActivePin() {
        return activePin;
    }

    public void setActivePin(String activePin) {
        this.activePin = activePin;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<Object> getPreferedNotification() {
        return preferedNotification;
    }

    public void setPreferedNotification(List<Object> preferedNotification) {
        this.preferedNotification = preferedNotification;
    }

    public List<Object> getPreferedSettings() {
        return preferedSettings;
    }

    public void setPreferedSettings(List<Object> preferedSettings) {
        this.preferedSettings = preferedSettings;
    }

    public List<Object> getFavouriteArtist() {
        return favouriteArtist;
    }

    public void setFavouriteArtist(List<Object> favouriteArtist) {
        this.favouriteArtist = favouriteArtist;
    }

}