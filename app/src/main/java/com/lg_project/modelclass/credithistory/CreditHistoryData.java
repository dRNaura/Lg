package com.lg_project.modelclass.credithistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditHistoryData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cust_id")
    @Expose
    private Integer custId;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("last_amount")
    @Expose
    private Double lastAmount;
    @SerializedName("avail_amount")
    @Expose
    private Double availAmount;
    @SerializedName("artist_id")
    @Expose
    private Object artistId;
    @SerializedName("beneficiary")
    @Expose
    private String beneficiary;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(Double lastAmount) {
        this.lastAmount = lastAmount;
    }

    public Double getAvailAmount() {
        return availAmount;
    }

    public void setAvailAmount(Double availAmount) {
        this.availAmount = availAmount;
    }

    public Object getArtistId() {
        return artistId;
    }

    public void setArtistId(Object artistId) {
        this.artistId = artistId;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

}
