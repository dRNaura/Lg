package com.lg_project.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lg_project.activity.MainActivity2;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;
import com.lg_project.adapter.CustomAdapterCreditPlan;
import com.lg_project.modelclass.creditplan.CreditPlanData;
import com.lg_project.modelclass.creditplan.ResponseCreditPlan;
import com.lg_project.modelclass.creditpurchase.CreditPurchaseData;
import com.lg_project.modelclass.creditpurchase.ResponseCreditPurchase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.lg_project.fragment.Details.PAYPAL_REQUEST_CODE;
//import static com.lg_project.fragment.Details.config;


public class BuyCredit extends Fragment {
    RecyclerView recyclerView;
    ArrayList<String> credit, dollar, planid, cost;
    CustomAdapterCreditPlan customAdapter;
    private ImageView back;
    SessionManageent session;
    String strPlanId = "";
    Double strCost=0.0;
    ProgressDialog progressDialog;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        MainActivity2.heading.setText(R.string.buy_credit);
        MainActivity2.l1.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buy_credit, container, false);

        return v;
    }

    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading.....");
        recyclerView = (RecyclerView) v.findViewById(R.id.recycle_buy_credit);
        credit = new ArrayList<>();
        dollar = new ArrayList<>();
        planid = new ArrayList<>();
        cost = new ArrayList<>();

        back = (ImageView) v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCredit men = new MyCredit();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_content, men);
                fragmentTransaction.commit();
                MainActivity2.drawerLayout.closeDrawers();
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        customAdapter = new CustomAdapterCreditPlan(getContext(), credit, dollar, planid, cost);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter


        menu();

        listeners();

    }

    public void listeners() {

        customAdapter.setOnBuyClicked(new CustomAdapterCreditPlan.OnBuyClicked() {
            @Override
            public void setOnBuy(int position) {
                strPlanId = planid.get(position);
                strCost = Double.valueOf(cost.get(position));

//                String[] separated = strCost.split("\\.");
//                strCost = Double.valueOf(separated[0]);

                onBraintreeSubmit(strCost);
//                processPayment(cost.get(position));
            }
        });
    }

    public void onBraintreeSubmit(Double amount) {
//sandbox_jyyn76fq_yhfkjhsf8fnbfj58
//sandbox_css89xw8_yhfkjhsf8fnbfj58
        //sandbox_mfqdhxgd_tqvvgtyd3qttrvn4
        DropInRequest dropInRequest = new DropInRequest().tokenizationKey("sandbox_mfqdhxgd_tqvvgtyd3qttrvn4");
        dropInRequest.amount(String.valueOf(amount));
        startActivityForResult(dropInRequest.getIntent(getContext()), PAYPAL_REQUEST_CODE);

    }

