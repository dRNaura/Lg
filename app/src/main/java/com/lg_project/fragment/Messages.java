package com.lg_project.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.lg_project.adapter.CustomAdapterMessages;
import com.lg_project.modelclass.ContractorActiveModel;
import com.lg_project.modelclass.ContractorListModel;
import com.lg_project.modelclass.ResponseCustomerListDetail;
import com.lg_project.modelclass.SocketManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.google.android.gms.internal.zzahn.runOnUiThread;

public class Messages extends Fragment {
    RecyclerView recyclerView;
    private LinearLayout back;
    private CustomAdapterMessages customAdapter;
    private Socket mSocket;
    SocketManager socketManager;
    List<ContractorListModel> data = new ArrayList<>();
    SessionManageent session;
    String userFullName = "";
    int userid;
    String socket;
    ArrayList<ContractorActiveModel> arrchathistoryCustomerList = new ArrayList<>();
    ArrayList<ContractorActiveModel> arrAvailableContractor = new ArrayList<>();
    ArrayList<ContractorListModel> arrAllContractorList = new ArrayList<>();
    ArrayList<ContractorListModel> arFilteredCustomerList = new ArrayList<>();
    ContractorListModel model;
    ImageView nodata;
    ProgressBar progressBar;
    Sprite wave;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_messages, container, false);
//        MainActivity2.heading.setText(R.string.msgs);
        MainActivity2.l1.setVisibility(View.VISIBLE);
        session = new SessionManageent(getContext());
        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManageent.KEY_Credit);
        // customer_id = user.get(SessionManageent.KEY_CUST_ID);
        userFullName = user.get(SessionManageent.KEY_FIRSTNAME);
        userid = Integer.parseInt(user.get(SessionManageent.KEY_ID));

        progressBar = v.findViewById(R.id.progress);
        wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        progressBar.setBackgroundResource(android.R.color.transparent);


        socketManager = (SocketManager)getActivity().getApplication();
        mSocket = socketManager.getSocket();
        mSocket.on("all-contractors", allContractors);
        mSocket.on("user-chat-history-lite-ack", history);
        mSocket.connect();


        nodata = v.findViewById(R.id.nodata);
        recyclerView = v.findViewById(R.id.recycle_message);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        //og.d("ACTIVE", String.valueOf(arrActiveUserList.size()));


        customAdapter = new CustomAdapterMessages(getContext(), arFilteredCustomerList);
        recyclerView.setAdapter(customAdapter);
         // set the Adapter to RecyclerView

        customAdapter.setOnClicked(new CustomAdapterMessages.OnClicked() {
            @Override
            public void setOnItem(int position) {
                ContractorListModel model = arFilteredCustomerList.get(position);

                Chat fragment2 = new Chat();
                Bundle bundle = new Bundle();
                bundle.putString("name", model.stage_name);
                bundle.putBoolean("status", model.online);
                bundle.putInt("idCustomer", model.id);
                bundle.putInt("useridCustomer", model.user);
                bundle.putString("activeCustomerSocketID", model.activeCustomerSocketID);
                fragment2.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.drawerLayout, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        back = v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity2.class);
                startActivity(intent);
                onDestroy();
            }
        });


        AvailableData();


        return v;
    }

    private final Emitter.Listener allContractors = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrAvailableContractor.clear();
                        JSONArray array = (JSONArray) args[0];
                        Log.e("allContractorSocket", array.toString());

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
                                }/*if(data.has("email") && !data.isNull("email")){
                                    model.email = data.getString("email");
                                }*/

                               /* if (!data.isNull("last_msg")) {
                                    model.last_msg = data.getString("last_msg");
                                    model.last_msg_date = data.getString("last_msg_date");
                                }
*/
                                arrAvailableContractor.add(model);
                            }
                        } catch (Exception e) {
                            Log.e("Exception", e.toString());
                            return;
                        }

                        for(int a=0 ; a<arFilteredCustomerList.size(); a++){
                            ContractorListModel modelHistory =arFilteredCustomerList.get(a);
                            for(int b=0 ; b<arrAvailableContractor.size();b++){
                                if(modelHistory.user == arrAvailableContractor.get(b).user){
                                    modelHistory.activeCustomerSocketID=arrAvailableContractor.get(b).id;
                                    modelHistory.online=arrAvailableContractor.get(b).online;
                                    arFilteredCustomerList.remove(a);
                                    arFilteredCustomerList.add(a,modelHistory);
                                }

                            }
                        }

                        customAdapter.notifyDataSetChanged();



                      //  AvailableData();

