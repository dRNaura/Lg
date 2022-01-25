package com.lg_project.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lg_project.activity.MainActivity2;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;
import com.lg_project.modelclass.enquiry.ResponseEnquiry;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ContactUs extends Fragment {

    TextInputEditText name,email,message;
    Button submit;
    private LinearLayout back;
    private SessionManageent session;
    String firstname="",userEMail="";
    ProgressBar progressBar;
    Sprite wave;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        MainActivity2.heading.setText(R.string.contactus);
        MainActivity2.l1.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_contact_us, container, false);

        return v;
    }

    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        session = new SessionManageent(getContext());

        progressBar = v.findViewById(R.id.progress);
         wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        progressBar.setBackgroundResource(android.R.color.transparent);

        back=(LinearLayout)v.findViewById(R.id.back);
        name=(TextInputEditText)v.findViewById(R.id.name_contact);
        email=(TextInputEditText)v.findViewById(R.id.email_contact);
        message=(TextInputEditText)v.findViewById(R.id.message_contact);
        submit=(Button)v.findViewById(R.id.submit_contact);

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // email
        firstname = user.get(SessionManageent.KEY_FIRSTNAME);
        userEMail = user.get(SessionManageent.KEY_EMAIL);
        Log.e("ERER", String.valueOf(email));

        name.setText(firstname);
        email.setText(userEMail);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), MainActivity2.class);
                startActivity(intent);
                onDestroy();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str_name=name.getText().toString();
                String str_email=email.getText().toString();
                String str_message=message.getText().toString();


                HashMap<String, String> user1 = session.getUserDetails();
                int id = Integer.parseInt(user1.get(SessionManageent.KEY_ID));
                Log.d("ERER", String.valueOf(id));

                if(TextUtils.isEmpty(str_name) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_message))
                {
                    Toast.makeText(getContext(), ""+getResources().getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    enquiry(String.valueOf(id),str_message);
                }

            }
        });






    }

    public void enquiry(String id,String message)
    {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);
        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseEnquiry> call = api.enquiry(ApiJsonMap(id,message));

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseEnquiry>() {
            @Override
            public void onResponse(Call<ResponseEnquiry> call, Response<ResponseEnquiry> response) {

                Log.d("SUCCESS", String.valueOf(response.body()));
                // Log.d("SUCCESSLOG", String.valueOf(response.body().getSuccess()));
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
                           // Toast.makeText(getContext(), "" + getResources().getString(R.string.session), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(getContext(), "" +errorCode, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.isSuccessful()) {

                        if (response.body().getSuccess()) {
                            Log.d("SUCCESS", String.valueOf(response.body().getSuccess()));
                            // Creating user login session
                            // For testing i am stroing name, email as follow
                            // Use user real data
                            //  session.createLoginSession(password, username,id);

                            Intent intent = new Intent(getContext(), MainActivity2.class);
                            startActivity(intent);
                            getActivity().finish();
                            progressBar.setVisibility(View.INVISIBLE);

                        } else {
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        //  List<Datum> list = response.body().getData();
                   /* for (int i = 0; i < list.size(); i++) {
                        Log.d("TEST", list.get(i).getFirstName());
                    }*/
                    } else {
                        Toast.makeText(getContext(), "" + getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {

                }
            }
            @Override
            public void onFailure(Call<ResponseEnquiry> call, Throwable t) {
                Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private JsonObject ApiJsonMap(String username, String password) {

        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("id",username);
            jsonObj_.put("question",password);


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