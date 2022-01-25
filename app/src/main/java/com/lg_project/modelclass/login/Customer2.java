package com.lg_project.modelclass.login;

import java.util.List;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Customer2 implements Parcelable
{

    @SerializedName("credit")
    @Expose
    private Integer credit;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user")
    @Expose
    private Integer user;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("country")
    @Expose
    private Integer country;
    @SerializedName("active_pin")
    @Expose
    private String activePin;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("height")
    @Expose
    private Object height;
    @SerializedName("weight")
    @Expose
    private Object weight;
    @SerializedName("city")
    @Expose
    private Object city;
    @SerializedName("province")
    @Expose
    private Object province;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("prefered_notification")
    @Expose
    private List<Object> preferedNotification = null;
    @SerializedName("prefered_settings")
    @Expose
    private List<PreferedSettingModel> preferedSettings = null;
    @SerializedName("favourite_artist")
    @Expose
    private List<FavouriteArtist> favouriteArtist = null;
    @SerializedName("customer_pic")
    @Expose
    private List<Object> customerPic = null;
    @SerializedName("booking_url")
    @Expose
    private String bookingUrl;
    public final static Creator<Customer2> CREATOR = new Creator<Customer2>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Customer2 createFromParcel(android.os.Parcel in) {
            return new Customer2(in);
        }

        public Customer2[] newArray(int size) {
            return (new Customer2[size]);
        }

    }
            ;

    protected Customer2(android.os.Parcel in) {
        this.credit = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.user = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.age = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.phone = ((String) in.readValue((String.class.getClassLoader())));
        this.country = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.activePin = ((String) in.readValue((String.class.getClassLoader())));
        this.state = ((String) in.readValue((String.class.getClassLoader())));
        this.height = ((Object) in.readValue((Object.class.getClassLoader())));
        this.weight = ((Object) in.readValue((Object.class.getClassLoader())));
        this.city = ((Object) in.readValue((Object.class.getClassLoader())));
        this.province = ((Object) in.readValue((Object.class.getClassLoader())));
        this.username = ((String) in.readValue((String.class.getClassLoader())));
        this.language = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.preferedNotification, (java.lang.Object.class.getClassLoader()));
        in.readList(this.preferedSettings, (PreferedSettingModel.class.getClassLoader()));
        in.readList(this.favouriteArtist, (FavouriteArtist.class.getClassLoader()));
        in.readList(this.customerPic, (java.lang.Object.class.getClassLoader()));
        this.bookingUrl = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Customer2() {
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public String getActivePin() {
        return activePin;
    }

    public void setActivePin(String activePin) {
        this.activePin = activePin;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Object getHeight() {
        return height;
    }

    public void setHeight(Object height) {
        this.height = height;
    }

    public Object getWeight() {
        return weight;
    }

    public void setWeight(Object weight) {
        this.weight = weight;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Object getProvince() {
        return province;
    }

    public void setProvince(Object province) {
        this.province = province;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<Object> getPreferedNotification() {
        return preferedNotification;
    }

    public void setPreferedNotification(List<Object> preferedNotification) {
        this.preferedNotification = preferedNotification;
    }

    public List<PreferedSettingModel> getPreferedSettings() {
        return preferedSettings;
    }

    public void setPreferedSettings(List<PreferedSettingModel> preferedSettings) {
        this.preferedSettings = preferedSettings;
    }

    public List<FavouriteArtist> getFavouriteArtist() {
        return favouriteArtist;
    }

    public void setFavouriteArtist(List<FavouriteArtist> favouriteArtist) {
        this.favouriteArtist = favouriteArtist;
    }

    public List<Object> getCustomerPic() {
        return customerPic;
    }

    public void setCustomerPic(List<Object> customerPic) {
        this.customerPic = customerPic;
    }

    public String getBookingUrl() {
        return bookingUrl;
    }

    public void setBookingUrl(String bookingUrl) {
        this.bookingUrl = bookingUrl;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(credit);
        dest.writeValue(id);
        dest.writeValue(user);
        dest.writeValue(age);
        dest.writeValue(phone);
        dest.writeValue(country);
        dest.writeValue(activePin);
        dest.writeValue(state);
        dest.writeValue(height);
        dest.writeValue(weight);
        dest.writeValue(city);
        dest.writeValue(province);
        dest.writeValue(username);
        dest.writeValue(language);
        dest.writeList(preferedNotification);
        dest.writeList(preferedSettings);
        dest.writeList(favouriteArtist);
        dest.writeList(customerPic);
        dest.writeValue(bookingUrl);
    }

    public int describeContents() {
        return 0;
    }

}