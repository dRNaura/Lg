package com.lg_project.modelclass.editprofile;

import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseEditProfile implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("timestamp")
    @Expose
    private Long timestamp;
    @SerializedName("data")
    @Expose
    private ResponseData data;
    public final static Creator<ResponseEditProfile> CREATOR = new Creator<ResponseEditProfile>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ResponseEditProfile createFromParcel(android.os.Parcel in) {
            return new ResponseEditProfile(in);
        }

        public ResponseEditProfile[] newArray(int size) {
            return (new ResponseEditProfile[size]);
        }

    }
            ;

    protected ResponseEditProfile(android.os.Parcel in) {
        this.success = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.timestamp = ((Long) in.readValue((Long.class.getClassLoader())));
        this.data = ((ResponseData) in.readValue((ResponseData.class.getClassLoader())));
    }

    public ResponseEditProfile() {
    }

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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public ResponseData getData() {
        return data;
    }

    public void setData(ResponseData data) {
        this.data = data;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(message);
        dest.writeValue(timestamp);
        dest.writeValue(data);
    }

    public int describeContents() {
        return 0;
    }

}