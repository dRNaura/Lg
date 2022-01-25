package com.lg_project.modelclass.state;

import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StateData implements Parcelable
{

    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("state_code")
    @Expose
    private String stateCode;
    public final static Creator<StateData> CREATOR = new Creator<StateData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public StateData createFromParcel(android.os.Parcel in) {
            return new StateData(in);
        }

        public StateData[] newArray(int size) {
            return (new StateData[size]);
        }

    }
            ;

    protected StateData(android.os.Parcel in) {
        this.state = ((String) in.readValue((String.class.getClassLoader())));
        this.stateCode = ((String) in.readValue((String.class.getClassLoader())));
    }

    public StateData() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(state);
        dest.writeValue(stateCode);
    }

    public int describeContents() {
        return 0;
    }

}
