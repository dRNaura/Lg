package com.lg_project.modelclass.contractordetails;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContractorDetailData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user")
    @Expose
    private Integer user;
    @SerializedName("stage_name")
    @Expose
    private String stageName;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("stats")
    @Expose
    private String stats;
    @SerializedName("mailing_address")
    @Expose
    private String mailingAddress;
    @SerializedName("ssn")
    @Expose
    private String ssn;
    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("body_type_desc")
    @Expose
    private String body_type_desc;

    @SerializedName("race_desc")
    @Expose
    private String race_desc;

    @SerializedName("greeting")
    @Expose
    private String greeting;

    @SerializedName("spoken_language")
    @Expose
    private String spoken_language;

    @SerializedName("artist_pic")

    @Expose
    private List<ArtistPic> artistPic = null;

    public String getSpoken_language() {
        return spoken_language;
    }

    public void setSpoken_language(String spoken_language) {
        this.spoken_language = spoken_language;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public Integer getId() {
        return id;
    }

    public String getBody_type_desc() {
        return body_type_desc;
    }

    public void setBody_type_desc(String body_type_desc) {
        this.body_type_desc = body_type_desc;
    }

    public String getRace_desc() {
        return race_desc;
    }

    public void setRace_desc(String race_desc) {
        this.race_desc = race_desc;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ArtistPic> getArtistPic() {
        return artistPic;
    }

    public void setArtistPic(List<ArtistPic> artistPic) {
        this.artistPic = artistPic;
    }

}