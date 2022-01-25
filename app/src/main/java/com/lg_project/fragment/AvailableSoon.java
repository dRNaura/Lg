package com.lg_project.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;
import com.lg_project.activity.MainActivity2;
import com.lg_project.adapter.CustomAdapter;
import com.lg_project.adapter.DilaogListAdapter;
import com.lg_project.modelclass.ContactorModel;
import com.lg_project.modelclass.FavResponseData;
import com.lg_project.modelclass.GetRaceBodyTypeList;
import com.lg_project.modelclass.all_contractor.AllContractorData;
import com.lg_project.modelclass.all_contractor.ResponseAllContractor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class AvailableSoon extends Fragment implements SearchView.OnQueryTextListener {
    private String customer_id = "",body="",race="",hair="", hairID="0";
    CustomAdapter customAdapter;
    SearchView editsearch;
    ImageView nodata,filter;
    List<AllContractorData> listt = new ArrayList<>();
    ArrayList<ContactorModel> arrayList = new ArrayList<>();
    ArrayList<GetRaceBodyTypeList> arrRaceList = new ArrayList<>();
    ArrayList<GetRaceBodyTypeList> arrBodyTypeList = new ArrayList<>();
    RecyclerView recyclerView;
    private SessionManageent session;
    TextView txt_race, txt_body_type,txt_hair;
    int body_TypeId = 01, raceID = 01, minAge=15,maxAge=70, minWeight =100,maxWeight=250;
    Double minHeight = 3.5,maxHeight=6.5;
    ArrayList<GetRaceBodyTypeList> arrHairColorList = new ArrayList<>();
    List<AllContractorData> data = new ArrayList<>();


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        MainActivity2.l1.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_available_soon, container, false);

        return v;
    }

    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        session = new SessionManageent(getContext());
        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManageent.KEY_Credit);
        customer_id = user.get(SessionManageent.KEY_CUST_ID);
        Log.d("id", String.valueOf(customer_id));

        nodata = (ImageView) v.findViewById(R.id.nodata);
        filter = (ImageView) v.findViewById(R.id.filter);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_available);
        // set a GridLayoutManager with default vertical orientation and 2 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        customAdapter = new CustomAdapter(getContext(), arrayList, "");
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView);


        editsearch = (SearchView) v.findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);
        editsearch.setFocusable(false);
        EditText searchEditText = (EditText) editsearch.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.textcolor));

