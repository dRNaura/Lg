package com.lg_project.modelclass.uploadimage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadArtistPic {

    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("profile")
    @Expose
    private Integer profile;
    @SerializedName("active")
    @Expose
    private String active;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getProfile() {
        return profile;
    }

    public void setProfile(Integer profile) {
        this.profile = profile;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}