package com.lg_project.modelclass.creditpurchase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditPurchaseData {

    @SerializedName("credit")
    @Expose
    private Double credit;

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

}
