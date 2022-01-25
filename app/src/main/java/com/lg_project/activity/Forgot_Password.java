package com.lg_project.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.modelclass.forgotpass.ResponseForgot;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Forgot_Password extends AppCompatActivity {

    TextInputEditText email;
    Button submit;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);

        email=(TextInputEditText)findViewById(R.id.forgotpassmail);
        submit=(Button)findViewById(R.id.submit_pass);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email=email.getText().toString();
              //  Toast.makeText(Forgot_Password.this, ""+str_email, Toast.LENGTH_SHORT).show();
                if(str_email.isEmpty()){
                    Toast.makeText(Forgot_Password.this, ""+getResources().getString(R.string.fill_email), Toast.LENGTH_SHORT).show();
                }else{
                    forgotpass(str_email);
                }

            }
        });


    }

    public void forgotpass(String username) {

        Retrofit retrofit = RetrofitInstance.getLocale(Forgot_Password.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username",username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("ObjArray", jsonObject.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Api apiService = retrofit.create(Api.class);
        Call<ResponseForgot> call = apiService.forgotpass(body);
        call.enqueue(new Callback<ResponseForgot>() {
            @Override
            public void onResponse(Call<ResponseForgot> call, Response<ResponseForgot> response) {
                String errorbody = "",errorMsg="";
                try {
                    if (response.errorBody() != null) {
                        errorbody = response.errorBody().string();
                        JSONObject responseObject = new JSONObject(errorbody);
                        errorMsg=responseObject.getString("message");

//
                    }
                    if (response.isSuccessful()) {
                        if (response.body().getMessage().equals("Success")) {
                            Toast.makeText(Forgot_Password.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Forgot_Password.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Forgot_Password.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(Forgot_Password.this, "" + errorMsg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseForgot> call, Throwable t) {
                Log.d("ERRor",t.getMessage());
                Toast.makeText(Forgot_Password.this, ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private JsonObject ApiJsonMap(String user) {

        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("username", user);


            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());

            //print parameter
            Log.e("MY gson.JSON:  ", "AS PARAMETER  " + gsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gsonObject;
    }
}