package com.lg_project.modelclass.notifymsg;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfflineMsgResponseData {

        @SerializedName("success")
        @Expose
        private Boolean success;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("timestamp")
        @Expose
        private double timestamp;

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

}
