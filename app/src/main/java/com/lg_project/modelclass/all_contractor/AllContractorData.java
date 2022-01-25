package com.lg_project.modelclass.all_contractor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllContractorData {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("race")
    @Expose
    private Integer race;

    @SerializedName("body_type")
    @Expose
    private Integer body_type;

    @SerializedName("isFavArtist")
    @Expose
    private Integer isFavArtist;

    public Integer getIsFavArtist() {
        return isFavArtist;
    }

    public void setIsFavArtist(Integer isFavArtist) {
        this.isFavArtist = isFavArtist;
    }

    @SerializedName("stage_name")
    @Expose
    private String stageName;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("stats")
    @Expose
    private String stats;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("body_type_desc")
    @Expose
    private String body_type_desc;

    @SerializedName("hair_color")
    @Expose
    private String hair_color;

    @SerializedName("race_desc")
    @Expose
    private String race_desc;

    public Integer getRace() {
        return race;
    }

    public void setRace(Integer race) {
        this.race = race;
    }

    public Integer getBody_type() {
        return body_type;
    }

    public void setBody_type(Integer body_type) {
        this.body_type = body_type;
    }

    public String getBody_type_desc() {
        return body_type_desc;
    }

    public void setBody_type_desc(String body_type_desc) {
        this.body_type_desc = body_type_desc;
    }

    public String getHair_color() {
        return hair_color;
    }

    public void setHair_color(String hair_color) {
        this.hair_color = hair_color;
    }

    public String getRace_desc() {
        return race_desc;
    }

    public void setRace_desc(String race_desc) {
        this.race_desc = race_desc;
    }

    public Integer getId() {
        return id;
    }



    public void setId(Integer id) {
        this.id = id;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}