//                        filterActiveCustomerList();
//                    Log.e("allCustomer", array.toString());
                    }

                });
            }
        }
    };



    public void AvailableData() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseCustomerListDetail> call = api.contractorr(String.valueOf(userid));
        Log.wtf("URL Called", call.request().url() + "");
        call.enqueue(new Callback<ResponseCustomerListDetail>() {
            @Override
            public void onResponse(Call<ResponseCustomerListDetail> call, Response<ResponseCustomerListDetail> response) {
                //  Log.d("COUN", String.valueOf(response.body().getMessage()));
                arrAllContractorList.clear();
                progressBar.setVisibility(View.INVISIBLE);
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
//                        Log.d("RERERE", data.get(i).first_name);

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

                        arrAllContractorList.add(model);

                        /*for (int a = 0; a < arrchatActiveCustomerList.size(); a++) {
                            if (model.user == arrchatActiveCustomerList.get(a).user) {
                                model.online = arrchatActiveCustomerList.get(a).online;
                                model.activeCustomerSocketID = arrchatActiveCustomerList.get(a).id;
                                model.last_msg = arrchatActiveCustomerList.get(a).last_msg;
                                model.last_msg_date = arrchatActiveCustomerList.get(a).last_msg_date;
                                arrAllContractorList.add(model);
                            }
                        }
*/

                    }

                    if (arrAllContractorList.size() == 0) {
                        nodata.setVisibility(View.VISIBLE);
                    } else {
                        nodata.setVisibility(View.GONE);
                    }

                    HashMap<String, String> socket = session.getSocketID();
                    String socketID = socket.get(session.KEY_SOCKET_ID);


                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", userFullName);
                        jsonObject.put("id", mSocket.id());
                        jsonObject.put("user", userid);
                        jsonObject.put("room", "");
                    } catch (JSONException e) {
                        Log.e("Exception", e.toString());
                    }
                    Log.e("json Message", jsonObject.toString());
                    mSocket.emit("user-chat-history-lite", jsonObject);
                    customAdapter.notifyDataSetChanged();

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseCustomerListDetail> call, Throwable t) {
               //   Log.e("ERRor",t.getMessage());
//                Toast.makeText(getContext(), "" + getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private final Emitter.Listener history = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrchathistoryCustomerList.clear();
                        arFilteredCustomerList.clear();
                        JSONArray array = (JSONArray) args[0];
                        Log.e("allCustomerNewEMit", array.toString());
                        try {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject data = array.getJSONObject(i);
                                ContractorActiveModel model = new ContractorActiveModel();
                                if(data.has("id") && !data.isNull("id")){
                                    model.id = data.getString("id");
                                }
                                if(data.has("user") && !data.isNull("user")){
                                    model.user = data.getInt("user");
                                }
                                if(data.has("userFrom") && !data.isNull("userFrom")){
                                    model.userFrom = data.getString("userFrom");
                                }

                                if (!data.isNull("last_msg")) {
                                    model.last_msg = data.getString("last_msg");
                                    model.last_msg_date = data.getString("last_msg_date");
                                }
                                arrchathistoryCustomerList.add(model);
                            }
                        } catch (Exception e) {
                            Log.e("Exception", e.toString());
                            return;
                        }

                        for(int i =0 ;i <arrchathistoryCustomerList.size();i++){
                            ContractorActiveModel histormodel=arrchathistoryCustomerList.get(i);
                            for(int a=0 ; a<arrAllContractorList.size(); a++){
                                ContractorListModel model =arrAllContractorList.get(a);
                                if(histormodel.user == model.user){
                                    model.last_msg = histormodel.last_msg;
                                    model.last_msg_date = histormodel.last_msg_date;
                                    arFilteredCustomerList.add(model);
                                }
                            }


                        }

                        if(arrchathistoryCustomerList.size()==0){
                            nodata.setVisibility(View.VISIBLE);
                        }else{
                            nodata.setVisibility(View.GONE);
                        }

                        customAdapter.notifyDataSetChanged();

                        mSocket.emit("avail_contractor", "");

                        //AvailableData();

//                        customerList();
                      //  filterActiveCustomerList();
//                    Log.e("allCustomer", array.toString());
                    }

                });
            }
        }
    };
        /*public void filterActiveCustomerList () {
        arrActiveUserList.clear();
        Log.d("REWSE", String.valueOf(arrAllContractorList.size()));
        for (int i = 0; i < arrAllContractorList.size(); i++) {
           ContractorListModel  model=arrAllContractorList.get(i);
            for (int a = 0; a < arrchatActiveCustomerList.size(); a++) {

                if (model.user==arrchatActiveCustomerList.get(a).user) {
                    ContractorActiveModel modelActive = arrchatActiveCustomerList.get(a);
                    model.online = modelActive.online;
                    model.activeCustomerSocketID =modelActive.id;
                    model.last_msg =modelActive.last_msg;
                    model.last_msg_date =modelActive.last_msg_date;
                    arrActiveUserList.add(model);
                    break;


                }
            }

        }
        if(arrActiveUserList.size()==0){
            nodata.setVisibility(View.VISIBLE);
        }else{
            nodata.setVisibility(View.GONE);
        }
            arrAllContractorList.clear();
            arrAllContractorList.addAll(arrActiveUserList);
            recyclerView.setAdapter(customAdapter);
            customAdapter.notifyDataSetChanged();
      //  Log.d("DATAaa",arrActiveUserList.get(0).last_msg);


    }
*/
}