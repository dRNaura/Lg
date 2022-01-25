package com.lg_project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

public class MyFavActivity extends AppCompatActivity {
    private String customer_id = "";
    CustomAdapter customAdapter;
    ImageView nodata;
    LinearLayout back;
    RecyclerView recyclerView;
    ArrayList<ContactorModel> arraylist = new ArrayList<>();
    private SessionManageent session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fav);


        session = new SessionManageent(MyFavActivity.this);
        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManageent.KEY_Credit);
        customer_id = user.get(SessionManageent.KEY_CUST_ID);
        Log.d("id", String.valueOf(customer_id));


        nodata = (ImageView) findViewById(R.id.nodata);
        back = findViewById(R.id.back);


        recyclerView = (RecyclerView) findViewById(R.id.fav_recycle);
        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MyFavActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        customAdapter = new CustomAdapter(MyFavActivity.this,arraylist, "fromFav");
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

                Intent intent = new Intent(MyFavActivity.this, MainActivity2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();


            }
        });
    }

    public void favList(String id) {
        /** Create handle for the RetrofitInstance interface*/
        Api api = RetrofitInstance.getRetrofitInstance().create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseAllContractor> call = api.favList(id);

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseAllContractor>() {
            @Override
            public void onResponse(Call<ResponseAllContractor> call, Response<ResponseAllContractor> response) {
                Log.d("COUN", String.valueOf(response.body().getMessage()));
                arraylist.clear();

                List<AllContractorData> list = response.body().getData();
                for (int i = 0; i < list.size(); i++) {
                    Log.d("RERERE", list.get(i).getStageName());
                    ContactorModel model=new ContactorModel();
                    model.first_name=list.get(i).getStageName();
                    model.profile_pic=list.get(i).getProfilePic();
                    model.cntrct_Age=list.get(i).getAge();
                    model.cntrct_Height=list.get(i).getHeight();
                    model.id=list.get(i).getId();
                    model.cntrct_Weight=list.get(i).getWeight();
                    model.cntrct_Stats=list.get(i).getStats();
                    if(list.get(i).getIsFavArtist() !=null){
                        model.cntrctFav=list.get(i).getIsFavArtist();
                    }

                    model.cntrctState=list.get(i).getState();
                    arraylist.add(model);
                }

                if (arraylist.isEmpty()) {
                    nodata.setVisibility(View.VISIBLE);
                } else {
                    nodata.setVisibility(View.GONE);
                }

                customAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ResponseAllContractor> call, Throwable t) {
                //  Log.d("ERRor",t.getMessage());
                Toast.makeText(MyFavActivity.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void deletefavItem(int favId) {
        /** Create handle for the RetrofitInstance interface*/

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customer_id", customer_id);
            jsonObject.put("contractor_id", favId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Api api = RetrofitInstance.getRetrofitInstance().create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<FavResponseData> call = api.favDelete(body);
        call.enqueue(new Callback<FavResponseData>() {
            @Override
            public void onResponse(Call<FavResponseData> call, Response<FavResponseData> response) {

                Log.e("SUCCESS", String.valueOf(response.body()));
                // Log.d("SUCCESSLOG", String.valueOf(response.body().getSuccess()));
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {

                        favList(customer_id);
//                        Toast.makeText(getContext(), "Success !!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MyFavActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    //  List<Datum> list = response.body().getData();
                   /* for (int i = 0; i < list.size(); i++) {
                        Log.d("TEST", list.get(i).getFirstName());
                    }*/
                } else {
                    Toast.makeText(MyFavActivity.this, "Something Wrong !!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<FavResponseData> call, Throwable t) {
                Log.d("ERRor", t.getMessage());
                //Toast.makeText(Login.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(MyFavActivity.this, MainActivity2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        finish();
    }
}