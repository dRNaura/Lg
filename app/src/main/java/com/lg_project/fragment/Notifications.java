package com.lg_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lg_project.activity.MainActivity2;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;
import com.lg_project.adapter.CustomAdapterNotification;
import com.lg_project.modelclass.mynotification.MyNotificatinData;
import com.lg_project.modelclass.mynotification.ResponseMyNotification;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Notifications extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> notify,date;
     CustomAdapterNotification customAdapter;
     LinearLayout back;
     ImageView nodata;
     SessionManageent sessionManageent;

   /* @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_notifications, container, false);

        back=v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), MainActivity2.class);
                startActivity(intent);
                onDestroy();
            }
        });

        sessionManageent = new SessionManageent(getContext());
        HashMap<String, String> user = sessionManageent.getUserDetails();
        // email
        int userid = Integer.parseInt(user.get(SessionManageent.KEY_ID));

        recyclerView=(RecyclerView)v.findViewById(R.id.recycle_notification);
        notify=new ArrayList<>();
        date=new ArrayList<>();
        nodata=(ImageView) v.findViewById(R.id.nodata);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter


        Log.d("USERIDDD", String.valueOf(userid));
        notification(String.valueOf(userid));
        return v;
    }


    public void notification(String id) {
        /** Create handle for the RetrofitInstance interface*/
        // Api api = RetrofitInstance.getRetrofitInstance().create(Api.class);
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api apiService = retrofit.create(Api.class);
        Call<ResponseMyNotification> call = apiService.notifications(id);
        Log.d("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseMyNotification>() {
            @Override
            public void onResponse(Call<ResponseMyNotification> call, Response<ResponseMyNotification> response) {

                try {
                    String errorbody = "", errorCode = "";
                    if (response.errorBody() != null) {
                        errorbody = response.errorBody().string();
                        JSONObject responseObject = new JSONObject(errorbody);
                        if (responseObject.has("message") && !responseObject.isNull("message")) {
                            errorCode = responseObject.getString("message");
                        }
                        if (errorCode.equalsIgnoreCase("Unautherized access.")) {
                            sessionManageent.logoutUser();
                            Toast.makeText(getContext(), "" + getResources().getString(R.string.session), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(getContext(), "" +errorCode, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            Log.d("SUCCESS", String.valueOf(response.body().getMessage()));
                            List<MyNotificatinData> notificatinData = response.body().getData();
                            for (int i = 0; i < notificatinData.size(); i++) {
                                notify.add(notificatinData.get(i).getMsg());
                                date.add(notificatinData.get(i).getDate());
                            }

                            if (notify.size() == 0) {
                                nodata.setVisibility(View.VISIBLE);
                            } else {
                                nodata.setVisibility(View.GONE);
                                customAdapter = new CustomAdapterNotification(getContext(), notify, date);
                                recyclerView.setAdapter(customAdapter);
                            }


                        } else {
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getContext(), "" + getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseMyNotification> call, Throwable t) {
                Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });
    }

}