//        AvailableData();
        getRaces();
        getBodyType();
        getHairColor();
        listeners();

    }


    public void listeners() {

        customAdapter.setOnAddClicked(new CustomAdapter.AddToFav() {
            @Override
            public void setOnAddItem(int position, int favId) {
                addItemToFav(favId);
            }
        });
        customAdapter.setOnDeleteClicked(new CustomAdapter.DeleteFav() {
            @Override
            public void setOnDeleteItem(int position, int favId) {
                deletefavItem(favId);
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterDIalog();
            }
        });

    }
    public void openFilterDIalog() {
        final Dialog optionDialog = new Dialog(getContext());
        optionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        optionDialog.setContentView(R.layout.filter_dialog_items);
        optionDialog.setCancelable(true);
        Window window = optionDialog.getWindow();
        window.setLayout(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        CrystalRangeSeekbar rangeSeekbar_Age = (CrystalRangeSeekbar) optionDialog.findViewById(R.id.rangeSeekbar_Age);
        CrystalRangeSeekbar rangeSeekbar_Height = (CrystalRangeSeekbar) optionDialog.findViewById(R.id.rangeSeekbar_Height);
        CrystalRangeSeekbar rangeSeekbar_Weight = (CrystalRangeSeekbar) optionDialog.findViewById(R.id.rangeSeekbar_Weight);

        TextView txt_filter_age = optionDialog.findViewById(R.id.txt_filter_age);
        TextView txt_filter_height = optionDialog.findViewById(R.id.txt_filter_height);
        TextView txt_filter_weight = optionDialog.findViewById(R.id.txt_filter_weight);
        txt_body_type = optionDialog.findViewById(R.id.txt_body_type);
        txt_race = optionDialog.findViewById(R.id.txt_race);
        txt_hair = optionDialog.findViewById(R.id.txt_hair);

        RelativeLayout rl_body_type = optionDialog.findViewById(R.id.rl_body_type);
        RelativeLayout rl_race = optionDialog.findViewById(R.id.rl_race);
        RelativeLayout rl_Hair = optionDialog.findViewById(R.id.rl_Hair);

//        SeekBar seekBar_Age = optionDialog.findViewById(R.id.seekBar_Age);
//        SeekBar seek_height = optionDialog.findViewById(R.id.seek_height);
//        SeekBar seek_weight = optionDialog.findViewById(R.id.seek_weight);
        Button btn_Submit = optionDialog.findViewById(R.id.btn_Submit);
        Button btn_filter_reset = optionDialog.findViewById(R.id.btn_filter_reset);
        ImageView img_cancel = optionDialog.findViewById(R.id.img_cancel);

//        seekBar_Age.setProgress(intAge);

//        minHeight= minHeight * 10;
//        maxHeight= maxHeight * 10;

        rangeSeekbar_Age.setMinStartValue(minAge).apply();
        rangeSeekbar_Height.setMinStartValue(Float.parseFloat(String.valueOf(minHeight))).apply();
        rangeSeekbar_Weight.setMinStartValue(minWeight).apply();

        rangeSeekbar_Age.setMaxStartValue(maxAge).apply();
        rangeSeekbar_Height.setMaxStartValue(Float.parseFloat(String.valueOf(maxHeight))).apply();
        rangeSeekbar_Weight.setMaxStartValue(maxWeight).apply();


        txt_body_type.setText(body);
        txt_race.setText(race);
        txt_hair.setText(hair);

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
                AvailableData();
            }
        });

        btn_filter_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                optionDialog.dismiss();
//                seekBar_Age.setProgress(1);
//                seekBar_Age.setMax(100);
//
//                seek_height.setProgress(40);
//                seek_height.setMax(70);
//
//                seek_weight.setProgress(40);
//                seek_weight.setMax(300);
                txt_body_type.setText("");
                txt_race.setText("");
                txt_hair.setText("");

//
                race = "";
                body = "";
                hair = "";
                hairID = "0";

                body_TypeId = 01;
                raceID = 01;
                minAge = 15;
                maxAge=70;
                minWeight = 100;
                maxWeight=250;
                minHeight = 3.5;
                maxHeight=6.5;

                AvailableData();
            }
        });

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDialog.dismiss();
                filter(minAge,maxAge,minWeight,maxWeight, minHeight, maxHeight);
            }
        });

        rl_body_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBodyTypeDialog();
            }
        });
        rl_race.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openRaceDialog();
            }
        });

        rl_Hair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openHairDialog();
            }
        });

        rangeSeekbar_Age.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {

                minAge = Integer.parseInt(String.valueOf(minValue));
                maxAge = Integer.parseInt(String.valueOf(maxValue));
                txt_filter_age.setText(minValue + " - " + maxValue + " yrs");
            }
        });

        rangeSeekbar_Height.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {

                double min = ((float) Double.parseDouble(String.valueOf(minValue)) / 10.0);
                Double prgrs = min;

                double max = ((float) Double.parseDouble(String.valueOf(maxValue)) / 10.0);

                Double prgrs2 = max;

                minHeight = Double.parseDouble(String.valueOf(prgrs));
                maxHeight = Double.parseDouble(String.valueOf(prgrs2));
                txt_filter_height.setText(prgrs + " - " + prgrs2 + " ft");
            }
        });

        rangeSeekbar_Weight.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minWeight = Integer.parseInt(String.valueOf(minValue));
                maxWeight = Integer.parseInt(String.valueOf(maxValue));
                txt_filter_weight.setText(minValue + " - " + maxValue + " lbs");
            }
        });


        optionDialog.show();

    }
    public void openBodyTypeDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.list_view);

        final ListView listView = dialog.findViewById(R.id.list_class);
