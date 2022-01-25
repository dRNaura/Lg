package com.lg_project.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lg_project.activity.MainActivity;
import com.lg_project.activity.MainActivity2;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;
import com.lg_project.adapter.CustomAdapterImages;
import com.lg_project.modelclass.city.CityData;
import com.lg_project.modelclass.city.ResponseCity;
import com.lg_project.modelclass.contractordetails.ArtistPic;
import com.lg_project.modelclass.country.Country;
import com.lg_project.modelclass.country.ResponseCountryData;
import com.lg_project.modelclass.editprofile.ResponseData;
import com.lg_project.modelclass.editprofile.ResponseEditProfile;
import com.lg_project.modelclass.state.ResponseState;
import com.lg_project.modelclass.state.StateData;
import com.lg_project.modelclass.updatepic.ResponseUpdatePic;
import com.lg_project.modelclass.updatepic.Updatepicdata;
import com.lg_project.modelclass.uploadimage.ResponseUploadImage;
import com.lg_project.modelclass.uploadimage.UploadArtistPic;
import com.lg_project.modelclass.uploadimage.UploadImageData;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;


public class EditProfile extends Fragment {

    TextView cancel_text;
    LinearLayout back;
    Button btn_Save;
    ArrayList<String> country = new ArrayList<>();
    ArrayList<String> countryId = new ArrayList<>();
    ArrayList<String> countrycode = new ArrayList<>();

    ArrayList<String> statelist = new ArrayList<>();
    ArrayList<String> stateid = new ArrayList<>();

    ArrayList<String> citylist = new ArrayList<>();
    ArrayList<String> cityid = new ArrayList<>();

    List<ArtistPic> data=new ArrayList<>();
    JSONObject jsonObject=new JSONObject();
    JSONArray jsonArray = new JSONArray();
    public static boolean isupdate = false;

    String str_Country_id="",id="",email="",firstName="",active_pin="",password="",last_name="",phone="",state="",age="",Country="",
    State="",City="",Height="",Weight="",str_state_id="",str_city_id="",str_countrycode="",str_height="",
    str_weight="";
    String hide="Active";
    SwitchCompat switchCompat;

    String save_firstname="",save_lastname="",save_email="",save_country="",save_age="",save_height="",save_weight="",
    save_state="",save_city="",save_password="",save_phone="",save_status="",save_id="",save_activepin="";

    Spinner spinner,spinner_state,spinner_city,spinner_height,spinner_weight;
    SessionManageent session;
    EditText et_first_name,et_last_name,et_profile_email,et_profile_age,et_profile_password,et_profile_phone;

    CircularImageView image;
    ImageView camera;
    AlertDialog alertDialog;
    String mediaPath="";
    RecyclerView recyclerView;
    private CustomAdapterImages customAdapter;
    private Uri selectedImage;

    List<String> images = new ArrayList<>();
    List<String> image_id=new ArrayList<>();
    List<String> image_status=new ArrayList<>();

    List<ResponseData> list=new ArrayList<>();

