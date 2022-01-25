package com.lg_project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.modelclass.signup.ResponseSignup;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PinCode extends AppCompatActivity {

    TextView txtSkip;
    EditText one, two, three, four;
    Button otpsend;
    String str_id, str_email, str_firstname, str_lastame, str_age, str_password, str_phone, str_terms, str_pin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code);

        str_firstname = getIntent().getStringExtra("First_Name");
        str_lastame = getIntent().getStringExtra("last_name");
        str_email = getIntent().getStringExtra("email");
        str_password = getIntent().getStringExtra("password");
        str_phone = getIntent().getStringExtra("phone");
        str_age = getIntent().getStringExtra("age");
        str_terms = getIntent().getStringExtra("terms");
        str_id = getIntent().getStringExtra("country");


        txtSkip = findViewById(R.id.txtSkip);

        one = (EditText) findViewById(R.id.login_first_otp);
        two = (EditText) findViewById(R.id.login_second_otp);
        three = (EditText) findViewById(R.id.login_third_otp);
        four = (EditText) findViewById(R.id.login_four_otp);

//        one.addTextChangedListener(this);
//        two.addTextChangedListener(this);
//        three.addTextChangedListener(this);
//        four.addTextChangedListener(this);

        otpsend = (Button) findViewById(R.id.sendotp);

        one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                two.requestFocus();
                String txt = s.toString();
                if (txt.equalsIgnoreCase("")) {
                    one.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                three.requestFocus();
                String txt = s.toString();
                if (txt.equalsIgnoreCase("")) {
                    one.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                four.requestFocus();
                String txt = s.toString();
                if (txt.equalsIgnoreCase("")) {
                    two.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txt = s.toString();
                if (txt.equalsIgnoreCase("")) {
                    three.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        otpsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                str_pin = one.getText().toString() + two.getText().toString() + three.getText().toString() +
                        four.getText().toString();
                Log.d("PIN", str_pin);
                if (TextUtils.isEmpty(str_firstname) || TextUtils.isEmpty(str_lastame) || TextUtils.isEmpty(str_email)
                        || TextUtils.isEmpty(str_id) || TextUtils.isEmpty(str_age) || TextUtils.isEmpty(str_password) ||
                        TextUtils.isEmpty(str_phone) || TextUtils.isEmpty(str_terms) || TextUtils.isEmpty(str_pin)) {
                    Toast.makeText(PinCode.this, ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
                } else {
                    signUp(str_email, str_firstname, str_lastame, str_age, str_password, str_phone, str_id, str_terms, str_pin);
                }
               /* Intent intent=new Intent(PinCode.this,MainActivity.class);
                startActivity(intent);*/

            }
        });

        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                str_pin = "";
                Log.e("PIN", str_pin);
                if (TextUtils.isEmpty(str_firstname) || TextUtils.isEmpty(str_lastame) || TextUtils.isEmpty(str_email)
                        || TextUtils.isEmpty(str_id) || TextUtils.isEmpty(str_age) || TextUtils.isEmpty(str_password) ||
                        TextUtils.isEmpty(str_phone) || TextUtils.isEmpty(str_terms)) {
                    Toast.makeText(PinCode.this, ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
                } else {
                    signUp(str_email, str_firstname, str_lastame, str_age, str_password, str_phone, str_id, str_terms, str_pin);
                }
               /* Intent intent=new Intent(PinCode.this,MainActivity.class);
                startActivity(intent);*/


            }
        });
    }

    public void signUp(String email, String firstname, String lastname, String age, String password, String phone,
                       String id, String terms, String pin) {

        Retrofit retrofit = RetrofitInstance.getLocale(PinCode.this);
        Api apiService = retrofit.create(Api.class);
        Call<ResponseSignup> call = apiService.signup(ApiJsonMap(firstname, lastname, email, password, id, age, phone, terms, pin));

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseSignup>() {
            @Override
            public void onResponse(Call<ResponseSignup> call, Response<ResponseSignup> response) {
                String errorbody = "",errorMsg="";
                try {
                    if (response.errorBody() != null) {
                        errorbody = response.errorBody().string();
                        JSONObject responseObject = new JSONObject(errorbody);
                        errorMsg=responseObject.getString("message");
                    }
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            Toast.makeText(PinCode.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PinCode.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(PinCode.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PinCode.this, ""+errorMsg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseSignup> call, Throwable t) {
                Log.d("ERRor", t.getMessage());
                //Toast.makeText(Login.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private JsonObject ApiJsonMap(String firstname, String lastname, String email, String ppassword, String id,
                                  String aage, String phone, String terms, String active_pin) {

        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("first_name", firstname);
            jsonObj_.put("last_name", lastname);
            jsonObj_.put("username", email);
            jsonObj_.put("password", ppassword);
            jsonObj_.put("country", id);
            jsonObj_.put("age", aage);
            jsonObj_.put("phone", phone);
            jsonObj_.put("term", terms);
            jsonObj_.put("active_pin", active_pin);
            jsonObj_.put("state", "active");


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