//        ImageView img_cancel_dialog = dialog.findViewById(R.id.img_cancel_dialog);
        TextView dialog_title = dialog.findViewById(R.id.dialog_title);
        dialog_title.setText(getResources().getString(R.string.body_type));
//        dialog_title.setText("Select Body Type");

        Window window = dialog.getWindow();
        window.setLayout(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        DilaogListAdapter adapter = new DilaogListAdapter(getContext(), arrBodyTypeList);
        listView.setAdapter(adapter);

        adapter.setOnItemClicked(new DilaogListAdapter.OnClicked() {
            @Override
            public void setOnItem(int position) {
                body_TypeId = arrBodyTypeList.get(position).id;
                body=arrBodyTypeList.get(position).title;
                txt_body_type.setText(body);
                dialog.dismiss();


            }
        });
//        img_cancel_dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();

    }
    public void openRaceDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.list_view);

        final ListView listView = dialog.findViewById(R.id.list_class);
//        ImageView img_cancel_dialog = dialog.findViewById(R.id.img_cancel_dialog);
        TextView dialog_title = dialog.findViewById(R.id.dialog_title);
        dialog_title.setText(getResources().getString(R.string.race));
//        dialog_title.setText("Select Race");

        Window window = dialog.getWindow();
        window.setLayout(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        DilaogListAdapter adapter = new DilaogListAdapter(getContext(), arrRaceList);
        listView.setAdapter(adapter);

        adapter.setOnItemClicked(new DilaogListAdapter.OnClicked() {
            @Override
            public void setOnItem(int position) {
                raceID = arrRaceList.get(position).id;
                race=arrRaceList.get(position).title;
                txt_race.setText(race);
                dialog.dismiss();


            }
        });
//        img_cancel_dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();

    }

    public void openHairDialog() {

        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.list_view);

        final ListView listView = dialog.findViewById(R.id.list_class);
//        ImageView img_cancel_dialog = dialog.findViewById(R.id.img_cancel_dialog);
        TextView dialog_title = dialog.findViewById(R.id.dialog_title);
        dialog_title.setText(getResources().getString(R.string.hair));
