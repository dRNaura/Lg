package com.lg_project.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.activity.MainActivity2;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Setting extends Fragment {

    LinearLayout back;
    Switch switch_compat;
//    SharedPreferences sharedPreferences;
//    SharedPreferences.Editor editor;
    SessionManageent session;
    public  String active_pin="",id="",state="",password="";
    TextView txt_pinLock;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        findViews(v);
    }
    public void findViews(View v){

        session = new SessionManageent(getContext());

//        sharedPreferences = getActivity().getSharedPreferences("check_pin_option", MODE_PRIVATE);
//        editor = sharedPreferences.edit();

        HashMap<String, String> user = session.getUserDetails();

        id = user.get(SessionManageent.KEY_CUST_ID);

        state = user.get(session.KEY_STATE);
        password = user.get(SessionManageent.KEY_PASSWORD);

        back =v.findViewById(R.id.back);
        switch_compat =v.findViewById(R.id.switch_compat);
        txt_pinLock =v.findViewById(R.id.txt_pinLock);

        boolean pinAsked = session.isPinAsked();
        Log.e("pinAsked", String.valueOf(pinAsked));

        if(pinAsked){
            switch_compat.setChecked(true);
        }else{
            switch_compat.setChecked(false);
        }

//        switch_compat.getTrackDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.red), PorterDuff.Mode.SRC_IN);



        clickListeners();
    }

    public void clickListeners(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity2.class);
                startActivity(intent);
                onDestroy();
            }
        });
        txt_pinLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDilaogToReset();
            }
        });
        switch_compat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==false){
                    switch_compat.setChecked(true);
                    Toast.makeText(getContext(),"You can't turn off the pin",Toast.LENGTH_SHORT).show();
                }
                session.pinAskedSession(true);

//                    switch_compat.getTrackDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.red), PorterDuff.Mode.SRC_IN);
            }
        });
    }
    public void openDilaogToReset(){

        final Dialog optionDialog = new Dialog(getContext());
        optionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        optionDialog.setContentView(R.layout.pin_enable_dilaog);
        optionDialog.setCancelable(true);
        Window window = optionDialog.getWindow();
        window.setLayout(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        EditText et_pin= optionDialog.findViewById(R.id.et_pin);
        TextView txt_save= optionDialog.findViewById(R.id.txt_save);
        TextView txt_Cancel= optionDialog.findViewById(R.id.txt_Cancel);
        HashMap<String, String> user = session.getUserDetails();
        active_pin = user.get(SessionManageent.KEY_ACTIVE_PIN);

        et_pin.setText(active_pin);

        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("pn",et_pin.getText().toString());
                if(et_pin.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getContext(),"Please Enter 4 digit Pin",Toast.LENGTH_SHORT).show();
                }else if(et_pin.getText().toString().length() < 4){
                    Toast.makeText(getContext(),"Enter 4 digit Pin",Toast.LENGTH_SHORT).show();
                }
//                else if(Integer.parseInt(et_pin.getText().toString()) > 4){
//                    Toast.makeText(getContext(),"Enter 4 digit Pin",Toast.LENGTH_SHORT).show();
//                }
                else{
                    optionDialog.dismiss();
                    updatePin(id,et_pin.getText().toString(),state);
                }

            }
        });
        txt_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionDialog.dismiss();
            }
        });
        optionDialog.show();
        optionDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void updatePin(String st_save_id, String st_save_activepin, String state) {
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",Integer.parseInt(st_save_id));
            jsonObject.put("active_pin", st_save_activepin);
            jsonObject.put("state", state);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("OUTTTT", String.valueOf(jsonObject));
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.editProfile(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String errorbody = "", errorCode = "";
                    if (response.errorBody() != null) {
                        errorbody = response.errorBody().string();
                        JSONObject responseObject = new JSONObject(errorbody);
                        if (responseObject.has("message") && !responseObject.isNull("message")) {
                            errorCode = responseObject.getString("message");
                        }
                        if (errorCode.equalsIgnoreCase("Unautherized access.")) {
                            session.logoutUser();
                            Toast.makeText(getContext(), "" + getResources().getString(R.string.session), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(getContext(), "" +errorCode, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.body() != null) {
                        String respo = response.body().string();
                        JSONObject responseObject = new JSONObject(respo);
                        String status = responseObject.optString("success");
                        String message = responseObject.optString("message");
                        if (status.equalsIgnoreCase("true")) {
                            JSONObject object=responseObject.getJSONObject("data");
                            String first_name=object.getString("first_name");
                            String last_name=object.getString("last_name");
                            String activePIn=object.getString("active_pin");
                            String username=object.getString("username");
                            String phone=object.getString("phone");
                            String state=object.getString("state");
                            int age=object.getInt("age");
                            int country=object.getInt("country");
                            HashMap<String, String> user = session.getUserDetails();
                            String credit = user.get(SessionManageent.KEY_Credit);
                            int userID = Integer.parseInt(user.get(SessionManageent.KEY_ID));
                            int type = Integer.parseInt(user.get(SessionManageent.KEY_USER_TYPE));
                            String token =user.get(SessionManageent.KEY_TOKEN);
                            session.createLoginSession(first_name+" "+last_name,password,username,userID,credit, Integer.parseInt(id),activePIn,type,token);
                            Toast.makeText(getContext(),""+message,Toast.LENGTH_SHORT).show();
                           /* Intent intent = new Intent(getContext(), MainActivity2.class);
                            startActivity(intent);
                            onDestroy();*/
                        }
                    } else {
                        Toast.makeText(getContext(),""+errorbody,Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("exception", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ERRor", t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
