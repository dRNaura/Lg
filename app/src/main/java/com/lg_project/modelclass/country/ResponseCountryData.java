package com.lg_project.modelclass.country;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lg_project.modelclass.Members;

import java.util.List;

public class ResponseCountryData {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose

    private List<Country> data = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Country> getData() {
        return data;
    }

    public void setData(List<Country> data) {
        this.data = data;
    }

}