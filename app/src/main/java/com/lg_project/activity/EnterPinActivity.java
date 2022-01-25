package com.lg_project.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;
import com.lg_project.modelclass.forgotpass.ResponseForgot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EnterPinActivity extends AppCompatActivity {

    TextView txtLogin;
    EditText pin1,pin_no2,pin_no3,pin_no4;
    SessionManageent session;
    String pinentered="",active_pin="";
    LinearLayout forgotpin;
    String email="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");


        session = new SessionManageent(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
         active_pin = user.get(SessionManageent.KEY_ACTIVE_PIN);
      //  Log.e("active_pin",active_pin);

        if(email.equals(""))
        {
            email = user.get(SessionManageent.KEY_EMAIL);
        }

        findView();
    }
    public void findView(){
        forgotpin=findViewById(R.id.forgotpin);
        txtLogin=findViewById(R.id.txtLogin);
        pin1=findViewById(R.id.pin1);
        pin_no2=findViewById(R.id.pin_no2);
        pin_no3=findViewById(R.id.pin_no3);
        pin_no4=findViewById(R.id.pin_no4);

        listeners();

    }
    public void listeners(){
       txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EnterPinActivity.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

        pin1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               pin_no2.requestFocus();
                String txt=s.toString();
                if(txt.equalsIgnoreCase("")){
                    pin1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pin_no2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pin_no3.requestFocus();
                String txt=s.toString();
                if(txt.equalsIgnoreCase("")){
                    pin1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        pin_no3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pin_no4.requestFocus();
                String txt=s.toString();
                if(txt.equalsIgnoreCase("")){
                    pin_no2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pin_no4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txt=s.toString();
                if(txt.equalsIgnoreCase("")){
                    pin_no3.requestFocus();
                }else{
                    matchPinEntered();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        forgotpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpass(email);
            }
        });

    }

    public void matchPinEntered(){

        pinentered=pin1.getText().toString()+pin_no2.getText().toString()+pin_no3.getText().toString()+pin_no4.getText().toString();

        if(active_pin.equalsIgnoreCase(pinentered)){
            Intent intent=new Intent(EnterPinActivity.this,MainActivity2.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
            finish();
        }else{
            pin1.setText("");
            pin_no2.setText("");
            pin_no3.setText("");
            pin_no4.setText("");
            pin1.requestFocus();
            Toast.makeText(EnterPinActivity.this,"Incorrect Pin",Toast.LENGTH_SHORT).show();
        }
    }

    public void forgotpass(String username) {

        Retrofit retrofit = RetrofitInstance.getLocale(EnterPinActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username",username);
            jsonObject.put("pin_required",true);
            jsonObject.put("user_type","2");
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
                            Toast.makeText(EnterPinActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                          /*  Intent intent = new Intent(EnterPinActivity.this, Login.class);
                            startActivity(intent);
                            finish();*/
                        } else {
                            Toast.makeText(EnterPinActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(EnterPinActivity.this, "" + errorMsg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseForgot> call, Throwable t) {
                Log.d("ERRor",t.getMessage());
                Toast.makeText(EnterPinActivity.this, ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });
    }
}