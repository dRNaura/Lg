package com.lg_project.fragment;

import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;
import com.lg_project.activity.MainActivity2;
import com.lg_project.modelclass.Members;
import com.lg_project.modelclass.ResponseMemberData;
import com.lg_project.modelclass.ResponseTipCollgue;
import com.lg_project.modelclass.country.Country;
import com.lg_project.modelclass.country.ResponseCountryData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class HighFiveFragment extends Fragment {
    SessionManageent session;
    EditText et_amount;
    Spinner spinner_staff_type,spinner_member;
    Button btn_make_payment;
    String[] staffType;
    String[] staffTypeID;

    String[] staffMember;
    String[] staffMemberID;
    String str_staffTypeID="",strStaffMemberID="";
    Double amount=0.0;
    public static final int PAYPAL_REQUEST_CODE = 123;
    int user_id=0;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_high_five, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        MainActivity2.heading.setText(R.string.high_Five);
        MainActivity2.l1.setVisibility(View.VISIBLE);
    }

    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        session = new SessionManageent(getContext());
        HashMap<String, String> user = session.getUserDetails();
         user_id = Integer.parseInt(user.get(SessionManageent.KEY_ID));

        findViews(v);
    }
    public void findViews(View v){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading.....");
        spinner_staff_type=v.findViewById(R.id.spinner_staff_type);
        spinner_member=v.findViewById(R.id.spinner_member);
        et_amount=v.findViewById(R.id.et_amount);
        btn_make_payment=v.findViewById(R.id.btn_make_payment);

        listeners();
    }
    public void listeners(){

        spinner_staff_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ID",staffTypeID[position]);
                str_staffTypeID=staffTypeID[position];

                getStaffMember(str_staffTypeID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_member.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ID",staffMemberID[position]);
                strStaffMemberID=staffMemberID[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_amount.getText().toString().isEmpty()){
                    amount = Double.valueOf(et_amount.getText().toString());
                    if(strStaffMemberID.equalsIgnoreCase("")){
                        Toast.makeText(getContext(), "" + getResources().getString(R.string.select_member), Toast.LENGTH_SHORT).show();
                    }  else if(amount <= 0) {
                        Toast.makeText(getContext(), "" + getResources().getString(R.string.greater_amount), Toast.LENGTH_SHORT).show();
                    } else {
                        onBraintreeSubmit(amount);
                    }
                }

            }
        });


        getStaffType();
    }
    public void onBraintreeSubmit(Double amount) {

        //sandbox_jyyn76fq_yhfkjhsf8fnbfj58
//sandbox_css89xw8_yhfkjhsf8fnbfj58

        DropInRequest dropInRequest = new DropInRequest().tokenizationKey("sandbox_mfqdhxgd_tqvvgtyd3qttrvn4");
        dropInRequest.amount(String.valueOf(amount));
        startActivityForResult(dropInRequest.getIntent(getContext()), PAYPAL_REQUEST_CODE);

    }

    public void getStaffType() {
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseCountryData> call = api.getStaff();

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseCountryData>() {
            @Override
            public void onResponse(Call<ResponseCountryData> call, Response<ResponseCountryData> response) {
                Log.d("COUN", String.valueOf(response.isSuccessful()));
                try {

                    str_staffTypeID="";
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

                    if (response.isSuccessful()) {
                        Log.d("COUN", String.valueOf(response.body()));
                        List<Country> list = response.body().getData();
                        staffType = new String[list.size()];
                        staffTypeID = new String[list.size()];

                        for (int i = 0; i < list.size(); i++) {
                            staffType[i] = list.get(i).getTitle();
                            staffTypeID[i] = String.valueOf(list.get(i).getId());
                            Log.e("COUN", list.get(i).getTitle());


                        }
                        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.text, staffType);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_staff_type.setAdapter(adapter);

                        str_staffTypeID=String.valueOf(list.get(0).getId());
                        getStaffMember(str_staffTypeID);

//                        if (!str_Country_id.isEmpty()) {
//                            for (int i = 0; i < list.size(); i++) {
//                                if (str_Country_id.equalsIgnoreCase(String.valueOf(list.get(i).getId()))) {
//                                    spinner.setSelection(i);
//                                    break;
//                                }
//                            }
//                        }

                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseCountryData> call, Throwable t) {
                //  Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getStaffMember(String id) {
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseMemberData> call = api.getStaffMember(id);

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseMemberData>() {
            @Override
            public void onResponse(Call<ResponseMemberData> call, Response<ResponseMemberData> response) {
                Log.d("COUN", String.valueOf(response.isSuccessful()));
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
                    strStaffMemberID="";

                    if (response.isSuccessful()) {
                        Log.d("COUN", String.valueOf(response.body()));
                        List<Members> list = response.body().getData();
                        staffMember = new String[list.size()];
                        staffMemberID = new String[list.size()];

                        for (int i = 0; i < list.size(); i++) {
                            staffMember[i] = list.get(i).getFirst_name()+" "+ list.get(i).getLast_name();
                            staffMemberID[i] = String.valueOf(list.get(i).getUser());
                            Log.e("COUN", list.get(i).getFirst_name()+" "+ list.get(i).getLast_name());
                        }
                        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.text, staffMember);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_member.setAdapter(adapter);

                        strStaffMemberID=String.valueOf(list.get(0).getUser());

//                        if (!str_Country_id.isEmpty()) {
//                            for (int i = 0; i < list.size(); i++) {
//                                if (str_Country_id.equalsIgnoreCase(String.valueOf(list.get(i).getId()))) {
//                                    spinner.setSelection(i);
//                                    break;
//                                }
//                            }
//                        }

                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseMemberData> call, Throwable t) {
                //  Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });

    }

//    public void makePAyment(int senderid,int beneficiaryid,Double amount,String paymentMethodNonce) {
//        /** Create handle for the RetrofitInstance interface*/
//        // Api api = RetrofitInstance.getRetrofitInstance().create(Api.class);
//        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
//
//        Api apiService = retrofit.create(Api.class);
//        /** Call the method with parameter in the interface to get the notice data*/
//        Call<ResponseTipCollgue> call = apiService.tipcollegue(ApiJsonMap(senderid,beneficiaryid,amount,paymentMethodNonce));
//
//        /**Log the URL called*/
//        Log.wtf("URL Called", call.request().url() + "");
//
//        call.enqueue(new Callback<ResponseTipCollgue>() {
//            @Override
//            public void onResponse(Call<ResponseTipCollgue> call, Response<ResponseTipCollgue> response) {
//
//                String errorbody = "", errorCode="";
//                try {
//                    if (response.errorBody() != null) {
//                        errorbody = response.errorBody().string();
//                        JSONObject responseObject = new JSONObject(errorbody);
//                        if (responseObject.has("message") && !responseObject.isNull("message")) {
//                            errorCode = responseObject.getString("message");
//                        }
//                        if (errorCode.equalsIgnoreCase("Unautherized access.")) {
//                            session.logoutUser();
//                            Toast.makeText(getContext(), "" + getResources().getString(R.string.session), Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//
//                        Toast.makeText(getContext(), "" +errorCode, Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    if (response.isSuccessful()) {
//                        if (response.body().getSuccess()) {
//                            Log.d("SUCCESS", String.valueOf(response.body().getMessage()));
////                            inputEditText.setText("");
//
//                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//
//                            // Creating user login session
//                            // For testing i am stroing name, email as follow
//                            // Use user real data
//
//                        } else {
//                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    } else {
//                        Toast.makeText(getContext(), "" +  response.body().getMessage(), Toast.LENGTH_SHORT).show();
//
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseTipCollgue> call, Throwable t) {
//                Log.d("ERRor",t.getMessage());
//                // Toast.makeText(getContext(), "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    private JsonObject ApiJsonMap(int senderid, int beneficiaryid, Double amount, String nounce) {


        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("sender", senderid);//userid
            jsonObj_.put("beneficiary", beneficiaryid);//userid
            jsonObj_.put("amount", 5.00);
            jsonObj_.put("nounce", nounce);
            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());

            //print parameter
            Log.e("MY gson.JSON:  ", "AS PARAMETER  " + gsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gsonObject;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                String paymentMethodNonce = Objects.requireNonNull(result.getPaymentMethodNonce()).getNonce();
                Log.e("nonce", "" + paymentMethodNonce);
                et_amount.setText("");

                makePAyment(user_id, Integer.parseInt(strStaffMemberID), amount,paymentMethodNonce);

                // send paymentMethodNonce to your server
            } else if (resultCode == RESULT_CANCELED) {
                // canceled
                Log.e("cancel", "cancel");
            } else {
                // an error occurred, checked the returned exception
                Exception exception = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.e("Exception", String.valueOf(exception));
            }
        }
    }

    public void makePAyment(int senderid,int beneficiaryid,Double amount,String paymentMethodNonce) {
        /** Create handle for the RetrofitInstance interface*/
        progressDialog.show();
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sender", senderid);//userid
            jsonObject.put("beneficiary", beneficiaryid);//userid
            jsonObject.put("amount", amount);
            jsonObject.put("nonce", paymentMethodNonce);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("ObjArray", jsonObject.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Api apiService = retrofit.create(Api.class);
        Call<ResponseBody> call = apiService.tipcollegue("tippayment/online", body);//socket url/chats
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
                        progressDialog.dismiss();
                        String respo = response.body().string();
                        JSONObject responseObject = new JSONObject(respo);
                        Log.e(" List", responseObject.toString());
                        String successstatus = responseObject.optString("success");
                        String message = responseObject.optString("message");
                        if (successstatus.equalsIgnoreCase("true")) {

                            Toast.makeText(getContext(), "" +message, Toast.LENGTH_SHORT).show();

                        }
                    }


                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d("ERRor", t.getMessage());
                //Toast.makeText(Login.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}