package com.lg_project.modelclass.mynotification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMyNotification {

        @SerializedName("success")
        @Expose
        private Boolean success;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("timestamp")
        @Expose
        private double timestamp;
        @SerializedName("data")
        @Expose
        private List<MyNotificatinData> data = null;

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

        public double getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(double timestamp) {
            this.timestamp = timestamp;
        }

        public List<MyNotificatinData> getData() {
            return data;
        }

        public void setData(List<MyNotificatinData> data) {
            this.data = data;
        }

}