    ArrayList<String> arrHeightList=new ArrayList<>();
    ArrayList<String> arrWeightList=new ArrayList<>();
    private File file;
    private String str_state="",str_city="";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        MainActivity2.heading.setText(R.string.edit_profile);
        MainActivity2.l1.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_edit_profile, container, false);
        session = new SessionManageent(getContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        id = user.get(SessionManageent.KEY_CUST_ID);
       // firstName = user.get(SessionManageent.KEY_FIRSTNAME);
        active_pin = user.get(SessionManageent.KEY_ACTIVE_PIN);
        password = user.get(SessionManageent.KEY_PASSWORD);
       // age = user.get(SessionManageent.KEY_AGE);
        str_Country_id = user.get(SessionManageent.KEY_COUNTRY);
       // state = user.get(SessionManageent.KEY_STATE);
       // email = user.get(SessionManageent.KEY_EMAIL);
       // phone = user.get(SessionManageent.KEY_PHONE);
        Log.e("id", String.valueOf(id));

        getHeight();
        getweight();


        fetchIds(v);





        return v;
    }
  /*  public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);



    }*/


    public void getHeight(){
        arrHeightList.clear();
        arrHeightList.add("Select Height");
        arrHeightList.add("4'8");
        arrHeightList.add("4'9");
        arrHeightList.add("4'11");
        arrHeightList.add("5'1");
        arrHeightList.add("5'2");
        arrHeightList.add("5'3");
        arrHeightList.add("5'4");
        arrHeightList.add("5'5");
        arrHeightList.add("5'6");
        arrHeightList.add("5'7");
        arrHeightList.add("5'8");
        arrHeightList.add("5'9");
        arrHeightList.add("5'10");
        arrHeightList.add("6'1");
        arrHeightList.add("6'2");
        arrHeightList.add("6'3");
        arrHeightList.add("6'4");
        arrHeightList.add("6'5");
        arrHeightList.add("6'6");
        arrHeightList.add("6'7");
        arrHeightList.add("6'8");
        arrHeightList.add("6'9");
        arrHeightList.add("6'11");
        arrHeightList.add("7'1");
        arrHeightList.add("7'2");
        arrHeightList.add("7'3");
        arrHeightList.add("7'4");
        arrHeightList.add("7'5");
        arrHeightList.add("7'6");
        arrHeightList.add("7'7");
        arrHeightList.add("7'8");
        arrHeightList.add("7'9");
        arrHeightList.add("7'11");
        arrHeightList.add("8'0");

    }

    public void getweight()
    {
        arrWeightList.clear();
        arrWeightList.add("Select Weight");
        arrWeightList.add("60");
        arrWeightList.add("70");
        arrWeightList.add("80");
        arrWeightList.add("90");
        arrWeightList.add("100");
        arrWeightList.add("110");
        arrWeightList.add("120");
        arrWeightList.add("130");
        arrWeightList.add("140");
        arrWeightList.add("150");
        arrWeightList.add("160");
        arrWeightList.add("170");
        arrWeightList.add("180");
        arrWeightList.add("190");
        arrWeightList.add("200");
        arrWeightList.add("210");
        arrWeightList.add("220");
        arrWeightList.add("230");
        arrWeightList.add("240");
        arrWeightList.add("250");
        arrWeightList.add("260");
        arrWeightList.add("270");
        arrWeightList.add("280");
        arrWeightList.add("290");
        arrWeightList.add("300");
    }







    public void fetchIds(View v){
        cancel_text=v.findViewById(R.id.cancel_text);
        back=v.findViewById(R.id.back);
        btn_Save=v.findViewById(R.id.btn_Save);
        spinner=v.findViewById(R.id.spinner);
        spinner_state=v.findViewById(R.id.spinner_state);
        spinner_city=v.findViewById(R.id.spinner_city);
        spinner_height=v.findViewById(R.id.spinner_height);
        spinner_weight=v.findViewById(R.id.spinner_weight);
        et_first_name=v.findViewById(R.id.et_first_name);
        et_last_name=v.findViewById(R.id.et_last_name);
        et_profile_email=v.findViewById(R.id.et_profile_email);
        et_profile_age=v.findViewById(R.id.et_profile_age);
        et_profile_password=v.findViewById(R.id.et_profile_password);
        et_profile_phone=v.findViewById(R.id.et_profile_phone);

       /* String str =firstName;
        String[] splited = str.split("\\s+");
        firstName= splited[0];
        last_name=splited[1];*/

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-action-local-broadcast"));


        customAdapter = new CustomAdapterImages(getContext(),images,image_id,image_status);

        profile(String.valueOf(id));

        et_first_name.setText(firstName);
        et_last_name.setText(last_name);
        et_profile_age.setText(age);
        et_profile_password.setText(password);
        et_profile_phone.setText(phone);
        et_profile_email.setText(email);


        switchCompat=(SwitchCompat)v.findViewById(R.id.hide_profile);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                HashMap<String, String> user = session.getUserDetails();
                String status = user.get(session.KEY_STATE);
                Log.e("status", status);
                    if(isChecked)
                    {
                        hide="Hide";
                    }
                    else
                    {
                        hide="Active";
                    }


            }
        });







        image=(CircularImageView)v.findViewById(R.id.image);
        camera=(ImageView)v.findViewById(R.id.camera);



        listeners();
    }
    public void listeners(){

        cancel_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity2.class);
                startActivity(intent);
                onDestroy();
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
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save_id = id;
                save_activepin = active_pin;
                save_firstname = et_first_name.getText().toString();
                save_lastname = et_last_name.getText().toString();
                save_email = et_profile_email.getText().toString();
                save_country = str_Country_id;
                save_age = et_profile_age.getText().toString();
                save_height = str_height;
                save_weight = str_weight;
                save_state = str_state_id;
                save_city = str_city;
                save_password = et_profile_password.getText().toString();
                save_phone = et_profile_phone.getText().toString();
                save_status = hide;


                if(TextUtils.isEmpty(save_firstname)
                        || TextUtils.isEmpty(save_lastname)
                        || TextUtils.isEmpty(save_email)
                        || TextUtils.isEmpty(save_country)
                        || TextUtils.isEmpty(save_age)
                        || TextUtils.isEmpty(save_height)
                        || TextUtils.isEmpty(save_weight)
                        || TextUtils.isEmpty(save_state)
                        || TextUtils.isEmpty(save_city)
                        || TextUtils.isEmpty(save_password)
                        || TextUtils.isEmpty(save_phone)
                ){
                    Toast.makeText(getContext(), ""+getResources().getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();

                }else
                {
                    editProfile(save_id,save_activepin,save_firstname,save_lastname,save_email,save_country,save_age,save_height,save_weight,
                            save_state,save_city,save_password,save_phone,save_status);
                }

            }
        });
       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               //Log.e("ID",countryId[position]);
               str_Country_id=countryId.get(position);
               str_countrycode = countrycode.get(position);
               getState(countrycode.get(position));
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.e("ID",countryId[position]);
                str_state_id=stateid.get(position);
                str_state = statelist.get(position);
                getCity(str_countrycode,str_state_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.e("ID",countryId[position]);
                str_city_id=citylist.get(position);
                str_city = citylist.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_height.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.e("ID",countryId[position]);
                str_height=arrHeightList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.e("ID",countryId[position]);
                str_weight=arrWeightList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //swt alert
              /* // new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                     //   .setCustomView(dialogView)
                      *//*  .setTitleText("Are you sure?")
                        .setContentText("You won't be able to recover this file!")
                        .setConfirmText("Delete!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })*//*
                        .show();*/





                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.picACTION_IMAGE_CAPTURE_alert, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(false);


                ImageView image = (ImageView) dialogView.findViewById(R.id.add_pic);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                        intent.setType("*/*");
//                        intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//                        try {
//                            startActivityForResult(
//                                    Intent.createChooser(intent, "Select a File to Upload"),
//                                    FILE_SELECT_CODE);
//                        } catch (android.content.ActivityNotFoundException ex) {
//                            // Potentially direct the user to the Market with a Dialog
//                            Toast.makeText(getContext(), "Please install a File Manager.",Toast.LENGTH_SHORT).show();
//                        }
                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
                            if (!Environment.isExternalStorageManager()) {
                                try {
                                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                    intent.addCategory("android.intent.category.DEFAULT");
                                    intent.setData(Uri.parse(String.format("package:%s", getContext().getApplicationContext().getPackageName())));
                                    startActivityForResult(intent, 100);
                                } catch (Exception e) {

                                    Intent intent = new Intent();
                                    intent.setAction(android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                    startActivityForResult(intent, 100);
                                }
                            } else {
                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, 101);
                            }
                        }
                        else {

                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, 101);
                        }
                    }
                });
                ImageView close = (ImageView) dialogView.findViewById(R.id.close_alert);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                Button b1=(Button)dialogView.findViewById(R.id.submit);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mediaPath.equals(""))
                        {
                            // Toast.makeText(getContext(), ""+getResources().getString(R.string.all_files_updated), Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            //alert(getResources().getString(R.string.all_files_updated));
                        }
                        else {
                                uploadFile(selectedImage, String.valueOf(id));
                        }
                    }
                });

                recyclerView=(RecyclerView)dialogView.findViewById(R.id.recycler_pics);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),4);
                recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
                //  call the constructor of CustomAdapter to send the reference and data to Adapter
                customAdapter = new CustomAdapterImages(getContext(),images,image_id,image_status);
                recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView

                alertDialog = dialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
            }
        });




    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("name");
            if(message.equals("success"))
            {
                HashMap<String, String> user = session.getUserDetails();


                // email
                id = user.get(SessionManageent.KEY_CUST_ID);
                Log.d("ERER", String.valueOf(id));
                images.clear();
                image_id.clear();
                image_status.clear();
                profile(String.valueOf(id));
                customAdapter.notifyDataSetChanged();
            }
        }
    };



    public void Country() {
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseCountryData> call = api.country();

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseCountryData>() {
            @Override
            public void onResponse(Call<ResponseCountryData> call, Response<ResponseCountryData> response) {
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

                    if (response.isSuccessful()) {
                        Log.d("COUN", String.valueOf(response.body()));
                        List<Country> list = response.body().getData();
                        country.clear();
                        countryId.clear();
                        countrycode.clear();

                        for (int i = 0; i < list.size(); i++) {
                            country.add(list.get(i).getTitle());
                            countryId.add(String.valueOf(list.get(i).getId()));
                            countrycode.add(list.get(i).getCountry());
                            Log.d("COUN", list.get(i).getTitle());
                        }
                        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.text, country);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                        if (!str_Country_id.isEmpty()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (str_Country_id.equalsIgnoreCase(String.valueOf(list.get(i).getId()))) {
                                    spinner.setSelection(i);
                                    getState(String.valueOf(list.get(i).getCountry()));
                                    break;
                                }
                            }
                        }

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

    public void profile(String id) {
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseEditProfile> call = api.customerdetail(id);

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseEditProfile>() {
            @Override
            public void onResponse(Call<ResponseEditProfile> call, Response<ResponseEditProfile> response) {
                Log.d("Edit", String.valueOf(response.isSuccessful()));
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

                        ResponseData pro = response.body().getData();
                        data = pro.getCustomerPic();

                        firstName = pro.getFirstName();
                        et_first_name.setText(firstName);

                        last_name = pro.getLastName();
                        et_last_name.setText(last_name);

                        email = pro.getUsername();
                        et_profile_email.setText(email);

                        age = String.valueOf(pro.getAge());
                        et_profile_age.setText(age);

                        phone = pro.getPhone();
                        et_profile_phone.setText(phone);

                        Height = String.valueOf(pro.getHeight());
                        if(Height.equals("null"))
                        {
                            str_height="";
                        }
                        else
                        {
                            str_height = Height;
                        }

                        Weight = pro.getWeight();
                        if(Weight == null)
                        {
                            str_weight="";
                        }
                        else
                        {
                            str_weight = Weight;
                        }


                        Country = String.valueOf(pro.getCountry());
                        if(Country == null)
                        {
                            str_Country_id="";
                        }
                        else
                        {
                            str_Country_id = Country;
                        }

                        State = pro.getProvince();
                        if(State == null)
                        {
                            str_state="";
                        }
                        else
                        {
                            str_state = State;
                        }

                        City = pro.getCity();
                        if(City == null)
                        {
                            str_city="";
                        }
                        else
                        {
                            str_city = City;
                        }

                        hide = pro.getState();

                        if(hide != null) {
                            if (hide.equalsIgnoreCase("Active")) {
                                switchCompat.setChecked(false);
                            } else {
                                switchCompat.setChecked(true);
                            }
                        }
                        else
                        {
                            switchCompat.setChecked(false);
                        }

                        String profilepic="";

                        for (int i = 0; i < data.size(); i++) {
                            images.add(data.get(i).getPath());
                            image_id.add(String.valueOf(data.get(i).getId()));
                            image_status.add(String.valueOf(data.get(i).getActive()));
                            if(data.get(i).getProfile() == 1)
                            {
                                profilepic = data.get(i).getPath();
                            }
                        }

                        customAdapter.notifyDataSetChanged();

                        Log.d("TRE",profilepic);

                        Glide.with(getContext()).load(profilepic).placeholder(R.drawable.lg_logo)
                                .into(image);

                        Country();
                        heightweight();

                    }
                } catch (Exception e) {

                    Log.d("EXXXX",e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseEditProfile> call, Throwable t) {
                //  Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void getState(String state) {
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseState> call = api.state(state);

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseState>() {
            @Override
            public void onResponse(Call<ResponseState> call, Response<ResponseState> response) {
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

                    if (response.isSuccessful()) {
                        Log.d("COUN", String.valueOf(response.body()));
                        List<StateData> list = response.body().getData();
                        statelist.clear();
                        stateid.clear();

                        for (int i = 0; i < list.size(); i++) {
                            statelist.add(list.get(i).getState());
                            stateid.add(list.get(i).getStateCode());
                          //  Log.d("COUN", list.get(i).getTitle());
                        }
                        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.text, statelist);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_state.setAdapter(adapter);

                        if (!str_state.isEmpty()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (str_state.equalsIgnoreCase(list.get(i).getState())) {
                                    spinner_state.setSelection(i);
                                   // getCity(coun,str_city_id);
                                    break;
                                }
                            }
                        }

                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseState> call, Throwable t) {
                //  Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getCity(String stateid,String city_id) {
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseCity> call = api.city(stateid,city_id);

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseCity>() {
            @Override
            public void onResponse(Call<ResponseCity> call, Response<ResponseCity> response) {
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

                    if (response.isSuccessful()) {
                        Log.d("COUN", String.valueOf(response.body()));
                        List<CityData> list = response.body().getData();
                        citylist.clear();
                        cityid.clear();

                        for (int i = 0; i < list.size(); i++) {
                            citylist.add(list.get(i).getCity());
                        }
                        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.text, citylist);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_city.setAdapter(adapter);

                        if (!str_city.isEmpty()) {
                            for (int i = 0; i < list.size(); i++) {
                                if (str_city.equalsIgnoreCase(list.get(i).getCity())) {
                                    spinner_city.setSelection(i);
                                    break;
                                }
                            }
                        }

                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseCity> call, Throwable t) {
                //  Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void editProfile(String st_save_id, String st_save_activepin,
                            String st_save_firstname, String st_save_lastname, String st_save_email, String st_save_country,
                            String st_save_age, String st_save_height, String st_save_weight,
                            String st_save_state, String st_save_city, String st_save_password, String st_save_phone,
                            String st_save_status) {

        String height = st_save_height.replaceAll("\\'"  ,  ".");
        Gson gson = new Gson();
        String arrayData = gson.toJson(data);
        Log.d("DRE",arrayData);

        for(int i=0;i<data.size();i++)
        {
            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("id",data.get(i).getId());
                jsonObject.put("path",data.get(i).getPath());
                jsonObject.put("profile",data.get(i).getProfile());
                jsonObject.put("active",data.get(i).getActive());

                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        Retrofit retrofit = RetrofitInstance.getLocale(getContext());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",Integer.parseInt(st_save_id));
            jsonObject.put("first_name", st_save_firstname);
            jsonObject.put("last_name", st_save_lastname);
            jsonObject.put("age", Integer.parseInt(st_save_age));//int
            jsonObject.put("country", Integer.parseInt(st_save_country));
            jsonObject.put("phone", st_save_phone);
            jsonObject.put("active_pin", st_save_activepin);
            jsonObject.put("height", height);
            jsonObject.put("weight", st_save_weight);
            jsonObject.put("city", st_save_city);
            jsonObject.put("province", st_save_state);
            jsonObject.put("customer_pic", jsonArray);
            jsonObject.put("state", st_save_status);
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
                            session.getInfoFromLoginSession(age,phone,country,state);
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

    public void alert(String msg)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder
                // .setMessage(Html.fromHtml("<p style='text-align:center;'>Please Fill the required details</p><h3 style='text-align:center;'>Click Yes to Exit !</h4>"))
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                            }
                        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // switch (requestCode) {
           /* case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path
                    String path = getImageFilePath(uri);
                   // Bitmap img=MediaStore.Images.Media.getBitmap(getContentResolver, filePath);

                        Log.d(TAG, "File Path: " + path);
                        images.add(path);
                        customAdapter.notifyDataSetChanged();

                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;*/
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            //the image URI
            // Get the Image from data
            selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mediaPath = cursor.getString(columnIndex);




            //selectedImage = data.getData();
            //  images.add(getRealPathFromURI(selectedImage));
            images.add(mediaPath);
            customAdapter.notifyDataSetChanged();


        }
        // }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void uploadFile(Uri fileUri,String idd) {
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.show();
        //creating a file
        // File file = new File(getRealPathFromURI(fileUri));
        if(mediaPath.equals(""))
        {
          /*  Toast.makeText(getContext(), ""+getResources().getString(R.string.all_files_updated),
                    Toast.LENGTH_SHORT).show();*/
        }
        else {
            file = new File(mediaPath);
            Log.d("FIle", String.valueOf(file));
        }

        Context applicationContext = MainActivity2.getContextOfApplication();
        RequestBody requestFile = RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(fileUri)), file);
        //  RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        //   RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);// MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("images", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description = RequestBody.create(okhttp3.MultipartBody.FORM, idd);

        // Api api = RetrofitInstance.getRetrofitInstance().create(Api.class);
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api apiService = retrofit.create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        //   Call<ResponseContractorDetail> call = api.detail(id);

        /**Log the URL called*/


        //creating a call and calling the upload image method
        Call<ResponseUploadImage> call = apiService.uploadImage(body,description);
        Log.wtf("URL Called Upload", call.request().url() + "");

        //finally performing the call
        call.enqueue(new Callback<ResponseUploadImage>() {
            @Override
            public void onResponse(Call<ResponseUploadImage> call, Response<ResponseUploadImage> response) {
                progressDialog.dismiss();
                Log.d("RESER",String.valueOf(response.body()));
                String errorbody = "", errorCode="";
                Log.d("SWEWSE", String.valueOf(response.isSuccessful()));
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
                        progressDialog.dismiss();
                        UploadImageData uploadImages = response.body().getData();
                        List<UploadArtistPic> pic = uploadImages.getArtistPic();
                        if(pic != null) {
                            for (int i = 0; i < pic.size(); i++) {
                                Log.d("PATHHHUpload", pic.get(i).getPath());
                                try {
                                    jsonObject.put("id", "");
                                    jsonObject.put("path", pic.get(i).getPath());
                                    jsonObject.put("profile", pic.get(i).getProfile());
                                    jsonObject.put("active", pic.get(i).getActive());

                                    jsonArray.put(jsonObject);

                                    Log.d("PATHHHUploadTREE", jsonObject.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }

                        updatepic();
                        // Toast.makeText(getContext(), "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "" + getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_LONG).show();
                    }
                    //  Log.d("test",response.body().getMessage());
                } catch (Exception e) {
                    Log.d("EXC",e.getMessage());

                }
            }

            @Override
            public void onFailure(Call<ResponseUploadImage> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("UPLOAd",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updatepic()
    {
        /** Create handle for the RetrofitInstance interface*/
        //  Api api = RetrofitInstance.getRetrofitInstance().create(Api.class);
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api apiService = retrofit.create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseUpdatePic> call = apiService.uploadprofile(ApiJsonMap());

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseUpdatePic>() {
            @Override
            public void onResponse(Call<ResponseUpdatePic> call, Response<ResponseUpdatePic> response) {

                String errorbody = "", errorCode="";
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
                            Log.d("SUCCESS", String.valueOf(response.body().getData()));
                            Updatepicdata updatepicdata = response.body().getData();
                            data = updatepicdata.getArtistPic();
                            Log.d("QWERTYYY", String.valueOf(data));

                           /* stagename.setText(updatepicdata.getStageName());
                            email.setText(updatepicdata.getUsername());
                            age.setText("" + updatepicdata.getAge());
                            height.setText("" + updatepicdata.getHeight());
                            weight.setText("" + updatepicdata.getWeight());
                            ssn.setText(updatepicdata.getSsn());*/

                           // Glide.with(getContext()).load(updatepicdata.getProfilePic()).placeholder(R.drawable.lg_logo).into(image);

                            images.clear();
                            image_id.clear();
                            image_status.clear();
                            for (int i = 0; i < data.size(); i++) {
                                images.add(data.get(i).getPath());
                                image_id.add(String.valueOf(data.get(i).getId()));
                                image_status.add(String.valueOf(data.get(i).getActive()));
                            }
                            customAdapter.notifyDataSetChanged();

                            alert(getResources().getString(R.string.files_update));
                            //  Toast.makeText(getContext(), "" + getResources().getString(R.string.success_msg), Toast.LENGTH_SHORT).show();


                            // Creating user login session
                            // For testing i am stroing name, email as follow
                            // Use user real data

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
            public void onFailure(Call<ResponseUpdatePic> call, Throwable t) {
                Log.d("ERRor",t.getMessage());
                // Toast.makeText(getContext(), "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private JsonObject ApiJsonMap() {

        Log.d("TESTER", String.valueOf(data.size()));

        for(int i=0;i<data.size();i++)
        {
            try {
                Log.d("TY", String.valueOf(data.get(i).getId()));
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("id",data.get(i).getId());
                jsonObject.put("path",data.get(i).getPath());
                jsonObject.put("profile",data.get(i).getProfile());
                jsonObject.put("active",data.get(i).getActive());

                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        //  Toast.makeText(getContext(), ""+jsonArray, Toast.LENGTH_LONG).show();

        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("id", id);
            jsonObj_.put("customer_pic", jsonArray);



            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());

            //print parameter
            Log.e("MY gson.JSON:  ", "AS PARAMETER  " + gsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gsonObject;
    }


    public void heightweight()
    {
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.text, arrHeightList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_height.setAdapter(adapter);
        String height = str_height.replaceAll("\\."  ,  "'");
        if (!height.isEmpty()) {
            for (int i = 0; i < arrHeightList.size(); i++) {
                if (height.equalsIgnoreCase(arrHeightList.get(i))) {
                    spinner_height.setSelection(i);
                    break;
                }
            }
        }
        else
        {
            spinner_height.setSelection(0);
        }

        ArrayAdapter adapter2 = new ArrayAdapter(getContext(), R.layout.text, arrWeightList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_weight.setAdapter(adapter2);
        if (!str_weight.isEmpty()) {
            for (int i = 0; i < arrWeightList.size(); i++) {
                if (str_weight.equals(arrWeightList.get(i))) {
                    spinner_weight.setSelection(i);
                    break;
                }
            }
        }
        else
        {
            spinner_weight.setSelection(0);
        }

    }




}