package com.lg_project.modelclass.login;

import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavouriteArtist implements Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cust_id")
    @Expose
    private Integer custId;
    @SerializedName("stage_name")
    @Expose
    private String stageName;
    public final static Creator<FavouriteArtist> CREATOR = new Creator<FavouriteArtist>() {


        @SuppressWarnings({
                "unchecked"
        })
        public FavouriteArtist createFromParcel(android.os.Parcel in) {
            return new FavouriteArtist(in);
        }

        public FavouriteArtist[] newArray(int size) {
            return (new FavouriteArtist[size]);
        }

    }
            ;

    protected FavouriteArtist(android.os.Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.custId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.stageName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public FavouriteArtist() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(custId);
        dest.writeValue(stageName);
    }

    public int describeContents() {
        return 0;
    }

}
