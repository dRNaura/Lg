package com.lg_project.modelclass.ResponseMypayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyPaymentData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sender_desc")
    @Expose
    private String senderDesc;
    @SerializedName("beneficiary_desc")
    @Expose
    private String beneficiaryDesc;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("amount_offline")
    @Expose
    private Integer amountOffline;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSenderDesc() {
        return senderDesc;
    }

    public void setSenderDesc(String senderDesc) {
        this.senderDesc = senderDesc;
    }

    public String getBeneficiaryDesc() {
        return beneficiaryDesc;
    }

    public void setBeneficiaryDesc(String beneficiaryDesc) {
        this.beneficiaryDesc = beneficiaryDesc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Integer getAmountOffline() {
        return amountOffline;
    }

    public void setAmountOffline(Integer amountOffline) {
        this.amountOffline = amountOffline;
    }

}