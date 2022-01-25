package com.lg_project.modelclass.creditplan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditPlanData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("credit")
    @Expose
    private double credit;
    @SerializedName("dollar_per_credit")
    @Expose
    private Double dollarPerCredit;
    @SerializedName("cost")
    @Expose
    private double cost;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("language")
    @Expose
    private String language;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public Double getDollarPerCredit() {
        return dollarPerCredit;
    }

    public void setDollarPerCredit(Double dollarPerCredit) {
        this.dollarPerCredit = dollarPerCredit;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}