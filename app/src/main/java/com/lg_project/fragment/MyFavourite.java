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
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lg_project.activity.MainActivity2;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;
import com.lg_project.adapter.CustomAdapter;
import com.lg_project.modelclass.ContactorModel;
import com.lg_project.modelclass.FavResponseData;
import com.lg_project.modelclass.all_contractor.AllContractorData;
import com.lg_project.modelclass.all_contractor.ResponseAllContractor;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MyFavourite extends Fragment {

    private String customer_id = "";
    CustomAdapter customAdapter;
    ImageView nodata;
    LinearLayout back;
    RecyclerView recyclerView;
    ArrayList<ContactorModel> arraylist = new ArrayList<>();
    private SessionManageent session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_favourite, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        MainActivity2.heading.setText(R.string.myfav);

        MainActivity2.l1.setVisibility(View.VISIBLE);
    }

    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        session = new SessionManageent(getContext());
        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManageent.KEY_Credit);
        customer_id = user.get(SessionManageent.KEY_CUST_ID);
        Log.d("id", String.valueOf(customer_id));


        nodata = (ImageView) v.findViewById(R.id.nodata);
        back = v.findViewById(R.id.back);


        recyclerView = (RecyclerView) v.findViewById(R.id.fav_recycle);
        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        customAdapter = new CustomAdapter(getContext(),arraylist, "fromFav");
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView

        favList(customer_id);


        listenes();
    }

    public void listenes() {

        customAdapter.setOnDeleteClicked(new CustomAdapter.DeleteFav() {
            @Override
            public void setOnDeleteItem(int position, int favId) {

                deletefavItem(favId);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity2.class);
                startActivity(intent);
                onDestroy();
            }
        });
    }

    public void favList(String id) {
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);
        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseAllContractor> call = api.favList(id);

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseAllContractor>() {
            @Override
            public void onResponse(Call<ResponseAllContractor> call, Response<ResponseAllContractor> response) {
                Log.d("COUN", String.valueOf(response.body().getMessage()));
                arraylist.clear();

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

                    List<AllContractorData> list = response.body().getData();
                    for (int i = 0; i < list.size(); i++) {
                        Log.d("RERERE", list.get(i).getStageName());
                        ContactorModel model = new ContactorModel();
                        model.first_name = list.get(i).getFirstName() + " " + list.get(i).getLastName();
                        model.stage_Name = list.get(i).getStageName();
                        model.profile_pic = list.get(i).getProfilePic();
                        model.cntrct_Age = list.get(i).getAge();
                        model.cntrct_Height = list.get(i).getHeight();
                        model.id = list.get(i).getId();
                        model.cntrct_Weight = list.get(i).getWeight();
                        model.cntrct_Stats = list.get(i).getStats();
                        if (list.get(i).getIsFavArtist() != null) {
                            model.cntrctFav = list.get(i).getIsFavArtist();
                        }

                        model.cntrctState = list.get(i).getState();
                        model.body_type_desc = list.get(i).getBody_type_desc();
                        model.hair_color = list.get(i).getHair_color();
                        model.race_desc = list.get(i).getRace_desc();
                        arraylist.add(model);
                    }

                    if (arraylist.isEmpty()) {
                        nodata.setVisibility(View.VISIBLE);
                    } else {
                        nodata.setVisibility(View.GONE);
                    }

                    customAdapter.notifyDataSetChanged();

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseAllContractor> call, Throwable t) {
                //  Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void deletefavItem(int favId) {
        /** Create handle for the RetrofitInstance interface*/
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", customer_id);
            jsonObject.put("contractor_id", favId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Api api = retrofit.create(Api.class);
        /** Call the method with parameter in the interface to get the notice data*/
        Call<FavResponseData> call = api.favDelete(body);
        call.enqueue(new Callback<FavResponseData>() {
            @Override
            public void onResponse(Call<FavResponseData> call, Response<FavResponseData> response) {

                Log.e("SUCCESS", String.valueOf(response.body()));
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
                            Toast.makeText(getContext(), "" + getResources().getString(R.string.session), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(getContext(), "" +errorCode, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {

                            favList(customer_id);
//                        Toast.makeText(getContext(), "Success !!", Toast.LENGTH_SHORT).show();

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
            public void onFailure(Call<FavResponseData> call, Throwable t) {
                Log.d("ERRor", t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private JsonObject ApiJsonMap(String customer_id, String favId) {

        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("customer_id", customer_id);
            jsonObj_.put("contractor_id", favId);


            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gsonObject;
    }

}