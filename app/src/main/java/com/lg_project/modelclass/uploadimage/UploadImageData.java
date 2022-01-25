package com.lg_project.modelclass.uploadimage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UploadImageData {

    @SerializedName("customer_pic")
    @Expose
    private List<UploadArtistPic> artistPic = null;

    public List<UploadArtistPic> getArtistPic() {
        return artistPic;
    }

    public void setArtistPic(List<UploadArtistPic> artistPic) {
        this.artistPic = artistPic;
    }

}