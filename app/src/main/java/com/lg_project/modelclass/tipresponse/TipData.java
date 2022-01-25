package com.lg_project.modelclass.tipresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TipData {

    @SerializedName("transaction_id")
    @Expose
    private String transactionId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

}