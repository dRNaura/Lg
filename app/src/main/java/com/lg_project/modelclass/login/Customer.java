package com.lg_project.modelclass.login;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Customer {

    @SerializedName("credit")
    @Expose
    private double credit;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("user")
    @Expose
    private int user;

    @SerializedName("age")
    @Expose
    private int age;

    @SerializedName("country")
    @Expose
    private int country;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("active_pin")
    @Expose
    private String active_pin;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("username")
    @Expose
    private String username;

   @SerializedName("language")
    @Expose
    private String language;

    @SerializedName("prefered_settings")
    @Expose
    private List<PreferedSettingModel> prefered_settings = null;

    public List<PreferedSettingModel> getPrefered_settings() {
        return prefered_settings;
    }

    public void setPrefered_settings(List<PreferedSettingModel> prefered_settings) {
        this.prefered_settings = prefered_settings;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getActive_pin() {
        return active_pin;
    }

    public void setActive_pin(String active_pin) {
        this.active_pin = active_pin;
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


    //    @SerializedName("value")
//    @Expose
//    private List<Value> value = null;
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public List<Value> getValue() {
//        return value;
//    }
//
//    public void setValue(List<Value> value) {
//        this.value = value;
//    }

}