//    public void processPayment(String amount) {
//        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD",
//                "Buy Credit", PayPalPayment.PAYMENT_INTENT_SALE);
//        Intent intent = new Intent(getContext(), PaymentActivity.class);
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
//        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
//    }

    public void menu() {
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api apiService = retrofit.create(Api.class);
        Call<ResponseCreditPlan> call = apiService.plan();
        call.enqueue(new Callback<ResponseCreditPlan>() {
            @Override
            public void onResponse(Call<ResponseCreditPlan> call, Response<ResponseCreditPlan> response) {

                Log.d("SUCCESS", String.valueOf(new Gson().toJson(response)));
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

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            Log.d("SUCCESS", String.valueOf(response.body().getSuccess()));
                            List<CreditPlanData> list = response.body().getData();
                            for (int i = 0; i < list.size(); i++) {
                                credit.add(String.valueOf(list.get(i).getCredit()));
                                dollar.add(String.valueOf(list.get(i).getDollarPerCredit()));
                                planid.add(String.valueOf(list.get(i).getId()));
                                cost.add(String.valueOf(list.get(i).getCost()));
                            }

                            customAdapter.notifyDataSetChanged();


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
            public void onFailure(Call<ResponseCreditPlan> call, Throwable t) {
                Log.d("ERRor", t.getMessage());
                //Toast.makeText(Login.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void creditpurchase(String id, String planid, Double amount,String nonce) {
        progressDialog.show();
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api apiService = retrofit.create(Api.class);
        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseCreditPurchase> call = apiService.creditpurchase(ApiJsonMap(id, planid, amount,nonce));

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");
        call.enqueue(new Callback<ResponseCreditPurchase>() {
            @Override
            public void onResponse(Call<ResponseCreditPurchase> call, Response<ResponseCreditPurchase> response) {
                Log.d("RES", String.valueOf(response.body()));
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

                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        if (response.body().getMessage().equals("Operation sucessful")) {
                            Toast.makeText(getContext(), "" +response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            CreditPurchaseData purchaseData = response.body().getData();
                            Log.d("RES", String.valueOf(purchaseData.getCredit()));
                            HashMap<String, String> user = session.getUserDetails();
                            // email
                            int user_id = Integer.parseInt(user.get(SessionManageent.KEY_ID));
                            int cust_id = Integer.parseInt(user.get(SessionManageent.KEY_CUST_ID));
                            String email = user.get(SessionManageent.KEY_EMAIL);
                            String password = user.get(SessionManageent.KEY_PASSWORD);
                            String active_pin = user.get(SessionManageent.KEY_ACTIVE_PIN);
                            String firstName = user.get(SessionManageent.KEY_FIRSTNAME);
                            String phone = user.get(SessionManageent.KEY_PHONE);
                            String state = user.get(SessionManageent.KEY_STATE);
                            int age = Integer.parseInt(user.get(SessionManageent.KEY_AGE));
                            int country = Integer.parseInt(user.get(SessionManageent.KEY_COUNTRY));
                            int type = Integer.parseInt(user.get(SessionManageent.KEY_USER_TYPE));
                            String token = user.get(SessionManageent.KEY_TOKEN);

                            double credit = purchaseData.getCredit();

                            session.createLoginSession(firstName, password, email, user_id, String.valueOf(credit), cust_id, active_pin, type, token);
                            session.getInfoFromLoginSession(age, phone, country, state);

                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            MyCredit men = new MyCredit();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.home_content, men);
                            fragmentTransaction.commit();
                            MainActivity2.drawerLayout.closeDrawers();
                        } else {
                            Toast.makeText(getContext(), "" + getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseCreditPurchase> call, Throwable t) {
                Log.d("ERRor", t.getMessage());
                Toast.makeText(getContext(), "" + getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private JsonObject ApiJsonMap(String id, String planid, Double amount,String nonce) {

        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("id", id);
            jsonObj_.put("plan", planid);
            jsonObj_.put("amount", amount);
            jsonObj_.put("nonce", nonce);

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
    public void onDestroy() {
//        getContext().stopService(new Intent(getContext(), PayPalService.class));
        super.onDestroy();
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == PAYPAL_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//                if (confirmation != null) {
//                    try {
//                        session = new SessionManageent(getContext());
//                        HashMap<String, String> user = session.getUserDetails();
//                        // email
//                        int email = Integer.parseInt(user.get(SessionManageent.KEY_CUST_ID));
//                        Log.e("ERER", String.valueOf(email)+"\n"+strCost+"\n"+strPlanId);
//
//                        creditpurchase(String.valueOf(email),strPlanId,strCost);
//                        Toast.makeText(getContext(), ""+getResources().getString(R.string.success_transaction), Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                        Log.e("Exception", e.toString());
//                    }
//                } else if (resultCode == Activity.RESULT_CANCELED)
//                    Toast.makeText(getContext(), ""+getResources().getString(R.string.transaction_cancel), Toast.LENGTH_SHORT).show();
//            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
//                Toast.makeText(getContext(), ""+getResources().getString(R.string.transaction_invalid), Toast.LENGTH_SHORT).show();
//
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                String paymentMethodNonce = Objects.requireNonNull(result.getPaymentMethodNonce()).getNonce();
                Log.e("nonce", "" + paymentMethodNonce);

                session = new SessionManageent(getContext());
                HashMap<String, String> user = session.getUserDetails();

                int email = Integer.parseInt(user.get(SessionManageent.KEY_CUST_ID));
                Log.e("ERER", String.valueOf(email) + "\n" + strCost + "\n" + strPlanId);

                creditpurchase(String.valueOf(email), strPlanId, strCost,paymentMethodNonce);

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
}