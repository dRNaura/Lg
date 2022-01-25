package com.lg_project.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;
import com.lg_project.modelclass.login.Customer;
import com.lg_project.modelclass.login.Customer2;
import com.lg_project.modelclass.login.Datum;
import com.lg_project.modelclass.login.PreferedSettingModel;
import com.lg_project.modelclass.login.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    TextView signup,forgot;
    Button b1;
    TextInputEditText email,password;

    // Session Manager Class
        SessionManageent session;
    String applanguage;
    ProgressBar progressBar;
    Sprite wave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManageent(getApplicationContext());
//        HashMap<String, String> language = session.getAppLanguage();
//        // email
//        applanguage=language.get(SessionManageent.KEY_APP_LANGUAGE);
//        Log.d("AppLanguage",applanguage);
//        setAppLocale(applanguage);

        setContentView(R.layout.activity_login);

        // Session Manager
//        Log.e("langgg",""+Locale.getDefault().getLanguage());
//        applanguage=Locale.getDefault().getLanguage();
//        session.AppLanguage(applanguage);
//        setAppLocale(applanguage);

      //  Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
        progressBar = findViewById(R.id.progress);
        wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        progressBar.setBackgroundResource(android.R.color.transparent);

        email=(TextInputEditText)findViewById(R.id.text_email);
        password=(TextInputEditText)findViewById(R.id.text_password);

        signup=(TextView)findViewById(R.id.textview_signup);
//        String text = "<font color=#FFFFFF>Don't have an account? </font> <font color=#E31717>Sign Up</font>";
//        signup.setText(Html.fromHtml(text));
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });

        forgot=(TextView)findViewById(R.id.forgot_pass);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Forgot_Password.class);
                startActivity(intent);
            }
        });

        b1=(Button)findViewById(R.id.login);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stremail=email.getText().toString();
                String strpass=password.getText().toString();
                if(TextUtils.isEmpty(stremail) || TextUtils.isEmpty(strpass))
                {
                    Toast.makeText(Login.this, "Please Fill all Fields !!!", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    login(stremail,strpass);
                }
              /*  Intent intent=new Intent(Login.this,MainActivity.class);
                startActivity(intent);
                session.createLoginSession("12345","test@gmail.com");
*/
            }
        });

    }

    public void login(String username,String password) {
        progressBar.setVisibility(View.VISIBLE);
        Api api = RetrofitInstance.getRetrofitInstance().create(Api.class);
        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseData> call = api.login(ApiJsonMap(username,password));
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call,Response<ResponseData> response) {

                String errorbody = "",errorMsg="";
                try {
                    if (response.errorBody() != null) {
                        errorbody = response.errorBody().string();
                        JSONObject responseObject = new JSONObject(errorbody);
                        errorMsg=responseObject.getString("message");
                    }
                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if(response.body().getSuccess())
                        {
                            Log.d("SUCCESS", String.valueOf(response.body().getData()));
                            // Creating user login session
                            // For testing i am stroing name, email as follow
                            // Use user real data
                            List<Datum> list=response.body().getData();
                            String firstName=list.get(0).getFirstName()+" "+list.get(0).getLastName();
                            String lastName=list.get(0).getLastName();
                            int type=list.get(0).getType();
                            String token=list.get(0).getToken();

                            Customer2 customer=list.get(0).getCustomer();
//                        List<Value> values=customer.getValue();

                            double credit=customer.getCredit();
                            int user_id=customer.getUser();
                            int customer_id=customer.getId();
                            String active_pin=customer.getActivePin();
                            int age=customer.getAge();
                            int country=customer.getCountry();
                            String phone=customer.getPhone();
                            String  state="";
                            if(customer.getState() !=null){
                                 state=customer.getState();
                            }
                            String language=customer.getLanguage();
                            List<PreferedSettingModel> preferedSettingModel=customer.getPreferedSettings();
                            if(preferedSettingModel.size() != 0) {
                                String app_icon = preferedSettingModel.get(0).getApp_icon();
                                int stealth_mode = preferedSettingModel.get(0).getStealth_mode();
                                int stealth_code = preferedSettingModel.get(0).getStealth_code();
                                session.preferedSetting(app_icon, stealth_code, stealth_mode, "");
                            }
                            else
                            {
                                session.preferedSetting("", 0, 0, "");
                            }
                            session.AppLanguage(language);
//                        setAppLocale(language);
                            session.createLoginSession(firstName,password, username,user_id, String.valueOf(credit),customer_id,
                                    active_pin,type,token);
                            session.getInfoFromLoginSession(age,phone,country,state);

                            Intent intent=new Intent(Login.this,EnterPinActivity.class);
                            intent.putExtra("email",username);
                            startActivity(intent);
                            finish();
                            Toast.makeText(Login.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(Login.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        //  List<Datum> list = response.body().getData();
                   /* for (int i = 0; i < list.size(); i++) {
                        Log.d("TEST", list.get(i).getFirstName());
                    }*/
                    }
                    else
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(Login.this, ""+errorMsg, Toast.LENGTH_SHORT).show();

                    }
                }catch (Exception e){
                    Log.e("Excepion",e.toString());
                }

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.e("ERRor",t.getMessage());
                //Toast.makeText(Login.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private JsonObject ApiJsonMap(String username,String password) {

        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("username",username);
            jsonObj_.put("password",password);


            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());

            //print parameter
            Log.e("MY gson.JSON:  ", "AS PARAMETER  " + gsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gsonObject;
    }

    private void setAppLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);

    }

}