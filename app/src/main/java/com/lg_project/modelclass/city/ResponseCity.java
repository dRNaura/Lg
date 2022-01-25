package com.lg_project.modelclass.city;

import java.util.List;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseCity implements Parcelable
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
    private List<CityData> data = null;
    public final static Creator<ResponseCity> CREATOR = new Creator<ResponseCity>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ResponseCity createFromParcel(android.os.Parcel in) {
            return new ResponseCity(in);
        }

        public ResponseCity[] newArray(int size) {
            return (new ResponseCity[size]);
        }

    }
            ;

    protected ResponseCity(android.os.Parcel in) {
        this.success = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.timestamp = ((Long) in.readValue((Long.class.getClassLoader())));
        in.readList(this.data, (CityData.class.getClassLoader()));
    }

    public ResponseCity() {
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

    public List<CityData> getData() {
        return data;
    }

    public void setData(List<CityData> data) {
        this.data = data;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(message);
        dest.writeValue(timestamp);
        dest.writeList(data);
    }

    public int describeContents() {
        return 0;
    }

}