//        dialog_title.setText("Select Hair");

        Window window = dialog.getWindow();
        window.setLayout(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        DilaogListAdapter adapter = new DilaogListAdapter(getContext(), arrHairColorList);
        listView.setAdapter(adapter);

        adapter.setOnItemClicked(new DilaogListAdapter.OnClicked() {
            @Override
            public void setOnItem(int position) {
                hairID = arrHairColorList.get(position).value;
                if(hairID.equalsIgnoreCase("Red")){
                    hairID="Reddish";
                }
                hair = arrHairColorList.get(position).title;
                txt_hair.setText(hair);
                dialog.dismiss();

            }
        });
//        img_cancel_dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();

    }






    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        filterName(text);
        //  customAdapter.filter(text);
        return false;
    }

    void filterName(String text) {
        ArrayList<ContactorModel> temp = new ArrayList<>();
        for (ContactorModel d : arrayList) {
            if (d.first_name.toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        if (temp.isEmpty()) {
            nodata.setVisibility(View.VISIBLE);
        }else{
            nodata.setVisibility(View.GONE);
        }


        //update recyclerview
        customAdapter.updateList(temp, "");
    }

    void filter(int minAge,int maxAge,int minWeight, int maxWeight,double minHeight, double maxHeight) {
        ArrayList<ContactorModel> temp = new ArrayList<>();
        for (ContactorModel d : arrayList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            Boolean Age = true;
            Boolean Weight = true;
            Boolean Height = true;
            Boolean Body_Type = true;
            Boolean Race = true;
            Boolean Haircolor = true;

            if(!((minAge == 15) && (maxAge == 70))) {
                if (!(minAge > 15 && maxAge < 70) && !(d.cntrct_Age > minAge && d.cntrct_Age < maxAge) ) {
                    Age = false;
                }
            }

            if(!((minHeight == 3.5) && (maxHeight == 6.10))) {
                if (!(minHeight > 3.5 && maxHeight < 6.10) &&
                        !(Double.parseDouble(d.cntrct_Height) > minHeight && Double.parseDouble(d.cntrct_Height) < maxHeight) ) {
                    Height = false;
                }
            }

            if(!((minWeight == 100) && (maxWeight == 250))) {
                if (!(minWeight > 100 && maxWeight < 150) && !(Integer.parseInt(d.cntrct_Weight) > minWeight &&
                        Integer.parseInt(d.cntrct_Weight) < maxWeight) ) {
                    Weight = false;
                }
            }

            if ((d.body_type != body_TypeId)) {
                Body_Type = false;
            }

            if ((d.race != raceID)) {
                Race = false;
            }

            if (!d.hair_color.equals(hair) && !hair.equals("")) {
                Haircolor = false;
            }

            if (Age && Height && Weight && Body_Type && Race && Haircolor)
            {
                temp.add(d);
            }



          /*  if (d.cntrct_Age > minAge && d.cntrct_Age < maxAge && Integer.parseInt(d.cntrct_Weight) > minWeight
                    && Integer.parseInt(d.cntrct_Weight) < maxWeight && Double.parseDouble(d.cntrct_Height) > minHeight
                    && Double.parseDouble(d.cntrct_Height) < maxHeight && d.body_type == body_TypeId && d.race == raceID
                    && d.hair_color.equalsIgnoreCase(hairID)) {
                temp.add(d);
            }*/


        }
        if (temp.isEmpty()) {
            nodata.setVisibility(View.VISIBLE);
        } else {
            nodata.setVisibility(View.GONE);
        }


        //update recyclerview
        customAdapter.updateList(temp, "");
    }



    public void AvailableData() {
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api apiService = retrofit.create(Api.class);
        Call<ResponseAllContractor> call = apiService.contractor(customer_id);
        call.enqueue(new Callback<ResponseAllContractor>() {
            @Override
            public void onResponse(Call<ResponseAllContractor> call, Response<ResponseAllContractor> response) {
                // Log.e("COUN", String.valueOf(response.body().getMessage()));
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

                    if(response.isSuccessful()) {
                        arrayList.clear();
                        data = response.body().getData();

                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i).getStatus().equals("Available_Soon")) {
                                Log.e("RERERE", String.valueOf(data.get(i).getBody_type()));
                                ContactorModel model = new ContactorModel();
                                model.first_name = data.get(i).getFirstName() + " " + data.get(i).getLastName();
                                model.profile_pic = data.get(i).getProfilePic();
                                model.cntrct_Age = data.get(i).getAge();
                                model.cntrct_Height = data.get(i).getHeight();
                                model.id = data.get(i).getId();
                                model.cntrct_Weight = data.get(i).getWeight();
                                model.cntrct_Stats = data.get(i).getStats();
                                model.cntrctFav = data.get(i).getIsFavArtist();
                                model.cntrctState = data.get(i).getState();
                                model.body_type_desc = data.get(i).getBody_type_desc();
                                model.stage_Name = data.get(i).getStageName();

                                if(data.get(i).getHair_color() !=null){
                                    model.hair_color = data.get(i).getHair_color();
                                }
                                if(data.get(i).getRace_desc() !=null){
                                    model.race_desc = data.get(i).getRace_desc();
                                }
                                if(data.get(i).getRace() !=null){
                                    model.race = data.get(i).getRace();
                                }
                                if(data.get(i).getBody_type() !=null){
                                    model.body_type = data.get(i).getBody_type();
                                }
                                // model.race = data.get(i).getRace();
                                arrayList.add(model);
                            }
                        }
                        if (arrayList.isEmpty()) {
                            Log.d("Names", "null");
                            nodata.setVisibility(View.VISIBLE);
                        } else {
                            nodata.setVisibility(View.GONE);
                        }


                        customAdapter.updateList(arrayList, "");
//                customAdapter.notifyDataSetChanged();
                    }
                    else
                    {

                        Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseAllContractor> call, Throwable t) {
                //  Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void addItemToFav(int favId) {
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
        Call<FavResponseData> call = api.favAdd(body);

        call.enqueue(new Callback<FavResponseData>() {
            @Override
            public void onResponse(Call<FavResponseData> call, Response<FavResponseData> response) {

                Log.e("SUCCESS", String.valueOf(response.body()));
                String errorbody = "", errorCode = "";
                try {
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
                            AvailableData();
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

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
            public void onFailure(Call<FavResponseData> call, Throwable t) {
                Log.d("ERRor", t.getMessage());
                //Toast.makeText(Login.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                String errorbody = "", errorCode = "";
                try {
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
                        if (response.body().getSuccess()) {

                            AvailableData();
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

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
    public void getRaces() {
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.getAllRaces();
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
                        arrRaceList.clear();
                        String respo = response.body().string();
                        JSONObject responseObject = new JSONObject(respo);
                        Log.e("addCat List", responseObject.toString());
                        String successstatus = responseObject.optString("success");

                        if (successstatus.equalsIgnoreCase("true")) {

                            JSONArray parent_categoriesOb = responseObject.getJSONArray("data");
                            for (int i = 0; i < parent_categoriesOb.length(); i++) {
                                JSONObject object = parent_categoriesOb.getJSONObject(i);
                                GetRaceBodyTypeList model = new GetRaceBodyTypeList();
                                model.id = object.getInt("id");
                                model.title = object.getString("title");
                                arrRaceList.add(model);
                            }

                        }

                    } else {

                        Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //  Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getBodyType() {
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.getBodyType();
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
                        arrBodyTypeList.clear();
                        String respo = response.body().string();
                        JSONObject responseObject = new JSONObject(respo);
                        Log.e("addCat List", responseObject.toString());
                        String successstatus = responseObject.optString("success");

                        if (successstatus.equalsIgnoreCase("true")) {

                            JSONArray parent_categoriesOb = responseObject.getJSONArray("data");
                            for (int i = 0; i < parent_categoriesOb.length(); i++) {
                                JSONObject object = parent_categoriesOb.getJSONObject(i);
                                GetRaceBodyTypeList model = new GetRaceBodyTypeList();
                                model.id = object.getInt("id");
                                model.title = object.getString("title");
                                arrBodyTypeList.add(model);
                            }

                        }

                    } else {

                        Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //  Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void getHairColor() {
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.getHairColor();
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
                        arrHairColorList.clear();
                        String respo = response.body().string();
                        JSONObject responseObject = new JSONObject(respo);
                        Log.e("hair List", responseObject.toString());
                        String successstatus = responseObject.optString("success");

                        if (successstatus.equalsIgnoreCase("true")) {

                            JSONArray parent_categoriesOb = responseObject.getJSONArray("data");
                            for (int i = 0; i < parent_categoriesOb.length(); i++) {
                                JSONObject object = parent_categoriesOb.getJSONObject(i);
                                GetRaceBodyTypeList model = new GetRaceBodyTypeList();
                                model.value = object.getString("value");
                                model.title = object.getString("title");
                                arrHairColorList.add(model);
                            }

                        }

                    } else {

                        Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //  Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.e("Exception", "inAVailableSoon");
            AvailableData();
        }else{
            // fragment is no longer visible
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}