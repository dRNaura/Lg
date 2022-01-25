package com.lg_project.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.bumptech.glide.Glide;
import com.lg_project.activity.MainActivity2;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;
import com.lg_project.adapter.CustomAdapterDetails;
import com.lg_project.modelclass.ContractorActiveModel;
import com.lg_project.modelclass.ContractorListModel;
import com.lg_project.modelclass.GetRaceBodyTypeHairModel;
import com.lg_project.modelclass.ResponseCustomerListDetail;
import com.lg_project.modelclass.SocketManager;
import com.lg_project.modelclass.contractordetails.ArtistPic;
import com.lg_project.modelclass.contractordetails.ContractorDetailData;
import com.lg_project.modelclass.contractordetails.ResponseContractorDetails;
import com.lg_project.modelclass.tipresponse.ResponseTip;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class Details extends Fragment {
    RecyclerView recyclerView;
    ArrayList<String> images = new ArrayList<>();
    String id, strimage, userid;
    Button tip_artist;

    CircularImageView image;
    TextView name, age, height, stats,weight,txt_detail_body_type,txt_detail_race,txt_greeting,txt_languages;
    SessionManageent session;
    String contractorid;
    String user_name="",beneficiary_name="",customer_id="",status="";
    Double amount=0.0;
    private CustomAdapterDetails customAdapter;
    private LinearLayout back;
    public static final int PAYPAL_REQUEST_CODE = 1111;
    public Button btn_live_chat;
    ArrayList<ContractorListModel> arrMssCustomerList = new ArrayList<>();
    List<ContractorListModel> data = new ArrayList<>();
    ArrayList<ContractorActiveModel> arrActiveCustomerList = new ArrayList<>();
    SocketManager socketManager;
    private Socket mSocket;
    LinearLayout lay_spoken_lang;
    ProgressDialog progressDialog;

    private ArrayList<String> arrLanguageList = new ArrayList<>();
    ArrayList<String> languageIdList = new ArrayList<>();
    ArrayList<GetRaceBodyTypeHairModel> arrSpokenLanguages = new ArrayList<>();
//    public static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK).clientId(PayPayConfiguration.PAYPAL_CLIENT_ID);

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
        View v = inflater.inflate(R.layout.fragment_details, container, false);

        return v;
    }

    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);


        session = new SessionManageent(getContext());
        HashMap<String, String> user = session.getUserDetails();
        customer_id = user.get(SessionManageent.KEY_CUST_ID);

        //start paypal service
