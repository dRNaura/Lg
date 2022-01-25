package com.lg_project.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;
import com.lg_project.activity.MainActivity2;
import com.lg_project.adapter.AllStealthAdapter;
import com.lg_project.modelclass.AllStealthModel;
import com.lg_project.modelclass.ChatModel;
import com.lg_project.modelclass.ResponseChatHistory;

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

public class AllStealthFragment extends Fragment {

    RecyclerView recycle_stealth;
    AllStealthAdapter allStealthAdapter;
    ArrayList<AllStealthModel> arrAllStealthList=new ArrayList<>();
    SessionManageent sessionManageent;
    LinearLayout back;
    String user_id="";
    int stealth_code=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_stealth, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        MainActivity2.l1.setVisibility(View.GONE);
    }

    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        sessionManageent = new SessionManageent(getContext());


        HashMap<String, String> user = sessionManageent.getUserDetails();
        String email = user.get(SessionManageent.KEY_Credit);
        user_id = user.get(SessionManageent.KEY_ID);

        recycle_stealth=v.findViewById(R.id.recycle_stealth);
        back=v.findViewById(R.id.back);

        HashMap<String, String> setting = sessionManageent.getPreferedSetting();
        stealth_code= Integer.parseInt(setting.get(SessionManageent.KEY_STEALTH_CODE));
        LinearLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        recycle_stealth.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        allStealthAdapter = new AllStealthAdapter(getContext(), arrAllStealthList);
        recycle_stealth.setAdapter(allStealthAdapter);

        getAllStealth();
        clickListeners();
    }
    public void clickListeners(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StleathMode menu=new StleathMode() ;
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_content,menu).commit();
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        allStealthAdapter.setOnClicked(new AllStealthAdapter.OnClicked() {
            @Override
            public void setOnClick(int position) {
                    int  stealth_code =arrAllStealthList.get(position).id;
                    String  stealth_codetitle =arrAllStealthList.get(position).title;
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                    builder.setMessage(getResources().getString(R.string.set_stealth))
//                            .setCancelable(false)
//                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
//                                public void onClick(final DialogInterface dialog, final int id) {
                                    configure_stealth(1,stealth_code,"test",stealth_codetitle);
//                                }
//                            })
//                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                public void onClick(final DialogInterface dialog, final int id) {
//                        dialog.cancel();
//                                }
//                            });
//                    final AlertDialog alert = builder.create();
//                    alert.show();
                }
        });
    }

    public void getAllStealth() {
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.all_stealth();
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
                            sessionManageent.logoutUser();
                            Toast.makeText(getContext(), "" + getResources().getString(R.string.session), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(getContext(), "" +errorCode, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.body() != null) {
                        arrAllStealthList.clear();
                        String respo = response.body().string();
                        JSONObject responseObject = new JSONObject(respo);
                        Log.e("stealth List", responseObject.toString());
                        String successstatus = responseObject.optString("success");
                        if (successstatus.equalsIgnoreCase("true")) {
                            JSONArray data = responseObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);
                                AllStealthModel model = new AllStealthModel();
                                model.id = object.getInt("id");
                                model.title = object.getString("title");
                                model.language = object.getString("language");
                                if(stealth_code==object.getInt("id")){
                                 model.selected="yes";
                                }
                                arrAllStealthList.add(model);
                            }
                            allStealthAdapter.notifyDataSetChanged();

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
    public void configure_stealth(int stealth_mode,int stealthCode,String app_icon,String msg) {
        /** Create handle for the RetrofitInstance interface*/
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        JSONObject jsonObject = new JSONObject();
        try {
            //chnage
            jsonObject.put("user", user_id);//user id
            jsonObject.put("app_icon", app_icon);//user id
            jsonObject.put("stealth_code", stealth_code);//cust+contractor user id
            jsonObject.put("stealth_mode", stealth_mode);//cust+contractor user id
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("ObjArray", jsonObject.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Api apiService = retrofit.create(Api.class);
        Call<ResponseBody> call = apiService.configureStealth("customer/configure/stealth", body);//socket url/chats
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
                            sessionManageent.logoutUser();
                            Toast.makeText(getContext(), "" + getResources().getString(R.string.session), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(getContext(), "" +errorCode, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (response.body() != null) {
                        String respo = response.body().string();
                        JSONObject responseObject = new JSONObject(respo);
                        Log.e(" List", responseObject.toString());
                        String successstatus = responseObject.optString("success");
                        String message = responseObject.optString("message");

                        if (successstatus.equalsIgnoreCase("true")) {
                            Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                            sessionManageent.preferedSetting(app_icon,stealthCode,stealth_mode,msg);
                            stealth_code=stealthCode;
                            getAllStealth();
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