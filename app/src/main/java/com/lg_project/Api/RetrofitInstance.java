package com.lg_project.Api;

import android.content.Context;
import android.content.SharedPreferences;

import com.lg_project.Session.SessionManageent;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://lgtulsa.com:8080/club-api/api/v1/";
   // private static final String BASE_URL_Socket= "http://15.207.210.221:8484/";
        private static final String BASE_URL_Socket= "http://3.99.23.167:8080";

    /**
     * Create an instance of Retrofit object
     * */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClient(Context context) {
        SessionManageent sessionManageent=new SessionManageent(context);
        HashMap<String, String> user=sessionManageent.getUserDetails();
        String token=user.get(SessionManageent.KEY_TOKEN);

        Retrofit retrofit = null;
        if (context != null) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {

                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            //  .header("Authorization", "Bearer "+AppConstant.TEMP_AUTH)
                            .header("Authorization", "Bearer "+token)
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });
//
            OkHttpClient client = httpClient.build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_Socket)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

//                OkHttpClient client = new OkHttpClient.Builder()
//                        .connectTimeout(100, TimeUnit.SECONDS)
//                        .readTimeout(100,TimeUnit.SECONDS).build();
//                retrofit = new Retrofit.Builder()
//                        .baseUrl(AppConstant.BASE_URL).client(client)
//                        .addConverterFactory(GsonConverterFactory.create(new Gson())).build();


            return retrofit;
        } else {


        }

        return retrofit;
    }

    public static Retrofit getLocale(Context context) {

        SessionManageent session = new SessionManageent(context);
        HashMap<String, String> language = session.getAppLanguage();
        String applanguage=language.get(SessionManageent.KEY_APP_LANGUAGE);

        HashMap<String, String> toekn = session.getUserDetails();
        String bearerToekn=toekn.get(SessionManageent.KEY_TOKEN);

        Retrofit retrofit = null;
        if (context != null) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {

                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("locale", applanguage)
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            .header("Authorization", "Bearer "+bearerToekn)
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });

            OkHttpClient client = httpClient.build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

            return retrofit;
        } else {


        }

        return retrofit;
    }

}