package com.lg_project.modelclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Members {

//    {"type_desc":"Cheer Leader","type":12,"first_name":"sanjay","last_name":"dhiman","payment_acc_id":"sanjaydhiman",
//            "user":476,"id":4},

    @SerializedName("user")
    @Expose
    private int user;

    @SerializedName("first_name")
    @Expose
    private String first_name;

   @SerializedName("last_name")
    @Expose
    private String last_name;


    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }




}
