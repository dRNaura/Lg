package com.lg_project.modelclass.updatepic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lg_project.modelclass.contractordetails.ArtistPic;

import java.util.List;

public class Updatepicdata {

    @SerializedName("body_type")
    @Expose
    private Integer bodyType;
    @SerializedName("body_type_desc")
    @Expose
    private String bodyTypeDesc;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user")
    @Expose
    private Integer user;
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
    private Double height;
    @SerializedName("weight")
    @Expose
    private Integer weight;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("notification_allowed")
    @Expose
    private Integer notificationAllowed;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("ssn")
    @Expose
    private String ssn;
    @SerializedName("mailing_address")
    @Expose
    private String mailingAddress;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("hair_color")
    @Expose
    private String hairColor;
    @SerializedName("race")
    @Expose
    private Integer race;
    @SerializedName("race_desc")
    @Expose
    private String raceDesc;
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
    @SerializedName("customer_pic")
    @Expose
    private List<ArtistPic> artistPic = null;

    public Integer getBodyType() {
        return bodyType;
    }

    public void setBodyType(Integer bodyType) {
        this.bodyType = bodyType;
    }

    public String getBodyTypeDesc() {
        return bodyTypeDesc;
    }

    public void setBodyTypeDesc(String bodyTypeDesc) {
        this.bodyTypeDesc = bodyTypeDesc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
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

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getNotificationAllowed() {
        return notificationAllowed;
    }

    public void setNotificationAllowed(Integer notificationAllowed) {
        this.notificationAllowed = notificationAllowed;
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

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public Integer getRace() {
        return race;
    }

    public void setRace(Integer race) {
        this.race = race;
    }

    public String getRaceDesc() {
        return raceDesc;
    }

    public void setRaceDesc(String raceDesc) {
        this.raceDesc = raceDesc;
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

    public List<ArtistPic> getArtistPic() {
        return artistPic;
    }

    public void setArtistPic(List<ArtistPic> artistPic) {
        this.artistPic = artistPic;
    }

}