//        Intent intent = new Intent(getContext(), PayPalService.class);
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//        getContext().startService(intent);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading.....");
        tip_artist = (Button) v.findViewById(R.id.tip_artist);
        name = (TextView) v.findViewById(R.id.name);
        age = (TextView) v.findViewById(R.id.age);
        height = (TextView) v.findViewById(R.id.height);
        stats = (TextView) v.findViewById(R.id.stats);
        weight = (TextView) v.findViewById(R.id.weight);
        txt_detail_body_type = (TextView) v.findViewById(R.id.txt_detail_body_type);
        txt_detail_race = (TextView) v.findViewById(R.id.txt_detail_race);
        txt_greeting = (TextView) v.findViewById(R.id.txt_greeting);
        txt_languages = (TextView) v.findViewById(R.id.txt_languages);

        image = (CircularImageView) v.findViewById(R.id.image);
        back =  v.findViewById(R.id.back);
        recyclerView = (RecyclerView) v.findViewById(R.id.images_recycle);
        btn_live_chat = v.findViewById(R.id.btn_live_chat);
        lay_spoken_lang = v.findViewById(R.id.lay_spoken_lang);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        customAdapter = new CustomAdapterDetails(getContext(), images);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("id", null);
            strimage = bundle.getString("image", null);
        }

        Glide.with(getContext()).load(strimage).placeholder(R.drawable.lg_logo).into(image);

        /*socketManager = (SocketManager)getActivity().getApplication();
        mSocket = socketManager.getSocket();
        mSocket.on("all-contractors", allContractors);
        mSocket.on("user-chat-history-lite-ack", chatHistoryCustomer);
        mSocket.connect();

        mSocket.emit("avail_contractor", "");*/
        getSpokenLanguage();
        DetailsData(id);



        btn_live_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*for(int a=0;a<arrMssCustomerList.size();a++){
                    if(arrMssCustomerList.get(a).id==Integer.parseInt(id)){
                        ContractorListModel model = arrMssCustomerList.get(a);*/
                        Chat fragment2 = new Chat();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", name.getText().toString());
                        bundle.putBoolean("status", false);
                        bundle.putInt("idCustomer", Integer.parseInt(id));
                        bundle.putInt("useridCustomer", Integer.parseInt(userid));
                        bundle.putString("activeCustomerSocketID","");
                        fragment2.setArguments(bundle);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.drawerLayout, fragment2);
                        fragmentTransaction.commit();
                       /* break;
                    }
                }*/

            }
        });

        customAdapter.setOnItemClicked(new CustomAdapterDetails.OnClicked() {
            @Override
            public void setOnItem(int position) {
                final Dialog optionDialog = new Dialog(getContext());
                optionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                optionDialog.setContentView(R.layout.profile_image_view);
                optionDialog.setCancelable(true);
                Window window = optionDialog.getWindow();
                window.setLayout(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.MATCH_PARENT);
                window.setGravity(Gravity.CENTER);
                String img="";
                img=images.get(position);
                ImageView image_profile = optionDialog.findViewById(R.id.image_profile);
                Glide.with(getContext()).load(img).placeholder(R.drawable.lg_logo).into(image_profile);
                optionDialog.show();
            }
        });


        tip_artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog optionDialog = new Dialog(getContext());
                optionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                optionDialog.setContentView(R.layout.tip_alert);
                optionDialog.setCancelable(true);
                Window window = optionDialog.getWindow();
                window.setLayout(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);

                EditText editText = (EditText) optionDialog.findViewById(R.id.amount);
                Button b1 = (Button) optionDialog.findViewById(R.id.submit);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!editText.getText().toString().isEmpty()){
                        amount = Double.valueOf(editText.getText().toString());
                            if (amount > 0) {
                                optionDialog.dismiss();
                                onBraintreeSubmit();
//                                processPayment();
                            } else {
                                Toast.makeText(getContext(), ""+getResources().getString(R.string.greater_amount), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), ""+getResources().getString(R.string.enter_amount), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                optionDialog.show();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog optionDialog = new Dialog(getContext());
                optionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                optionDialog.setContentView(R.layout.profile_image_view);
                optionDialog.setCancelable(true);
                Window window = optionDialog.getWindow();
                window.setLayout(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.MATCH_PARENT);
                window.setGravity(Gravity.CENTER);

                ImageView image_profile = optionDialog.findViewById(R.id.image_profile);
                Glide.with(getContext()).load(strimage).placeholder(R.drawable.lg_logo).into(image_profile);
                optionDialog.show();

            }
        });


    }

    private Emitter.Listener allContractors = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrActiveCustomerList.clear();
                        JSONArray array = (JSONArray) args[0];
                        Log.d("allContractorSocket", array.toString());
                        try {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject data = array.getJSONObject(i);
                                ContractorActiveModel model = new ContractorActiveModel();
                                if(data.has("id") && !data.isNull("id")){
                                    model.id = data.getString("id");
                                }
                                if(data.has("username") && !data.isNull("username")){
                                    model.username = data.getString("username");
                                }
                                if(data.has("user") && !data.isNull("user")){
                                    model.user = data.getInt("user");
                                }
                                if(data.has("room") && !data.isNull("room")){
                                    model.room = data.getString("room");
                                }
                                if(data.has("online") && !data.isNull("online")){
                                    model.online = data.getBoolean("online");
                                }
                                /*if(data.has("email") && !data.isNull("email")){
                                    model.email = data.getString("email");
                                }*/
                                if(data.has("online") && !data.isNull("online")){
                                    model.online = data.getBoolean("online");
                                }
                                Log.d("USER", String.valueOf(model.user));

                                if (!data.isNull("last_msg")) {
                                    model.last_msg = data.getString("last_msg");
                                    model.last_msg_date = data.getString("last_msg_date");
                                }

                                arrActiveCustomerList.add(model);
                            }
                        } catch (Exception e) {
                            Log.e("Exception", e.toString());
                            return;
                        }

                        HashMap<String, String> socket = session.getSocketID();
                        String socketID = socket.get(session.KEY_SOCKET_ID);


                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("id", socketID);
                            jsonObject.put("room", "");
                            jsonObject.put("username", beneficiary_name);
                            jsonObject.put("user", id);
                        } catch (JSONException e) {
                            Log.e("Exception", e.toString());
                        }
                        Log.e("json", jsonObject.toString());
                        mSocket.emit("user-chat-history-lite", jsonObject);

                      //  AvailableData();

