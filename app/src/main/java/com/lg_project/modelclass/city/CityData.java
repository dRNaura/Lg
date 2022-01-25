package com.lg_project.modelclass.city;

import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityData implements Parcelable
{

    @SerializedName("city")
    @Expose
    private String city;
    public final static Creator<CityData> CREATOR = new Creator<CityData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public CityData createFromParcel(android.os.Parcel in) {
            return new CityData(in);
        }

        public CityData[] newArray(int size) {
            return (new CityData[size]);
        }

    }
            ;

    protected CityData(android.os.Parcel in) {
        this.city = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CityData() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(city);
    }

    public int describeContents() {
        return 0;
    }

}