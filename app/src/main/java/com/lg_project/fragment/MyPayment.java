package com.lg_project.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.lg_project.activity.MainActivity2;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;
import com.lg_project.adapter.CustomAdapterPayment;
import com.lg_project.modelclass.ResponseMypayment.MyPaymentData;
import com.lg_project.modelclass.ResponseMypayment.ResponseMyPayment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MyPayment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<String> date,price,mode,user,contractor;
    CustomAdapterPayment customAdapter;
    LinearLayout back;
    SessionManageent session;
    ProgressBar progressBar;
    Sprite wave;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_my_payment, container, false);


        return v;
    }

    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);


        back=v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), MainActivity2.class);
                startActivity(intent);
                onDestroy();
            }
        });

        progressBar = v.findViewById(R.id.progress);
        wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        progressBar.setBackgroundResource(android.R.color.transparent);

        date=new ArrayList<>();
        price=new ArrayList<>();
        user=new ArrayList<>();
        contractor=new ArrayList<>();
        mode=new ArrayList<>();

        recyclerView=(RecyclerView)v.findViewById(R.id.recycle_payment);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter

        session = new SessionManageent(getContext());
        HashMap<String, String> user1 = session.getUserDetails();
        int id = Integer.parseInt(user1.get(SessionManageent.KEY_ID));
        Log.d("ERER", String.valueOf(id));

        mypayment(String.valueOf(id));


    }

    public void mypayment(String id)
    {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseMyPayment> call = api.payment(id);

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseMyPayment>() {
            @Override
            public void onResponse(Call<ResponseMyPayment> call, Response<ResponseMyPayment> response) {

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
                          //  Toast.makeText(getContext(), "" + getResources().getString(R.string.session), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(getContext(), "" +errorCode, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (response.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (response.body().getSuccess()) {
                            Log.d("SUCCESS", String.valueOf(response.body().getSuccess()));
                            List<MyPaymentData> list = response.body().getData();
                            for (int i = 0; i < list.size(); i++) {
                                date.add(list.get(i).getDate());
                                price.add(String.valueOf(list.get(i).getAmount()));
                                user.add(list.get(i).getSenderDesc());
                                contractor.add(list.get(i).getBeneficiaryDesc());
                                mode.add(list.get(i).getPaymentMode());
                            }

                            recyclerView.smoothScrollToPosition(price.size());
                            customAdapter = new CustomAdapterPayment(getContext(), date, price, user, contractor, mode);
                            recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView


                            // Creating user login session
                            // For testing i am stroing name, email as follow
                            // Use user real data
                            // List<Datum> list=response.body().getData();
                            // Log.d("IId", String.valueOf(list.get(0).getId()));
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
            public void onFailure(Call<ResponseMyPayment> call, Throwable t) {
                Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });
    }


}