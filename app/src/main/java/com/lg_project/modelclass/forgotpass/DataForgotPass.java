package com.lg_project.modelclass.forgotpass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataForgotPass {

    @SerializedName("email")
    @Expose
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
