package com.lg_project.modelclass.mynotification;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyNotificatinData {

        @SerializedName("msg")
        @Expose
        private String msg;
        @SerializedName("date")
        @Expose
        private String date;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

}
