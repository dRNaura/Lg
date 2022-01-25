package com.lg_project.modelclass.deleteimage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteImageData {

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}