//                        filterActiveCustomerList();
//                    Log.e("allCustomer", array.toString());
                    }

                });
            }
        }
    };
    private Emitter.Listener chatHistoryCustomer = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrActiveCustomerList.clear();
                        JSONArray array = (JSONArray) args[0];
                        Log.e("allCustomerNewEMit", array.toString());
                        try {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject data = array.getJSONObject(i);
                                ContractorActiveModel model = new ContractorActiveModel();
                                model.id = data.getString("id");
                                model.username = data.getString("username");
                                model.user = data.getInt("user");
                                model.room = data.getString("room");
                                model.online = data.getBoolean("online");
                               // model.email = data.getString("email");
                                if (!data.isNull("last_msg")) {
                                    model.last_msg = data.getString("last_msg");
                                    model.last_msg_date = data.getString("last_msg_date");
                                }
                                arrActiveCustomerList.add(model);
                            }
                        } catch (Exception e) {
                            Log.e("Exception", e.toString());
                            return;
                        }

                       // AvailableData();

//                        customerList();
//                        filterActiveCustomerList();
//                    Log.e("allCustomer", array.toString());
                    }

                });
            }
        }
    };


    public void DetailsData(String id) {
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseContractorDetails> call = api.detail(id);

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseContractorDetails>() {
            @Override
            public void onResponse(Call<ResponseContractorDetails> call, Response<ResponseContractorDetails> response) {
                List<ContractorDetailData> list = response.body().getData();
                List<ArtistPic> pic = list.get(0).getArtistPic();
                Log.d("SESE", String.valueOf(pic.size()));
                Log.d("userid", String.valueOf(list.get(0).getUser()));
                userid = String.valueOf(list.get(0).getUser());

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
                    contractorid = String.valueOf(list.get(0).getUser());
                    for (int i1 = 0; i1 < pic.size(); i1++) {
                        Log.d("IMM", String.valueOf(pic.get(i1).getPath()));
                        if(!(pic.get(i1).getActive().equals("Pending")) && !(pic.get(i1).getActive().equals("Rejected"))) {
                            images.add(pic.get(i1).getPath());
                        }
                    }


                    for (int i = 0; i < list.size(); i++) {
                        beneficiary_name=list.get(i).getFirstName() + " " + list.get(i).getLastName();
                        status = list.get(i).getStatus();
                        name.setText(list.get(i).getStageName());
                        age.setText("" + list.get(i).getAge() + " yrs");
                        height.setText(list.get(i).getHeight() + "ft");
                        stats.setText("'" + list.get(i).getStats() + "'");
                        weight.setText(list.get(i).getWeight() + " lbs");
                        txt_detail_body_type.setText(list.get(i).getBody_type_desc());
                        txt_detail_race.setText(list.get(i).getRace_desc());
                        if(list.get(i).getGreeting() !=null){
                            txt_greeting.setVisibility(View.VISIBLE);
                            txt_greeting.setText(list.get(i).getGreeting());
                        }else{
                            txt_greeting.setVisibility(View.GONE);
                        }



                        //
                       /* String spokenLang="";
                        try {
                            if(list.get(i).getSpoken_language() !=null){
                                String languages=list.get(i).getSpoken_language();

                                String[] separated = languages.split("_");

                                // temprory condition needs to change
                                if(!separated[0].isEmpty()){
                                    if(separated[0].equalsIgnoreCase("en")){
                                        spokenLang=spokenLang+""+getResources().getString(R.string.English);
                                    }
                                    if(separated[0].equalsIgnoreCase("es")){
                                        spokenLang=spokenLang+""+getResources().getString(R.string.spanish);
                                    }
                                    if(separated[0].equalsIgnoreCase("fr")){
                                        spokenLang=spokenLang+""+getResources().getString(R.string.french);
                                    }
                                }
                                if(!separated[1].isEmpty()){
                                    if(separated[1].equalsIgnoreCase("en")){
                                        spokenLang=spokenLang+", "+getResources().getString(R.string.English);;
                                    }
                                    if(separated[1].equalsIgnoreCase("es")){
                                        spokenLang=spokenLang+", "+getResources().getString(R.string.spanish);;
                                    }
                                    if(separated[1].equalsIgnoreCase("fr")){
                                        spokenLang=spokenLang+", "+getResources().getString(R.string.french);
                                    }
                                }
                                if(!separated[2].isEmpty()){
                                    if(separated[2].equalsIgnoreCase("en")){
                                        spokenLang=spokenLang+", "+getResources().getString(R.string.English);;
                                    }
                                    if(separated[2].equalsIgnoreCase("es")){
                                        spokenLang=spokenLang+", "+getResources().getString(R.string.spanish);;
                                    }
                                    if(separated[2].equalsIgnoreCase("fr")){
                                        spokenLang=spokenLang+","+getResources().getString(R.string.french);
                                    }
                            }

                            }
                        }catch (Exception e){
//                            Log.e("Exc",e.toString());
                        }*/
                        String languages=list.get(0).getSpoken_language();
                        String spokenLang="";
                        StringBuilder result = new StringBuilder();
                        String[] separated = languages.split("_");

                        for(int i1=0;i1<separated.length;i1++)
                        {
                            for(int j=0;j<arrSpokenLanguages.size();j++)
                            {
                                if(arrSpokenLanguages.get(j).code.equals(separated[i1]))
                                {
                                    //spokenLang=spokenLang+", "+arrSpokenLanguages.get(j).title;
                                    spokenLang = arrSpokenLanguages.get(j).title;
                                    result.append( spokenLang).append( ", " );
                                }
                            }
                        }

                        String withoutLastComma = result.substring( 0, result.length( ) - ", ".length( ) );
                        System.err.println( withoutLastComma );




                        if(spokenLang.equalsIgnoreCase("")){
                            lay_spoken_lang.setVisibility(View.GONE);
                        }else{
                            lay_spoken_lang.setVisibility(View.VISIBLE);
                            txt_languages.setText(withoutLastComma);
                        }

                    }


                    customAdapter.notifyDataSetChanged();

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseContractorDetails> call, Throwable t) {
                //  Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void makePayment(String id, String contractorid, Double amount,String nounce) {
        progressDialog.show();
        /** Create handle for the RetrofitInstance interface*/
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sender", id);//userid
            jsonObject.put("beneficiary", contractorid);//userid
            jsonObject.put("amount", amount);
            jsonObject.put("nonce", nounce);
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
                            MyPayment myPayment = new MyPayment();
                            getFragmentManager().beginTransaction().replace(R.id.drawerLayout, myPayment).commit();

                            // Creating user login session
                            // For testing i am stroing name, email as follow
                            // Use user real data
                            // List<Datum> list=response.body().getData();
                            // Log.d("IId", String.valueOf(list.get(0).getId()));
                            Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();

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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERRor", t.getMessage());

//                Toast.makeText(Login.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


//    public void amount(String id, String contractorid, String amount,String nounce ) {
//        /** Create handle for the RetrofitInstance interface*/
//        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("sender", id);
//            jsonObject.put("beneficiary", contractorid);
//            jsonObject.put("amount", amount);
//            jsonObject.put("nonce", nounce);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Log.e("obj",jsonObject.toString());
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
//        Api api = retrofit.create(Api.class);
//        Call<ResponseTip> call = api.Tipartist(body);
//        Log.e("URL Called", call.request().url() + "");
//        call.enqueue(new Callback<ResponseTip>() {
//            @Override
//            public void onResponse(Call<ResponseTip> call, Response<ResponseTip> response) {
//
//                Log.d("SUCCESS", String.valueOf(response.body()));
//                // Log.d("SUCCESSLOG", String.valueOf(response.body().getSuccess()));
//
//                try {
//                    String errorbody = "", errorCode = "";
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
//                    if (response.isSuccessful()) {
//                        if (response.body().getSuccess()) {
//                            Log.d("SUCCESS", String.valueOf(response.body().getSuccess()));
//                            MyPayment myPayment = new MyPayment();
//                            getFragmentManager().beginTransaction().replace(R.id.drawerLayout, myPayment).commit();
//
//                            // Creating user login session
//                            // For testing i am stroing name, email as follow
//                            // Use user real data
//                            // List<Datum> list=response.body().getData();
//                            // Log.d("IId", String.valueOf(list.get(0).getId()));
//                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//
//                        //  List<Datum> list = response.body().getData();
//                   /* for (int i = 0; i < list.size(); i++) {
//                        Log.d("TEST", list.get(i).getFirstName());
//                    }*/
//                    } else {
//                        Toast.makeText(getContext(), "" + getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
//
//
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseTip> call, Throwable t) {
//                Log.e("ERRor", t.getMessage());
//                //Toast.makeText(Login.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    public void onBraintreeSubmit() {

//        DropInRequest dropInRequest = new DropInRequest().clientToken("sandbox_jyyn76fq_yhfkjhsf8fnbfj58");
//        dropInRequest.amount("1");

//        DropInRequest dropInRequest = new DropInRequest()
//                .tokenizationKey("sandbox_css89xw8_yhfkjhsf8fnbfj58");
//        startActivityForResult(dropInRequest.getIntent(MainActivity.this), REQUEST_CODE);

        DropInRequest dropInRequest = new DropInRequest().tokenizationKey("sandbox_mfqdhxgd_tqvvgtyd3qttrvn4");
        dropInRequest.amount(String.valueOf(amount));
        startActivityForResult(dropInRequest.getIntent(getContext()), PAYPAL_REQUEST_CODE);

    }

//    public void processPayment() {
//        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD",
//                "Tip Artist", PayPalPayment.PAYMENT_INTENT_SALE);
//        Intent intent = new Intent(getContext(), PaymentActivity.class);
//        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
//        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
//    }
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
//                        int id = Integer.parseInt(user.get(SessionManageent.KEY_ID));
//                        Log.e("ERER", String.valueOf(id));
//                        amount(String.valueOf(id), contractorid, amount);
//                        Toast.makeText(getContext(), ""+getResources().getString(R.string.success_transaction), Toast.LENGTH_SHORT).show();
//
//                    } catch (Exception e) {
//                        Log.e("Exception", e.toString());
//                    }
//                } else if (resultCode == Activity.RESULT_CANCELED)
//                    Toast.makeText(getContext(), ""+getResources().getString(R.string.transaction_cancel), Toast.LENGTH_SHORT).show();
//
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
                // email
                int id = Integer.parseInt(user.get(SessionManageent.KEY_ID));
                user_name = user.get(SessionManageent.KEY_FIRSTNAME);
                Log.e("ERER", String.valueOf(id));
                makePayment(String.valueOf(id), contractorid, amount,paymentMethodNonce);
//                Toast.makeText(getContext(), ""+getResources().getString(R.string.success_transaction), Toast.LENGTH_SHORT).show();

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

  /*  public void AvailableData() {
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);

        *//** Call the method with parameter in the interface to get the notice data*//*
        Call<ResponseCustomerListDetail> call = api.contractorr(String.valueOf(id));

        *//**Log the URL called*//*
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseCustomerListDetail>() {
            @Override
            public void onResponse(Call<ResponseCustomerListDetail> call, Response<ResponseCustomerListDetail> response) {
                //  Log.d("COUN", String.valueOf(response.body().getMessage()));
                arrMssCustomerList.clear();
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

                    data = response.body().getData();
                    for (int i = 0; i < data.size(); i++) {
                        Log.d("RERERE", data.get(i).first_name);

                        ContractorListModel model = new ContractorListModel();
                        model.isFavArtist = data.get(i).isFavArtist;
                        model.first_name = data.get(i).first_name;
                        model.profile_pic = data.get(i).profile_pic;
                        model.age = data.get(i).age;
                        model.height = data.get(i).height;
                        model.id = data.get(i).id;
                        model.weight = data.get(i).weight;
                        model.stats = data.get(i).stats;
                        model.user = data.get(i).user;
                        model.phone = data.get(i).phone;
                        model.state = data.get(i).state;
                        model.body_type_desc = data.get(i).body_type_desc;
                        model.hair_color = data.get(i).hair_color;
                        model.race_desc = data.get(i).race_desc;
                        model.body_type = data.get(i).body_type;
                        model.race = data.get(i).race;
                        model.ssn = data.get(i).ssn;
                        model.mailing_address = data.get(i).mailing_address;
                        model.stage_name = data.get(i).stage_name;

                        for (int a = 0; a < arrActiveCustomerList.size(); a++) {
                            if (model.user == arrActiveCustomerList.get(a).user) {
                                model.online = arrActiveCustomerList.get(a).online;
                                model.activeCustomerSocketID = arrActiveCustomerList.get(a).id;
                                model.last_msg = arrActiveCustomerList.get(a).last_msg;
                                model.last_msg_date = arrActiveCustomerList.get(a).last_msg_date;
                            }
                        }


                        arrMssCustomerList.add(model);
                    }


                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseCustomerListDetail> call, Throwable t) {
                //  Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), "" + getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });

    }*/

    public void getSpokenLanguage() {
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.getSpokenLanguages();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String errorbody="",errorCode="";
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

                    if (response.body() != null) {
                        arrSpokenLanguages.clear();
                        String respo = response.body().string();
                        JSONObject responseObject = new JSONObject(respo);
                        Log.e("language List", responseObject.toString());
                        String successstatus = responseObject.optString("success");

                        if (successstatus.equalsIgnoreCase("true")) {

                            JSONArray parent_categoriesOb = responseObject.getJSONArray("data");
                            for (int i = 0; i < parent_categoriesOb.length(); i++) {
                                JSONObject object = parent_categoriesOb.getJSONObject(i);
                                GetRaceBodyTypeHairModel model = new GetRaceBodyTypeHairModel();
                                model.id = object.getInt("id");
                                model.title = object.getString("title");
                                model.code = object.getString("code");
                                String title= object.getString("title");
                                String ids=  object.getString("code");
                                arrLanguageList.add(title);
                                languageIdList.add(ids);
                                arrSpokenLanguages.add(model);

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

}