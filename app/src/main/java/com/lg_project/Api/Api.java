package com.lg_project.Api;


import com.google.gson.JsonObject;
import com.lg_project.modelclass.FavResponseData;
import com.lg_project.modelclass.ResponseChatHistory;
import com.lg_project.modelclass.ResponseCustomerListDetail;
import com.lg_project.modelclass.ResponseMemberData;
import com.lg_project.modelclass.ResponseMypayment.ResponseMyPayment;
import com.lg_project.modelclass.ResponseTipCollgue;
import com.lg_project.modelclass.all_contractor.ResponseAllContractor;
import com.lg_project.modelclass.city.ResponseCity;
import com.lg_project.modelclass.contractordetails.ResponseContractorDetails;
import com.lg_project.modelclass.country.ResponseCountryData;
import com.lg_project.modelclass.credithistory.ResponseCreditHistory;
import com.lg_project.modelclass.creditplan.ResponseCreditPlan;
import com.lg_project.modelclass.creditpurchase.ResponseCreditPurchase;
import com.lg_project.modelclass.deleteimage.ResponseDeleteImage;
import com.lg_project.modelclass.editprofile.ResponseEditProfile;
import com.lg_project.modelclass.enquiry.ResponseEnquiry;
import com.lg_project.modelclass.forgotpass.ResponseForgot;
import com.lg_project.modelclass.login.ResponseData;
import com.lg_project.modelclass.menu.ResponseMenu;
import com.lg_project.modelclass.menu.ResponseMenuImage;
import com.lg_project.modelclass.mynotification.ResponseMyNotification;
import com.lg_project.modelclass.notifymsg.OfflineMsgResponseData;
import com.lg_project.modelclass.profilepic.ResponseProfilePic;
import com.lg_project.modelclass.signup.ResponseSignup;
import com.lg_project.modelclass.state.ResponseState;
import com.lg_project.modelclass.tipresponse.ResponseTip;
import com.lg_project.modelclass.updatepic.ResponseUpdatePic;
import com.lg_project.modelclass.uploadimage.ResponseUploadImage;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface Api {
    /* @Headers({
             "Accept: application/json",
             "Content-Type: app'lication/json"
     })*/
    @POST("user/login/customer")
    Call<ResponseData> login(@Body JsonObject locationPost);

    @GET("country")
    Call<ResponseCountryData> country();

    @GET("country/{state}/")
    Call<ResponseState> state(@Path("state") String state);

    @GET("country/{state}/{city}/")
    Call<ResponseCity> city(@Path("state") String state, @Path("city") String city);

    @GET("user/staff/all/types/info")
    Call<ResponseCountryData> getStaff();

    @GET("user/staff/type/{id}")
    Call<ResponseMemberData> getStaffMember(@Path("id") String id);

    @POST("customer")
    Call<ResponseSignup> signup(@Body JsonObject locationPost);


    @POST("user/forgotpassword")
    Call<ResponseForgot> forgotpass(@Body RequestBody body);

    @GET("contractor/customer/{id}")
    Call<ResponseAllContractor> contractor(@Path("id") String id);

    @GET("contractor/customer/{id}")
    Call<ResponseCustomerListDetail> contractorr(@Path("id") String id);

    @GET("contractor/{id}")
    Call<ResponseContractorDetails> detail(@Path("id") String id);

    @GET("customer/{id}")
    Call<ResponseEditProfile> customerdetail(@Path("id") String id);

    @GET("contractor")
    Call<ResponseContractorDetails> allcontractor();

    @GET("club/menu")
    Call<ResponseMenu> menu();

    @GET("club/menu/{id}")
    Call<ResponseMenuImage> menuimage(@Path("id") String id);

    @POST("tippayment/online")
    Call<ResponseTip> Tipartist(@Body RequestBody options);

    @GET("tippayment/sender/{id}")
    Call<ResponseMyPayment> payment(@Path("id") String id);

    @POST("enquiry")
    Call<ResponseEnquiry> enquiry(@Body JsonObject locationPost);

    @GET("credit/history/{id}")
    Call<ResponseCreditHistory> credithistory(@Path("id") String id);

    @GET("customer/favourite/{id}")
    Call<ResponseAllContractor> favList(@Path("id") String id);

//    @DELETE("customer/favourite/delete}")
//    Call<ResponseData> favDelete(@Body RequestBody options);

    @HTTP(method = "DELETE", path = "customer/favourite/delete", hasBody = true)
    Call<FavResponseData> favDelete(@Body RequestBody options);


    @POST("customer/favourite")
    Call<FavResponseData> favAdd(@Body RequestBody options);

    @GET("credit/plan")
    Call<ResponseCreditPlan> plan();

    @POST("credit/purchase")
    Call<ResponseCreditPurchase> creditpurchase(@Body JsonObject locationPost);

    @POST("user/update/device")
    Call<OfflineMsgResponseData> updateDevice(@Body RequestBody options);

    @HTTP(method = "DELETE", path = "user/update/device/delete", hasBody = true)
    Call<FavResponseData> deleteDevice(@Body RequestBody options);

    @PUT("customer")
    Call<ResponseBody> editProfile(@Body RequestBody options);

    @GET("contractor/races/all")
    Call<ResponseBody> getAllRaces();

    @GET("user/setup/body/types")
    Call<ResponseBody> getBodyType();

    @GET("contractor/hair/color/all")
    Call<ResponseBody> getHairColor();

    @POST()
    Call<ResponseChatHistory> chatHistory(@Url String url, @Body RequestBody body);

    @POST("customer/sendOfflineMsg")
    Call<OfflineMsgResponseData> notifymsg(@Body JsonObject locationPost);

    @GET("user/{id}/sent/notifications")
    Call<ResponseMyNotification> notifications(@Path("id") String id);

    @GET("customer/stealth/all")
    Call<ResponseBody> all_stealth();

    @POST()
    Call<ResponseBody> configureStealth(@Url String url, @Body RequestBody body);

    @POST()
    Call<ResponseBody> chatInfo(@Url String url, @Body RequestBody body);

    @POST()
    Call<ResponseBody> tipcollegue(@Url String url, @Body RequestBody body);

    @GET("user/setup/spoken/languages")
    Call<ResponseBody> getSpokenLanguages();

    @PUT("customer/set/pics/profile")
    Call<ResponseProfilePic> profilepic(@Body JsonObject locationPost);

    @DELETE("customer/{id}/pics/{image_id}")
    Call<ResponseDeleteImage> deleteimage(@Path("id") String id,
                                          @Path("image_id") String image_id);

    @Multipart
    @POST("file/customer")
    Call<ResponseUploadImage> uploadImage(@Part MultipartBody.Part image,
                                          @Part("customer") RequestBody id);

    //check
    @PUT("customer/")
    Call<ResponseUpdatePic> uploadprofile(@Body JsonObject locationPost);


//    @POST("tippayment/online")
//    Call<ResponseTipCollgue> tipcollegue(@Body JsonObject locationPost);

}