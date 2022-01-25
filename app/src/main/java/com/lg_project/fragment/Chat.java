package com.lg_project.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lg_project.activity.MainActivity2;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;
import com.lg_project.adapter.ChatAdapter;
import com.lg_project.modelclass.ChatModel;
import com.lg_project.modelclass.ContractorActiveModel;
import com.lg_project.modelclass.ContractorListModel;
import com.lg_project.modelclass.ResponseChatHistory;
import com.lg_project.modelclass.SocketManager;
import com.lg_project.modelclass.contractordetails.ArtistPic;
import com.lg_project.modelclass.contractordetails.ContractorDetailData;
import com.lg_project.modelclass.contractordetails.ResponseContractorDetails;
import com.lg_project.modelclass.notifymsg.OfflineMsgResponseData;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Chat extends Fragment {

    CircularImageView imageView;
    TextView textView, textonline;
    ImageView back, emoji, statusimg;
    LinearLayout rootLayout, l1;
    String room = "", activeCustomerSocketID = "", strOnlinestatus = "", name1 = "", chatINfoDate = "", from = "";
    private SessionManageent session;
    int idCustomer = 0, idContactor = 0, cust_id = 0 , useridContactor = 0;
    ArrayList<ChatModel> arrChatHistoryList = new ArrayList<>();
    ArrayList<ContractorActiveModel> arrAvailableContractor = new ArrayList<>();
    ChatAdapter chatAdapter;
    RecyclerView recycler_chat_view;
    FloatingActionButton send_msg;
    EmojiconEditText et_Message;
    SocketManager socketManager;
    private Socket mSocket;
    boolean isopen = false;
    String status;
    View root;
    public boolean isKeyBoardOpen = false;
    public boolean onlineStatus;
    Dialog optionDialog;
    ProgressBar progressBar;
    Sprite wave;

    ArrayList<ContractorActiveModel> arrActiveCustomerList = new ArrayList<>();
    private ViewTreeObserver.OnGlobalLayoutListener mKeyboardLayoutListener;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        return v;
    }

    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);





        String name = getArguments().getString("name");
        int image = getArguments().getInt("image");
        //change
        idContactor = getArguments().getInt("idCustomer");
        useridContactor = getArguments().getInt("useridCustomer");
        status = String.valueOf(getArguments().getBoolean("status"));
        from = getArguments().getString("from");
        //Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
        activeCustomerSocketID = getArguments().getString("activeCustomerSocketID");
        onlineStatus = getArguments().getBoolean("status");
        //change
        Log.e("idContactor", "" + idContactor);
        Log.e("onlinestatus", "" + onlineStatus);
        Log.e("activeCustomerSocketID", "" + activeCustomerSocketID);

        textView = (TextView) v.findViewById(R.id.namee);
        textView.setText(name);


        session = new SessionManageent(getContext());
        HashMap<String, String> user = session.getUserDetails();
        idCustomer = Integer.parseInt(user.get(SessionManageent.KEY_ID));
        cust_id = Integer.parseInt(user.get(SessionManageent.KEY_CUST_ID));
        name1 = user.get(SessionManageent.KEY_FIRSTNAME);
        Log.e("idCustomer", "" + idCustomer);

        //change
        room = String.valueOf(idCustomer) + useridContactor;
        Log.e("room", room);



        // tool.setVisibility(View.GONE);

        socketManager = (SocketManager)getActivity().getApplication();
        mSocket = socketManager.getSocket();
        mSocket.on("chat_message_ack", chatMessage);
        mSocket.on("notifyTyping", notifytyping);
        mSocket.on("notifyStopTyping", notifystoptyping);
        mSocket.on("all-contractors", allContractors);
        mSocket.connect();


        mSocket.emit("avail_contractor", "");

        if(from != null) {
            if (from.equals("fromPush")) {
                DetailsData(String.valueOf(idContactor));
            }
        }


        fetchVIewIDs(v);


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
/*
                            for(int b=0 ; b<arrAvailableContractor.size();b++){
                                if(idContactor == arrAvailableContractor.get(b).user){
                                    activeCustomerSocketID=arrAvailableContractor.get(b).id;
                                    onlineStatus=arrAvailableContractor.get(b).online;

                                }

                            }*/


                        for(int b=0 ; b<arrAvailableContractor.size();b++) {
                            if (useridContactor == arrAvailableContractor.get(b).user) {
                                if (arrAvailableContractor.get(b).online == true) {
                                    onlineStatus = true;
                                    strOnlinestatus = "Online";
                                    textonline.setText(strOnlinestatus);
                                    textonline.setTextColor(Color.parseColor("#6bd505"));
                                    statusimg.setImageDrawable(getResources().getDrawable(R.drawable.status));
                                    activeCustomerSocketID = arrAvailableContractor.get(b).id;
                                } else {
                                    onlineStatus = false;
                                    strOnlinestatus = "Offline";
                                    textonline.setText(strOnlinestatus);
                                    textonline.setTextColor(Color.parseColor("#8c8888"));
                                    statusimg.setImageDrawable(getResources().getDrawable(R.drawable.status_grey));
                                    activeCustomerSocketID = "";
                                }


                            }
                        }





                        //  AvailableData();

//                        filterActiveCustomerList();
//                    Log.e("allCustomer", array.toString());
                    }

                });
            }
        }
    };

    public void fetchVIewIDs(View v) {

        progressBar = v.findViewById(R.id.progress);
        wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        progressBar.setBackgroundResource(android.R.color.transparent);


        imageView = (CircularImageView) v.findViewById(R.id.image);
        back = v.findViewById(R.id.back);

        rootLayout = v.findViewById(R.id.lay_chat);
        l1 = v.findViewById(R.id.l1);
        recycler_chat_view = v.findViewById(R.id.recycler_chat_view);

        et_Message = v.findViewById(R.id.et_Message);
        send_msg = v.findViewById(R.id.submit_btn);
        statusimg = v.findViewById(R.id.status);
        textonline = (TextView) v.findViewById(R.id.txtonline);

        if (onlineStatus) {
            strOnlinestatus = "Online";
            textonline.setText(strOnlinestatus);
            textonline.setTextColor(Color.parseColor("#6bd505"));
            statusimg.setImageDrawable(getResources().getDrawable(R.drawable.status));

        } else {
            strOnlinestatus = "Offline";
            textonline.setText(strOnlinestatus);
            textonline.setTextColor(Color.parseColor("#8c8888"));
            statusimg.setImageDrawable(getResources().getDrawable(R.drawable.status_grey));
        }



        root = v.findViewById(R.id.lay_chat);
        emoji = (ImageView) v.findViewById(R.id.emoji_btn);

        et_Message.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isopen = true;
                Log.e("Keyboard", "open");
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("room", room);
                    //change
                    jsonObject.put("to_user", idContactor);
                    jsonObject.put("from_user", idCustomer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("json", jsonObject.toString());

                mSocket.emit("typing", jsonObject);
                isKeyBoardOpen = true;

                return false;
            }
        });


        // EmojiconEditText emojiconEditText = (EmojiconEditText)v.findViewById(R.id.et_Message);
        EmojIconActions emojIcon = new EmojIconActions(getContext(), root, et_Message, emoji);
        emojIcon.ShowEmojIcon();
     /*   emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emojIcon.setUseSystemEmoji(true);
            }
        });*/


        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
             //   chatHostory();
            }

            @Override
            public void onKeyboardClose() {

                if (isKeyBoardOpen) {
                    String last_msg = "", created_at = "";
                    for (int i = 0; i < arrChatHistoryList.size(); i++) {
                        last_msg = arrChatHistoryList.get(i).msg;
                        created_at = arrChatHistoryList.get(i).createdAt;

                    }
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("to_user", idContactor);
                        //change
                        jsonObject.put("from_user", idCustomer);
                        jsonObject.put("room", room);
                        jsonObject.put("msg", last_msg);
                        jsonObject.put("createdAt", created_at);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("jsonStopTyping", jsonObject.toString());
                    mSocket.emit("stopTyping", jsonObject);
                    isKeyBoardOpen = false;
                }
                //  mSocket.emit("stopTyping",jsonObject2);
            }
        });


        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        recycler_chat_view.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        Log.d("RESTST", String.valueOf(idContactor));
        chatAdapter = new ChatAdapter(getContext(), arrChatHistoryList, idCustomer);
        recycler_chat_view.setAdapter(chatAdapter); // set the Adapter to RecyclerView

        listeners();
        chatHostory();
        checkChatInfo();


    }

    public void listeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootLayout.setVisibility(View.GONE);
                l1.setVisibility(View.GONE);

//                Intent intent = new Intent(getContext(), MainActivity2.class);
//                startActivity(intent);

//                Messages fragment2 = new Messages();
//                Bundle bundle = new Bundle();
//                fragment2.setArguments(bundle);
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.lay_chat, fragment2);
//                fragmentTransaction.commit();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();

            }
        });

        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_Message.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "" + getResources().getString(R.string.no_empty_msg), Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, String> socket = session.getSocketID();
                    String socketIDContactor = socket.get(session.KEY_SOCKET_ID);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("to", activeCustomerSocketID);
                        //chnage
                        jsonObject.put("to_user", useridContactor);
                        jsonObject.put("from", socketIDContactor);
                        jsonObject.put("from_user", idCustomer);
                        jsonObject.put("room", room);
                        jsonObject.put("msg", et_Message.getText().toString());
                        jsonObject.put("sender", "customer");
                        jsonObject.put("receiver", "artist");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("json", jsonObject.toString());

                    mSocket.emit("chat_message_artist", jsonObject);
                    if (!onlineStatus) {
                        Log.d("USER OFFLINE", "OFFLINE");
                        offlinemsg(et_Message.getText().toString());
                    }

                    et_Message.clearFocus();
                    et_Message.setText("");
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


                }
            }
        });
    }

    public void chatInfoDialog() {
        optionDialog = new Dialog(getContext());
        optionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        optionDialog.setContentView(R.layout.chat_info_dialog);
        optionDialog.setCancelable(false);
        Window window = optionDialog.getWindow();
        window.setLayout(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        EditText editText = (EditText) optionDialog.findViewById(R.id.amount);
        Button b1 = (Button) optionDialog.findViewById(R.id.btn_submit);
        ImageView close = (ImageView) optionDialog.findViewById(R.id.close);
        Button btn_cancel = (Button) optionDialog.findViewById(R.id.btn_cancel);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spendCredit(optionDialog);

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootLayout.setVisibility(View.GONE);
                l1.setVisibility(View.GONE);

//                Messages fragment2 = new Messages();
//                Bundle bundle = new Bundle();
//                fragment2.setArguments(bundle);
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.lay_chat, fragment2);
//                fragmentTransaction.commit();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                optionDialog.dismiss();
            }
        });

        optionDialog.show();
    }

    public void chatHostory() {
        /** Create handle for the RetrofitInstance interface*/
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = RetrofitInstance.getClient(getContext());


        JSONObject jsonObject = new JSONObject();
        try {
            //chnage
            jsonObject.put("to_user", useridContactor);//user id
            jsonObject.put("from_user", idCustomer);//user id
            jsonObject.put("room", room);//cust+contractor user id
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        if (retrofit != null) {

        Log.e("ObjArraychat", jsonObject.toString());
        // RequestBody body = RequestBody.create(MediaType.parse("application/json"), " {\r\n    \"to_user\":\"416\",\r\n    \"from_user\":\"412\",\r\n    \"room\":\"416412\"\r\n}");
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Api apiService = retrofit.create(Api.class);
        Call<ResponseChatHistory> call = apiService.chatHistory("chats", body);//socket url/chats
//        }
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
//        Api api = RetrofitInstance.getSocketRetrofitInstance().create(Api.class);
//        Call<ResponseChatHistory> call = api.chatHistory(body);
        call.enqueue(new Callback<ResponseChatHistory>() {
            @Override
            public void onResponse(Call<ResponseChatHistory> call, Response<ResponseChatHistory> response) {

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

                    if(response.isSuccessful()) {
                        arrChatHistoryList.clear();
                        List<ChatModel> list = response.body().getData();
                        for (int i = 0; i < list.size(); i++) {
                            ChatModel model = new ChatModel();
                            model._id = list.get(i)._id;
                            model.room = list.get(i).room;
                            model.from_user = list.get(i).from_user;
                            model.to = list.get(i).to;
                            model.to_user = list.get(i).to_user;
                            model.from = list.get(i).from;
                            model.msg = list.get(i).msg;
                            model.reciever = list.get(i).reciever;
                            model.createdAt = list.get(i).createdAt;
                            model.updatedAt = list.get(i).updatedAt;
                            model.__v = list.get(i).__v;
                            arrChatHistoryList.add(model);

                        }


                        chatAdapter.notifyDataSetChanged();
                        recycler_chat_view.scrollToPosition(chatAdapter.getItemCount() - 1);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        recycler_chat_view.smoothScrollToPosition(arrChatHistoryList.size());
//                    }
//                }, 0);

                    }
                } catch (Exception e) {
                    Log.d("Execption Chat", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseChatHistory> call, Throwable t) {
//                Log.d("ERRor", t.getMessage());
                //Toast.makeText(Login.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkChatInfo() {
        /** Create handle for the RetrofitInstance interface*/

        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cust_id", cust_id);//user id
            jsonObject.put("artrist_id", idContactor);//user id
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("ObjArray", jsonObject.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Api apiService = retrofit.create(Api.class);
        Call<ResponseBody> call = apiService.chatInfo("credit/chat/info", body);//socket url/chats
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                arrChatHistoryList.clear();
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
                        Log.e(" List", responseObject.toString());
                        String successstatus = responseObject.optString("success");
                        String message = responseObject.optString("message");
                        if (successstatus.equalsIgnoreCase("true")) {

                            JSONObject data = responseObject.getJSONObject("data");
                            if(data.has("date") && !data.isNull("date")){
                                chatINfoDate = data.getString("date");
                                SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                Date msgDate = null;
                                try {
                                    msgDate = sp.parse(chatINfoDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                sp = new SimpleDateFormat("yyyy-MM-dd");
                                String strMsgDate = sp.format(msgDate);

                                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date startDate = null;   // initialize start date
                                try {
                                    startDate = myFormat.parse(strMsgDate);

                                    Date todayDate = Calendar.getInstance().getTime();
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                    String currentDate = formatter.format(todayDate);
                                    Date endDate   = myFormat.parse("2021-03-30"); // initialize  end date

                                    long duration  = endDate.getTime() - startDate.getTime();
                                    long days =TimeUnit.DAYS.convert(duration, TimeUnit.MILLISECONDS);
                                    if(days > 30){
                                        chatINfoDate="";
                                        Log.d("Days: " ,""+ TimeUnit.DAYS.convert(duration, TimeUnit.MILLISECONDS));
                                    }



                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            if (chatINfoDate.equalsIgnoreCase("") ) {
                                chatInfoDialog();
                            }

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
    public void spendCredit(Dialog optionDialog) {
        /** Create handle for the RetrofitInstance interface*/

        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", cust_id);//user id
            jsonObject.put("beneficiary", idContactor);//user id
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("CreditObjArray", jsonObject.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Api apiService = retrofit.create(Api.class);
        Call<ResponseBody> call = apiService.chatInfo("credit/spend", body);//socket url/chats
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.d("CREDITINFO", String.valueOf(response.body()));
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
                        Log.e(" List", responseObject.toString());
                        String successstatus = responseObject.optString("success");
                        String message = responseObject.optString("message");
                        if (successstatus.equalsIgnoreCase("true")) {
                            optionDialog.dismiss();
                            JSONObject data = responseObject.getJSONObject("data");
                            if(data.has("credit") && !data.isNull("credit")){
                                String credit=data.getString("credit");
                            }

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


    private Emitter.Listener chatMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject array = (JSONObject) args[0];
                        Log.e("chatSocket", array.toString());
                        try {
                            ChatModel model = new ChatModel();
                            model.from_user = array.getInt("from_user");
                            model.to_user = array.getInt("to_user");
                            model.msg = array.getString("msg");
                            model.createdAt = array.getString("createdAt");
//                            arrChatHistoryList.add(model);
//                            layoutManager.setStackFromEnd(true);
//                            recycler_chat_view.setLayoutManager(layoutManager);
                            chatAdapter.addItem(model);
                            recycler_chat_view.scrollToPosition(chatAdapter.getItemCount() - 1);

//                        Object array = (Object) args[0];
//                        Log.e("chatSocket", array.toString());
//                        chatHostory();
                            //  chatAdapter.notifyDataSetChanged();

                        } catch (Exception e) {

                        }
                    }

                });
            }
        }
    };

    private Emitter.Listener notifytyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {


            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  Object array = (Object) args[0];
                        //  Log.d("notifytyping", array.toString());
                        textonline.setText("typing...");
                        textonline.setTextColor(ContextCompat.getColor(getContext(), R.color.drawertextcolor));
                        Log.d("notifytyping", "typing");

                    }
                });
            }
        }
    };

    private Emitter.Listener notifystoptyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            // Log.d("notifytyping","typing");
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  Object array = (Object) args[0];
                        //  Log.d("notifystoptyping", array.toString());
                        if (!onlineStatus) {
                            textonline.setText("Offline");
                            textonline.setTextColor(ContextCompat.getColor(getContext(), R.color.grey));
                        } else {
                            textonline.setText("Online");
                            textonline.setTextColor(ContextCompat.getColor(getContext(), R.color.main_green_color));
                        }

                    }
                });
            }
        }
    };

    public void offlinemsg(String message) {
        /** Create handle for the RetrofitInstance interface*/
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<OfflineMsgResponseData> call = api.notifymsg(ApiJsonMap(message));

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<OfflineMsgResponseData>() {
            @Override
            public void onResponse(Call<OfflineMsgResponseData> call, Response<OfflineMsgResponseData> response) {

                //  Log.d("SENDOFFLINE", String.valueOf(response.body().getMessage()));
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
                            Log.e("SUCCESS", String.valueOf(response.body().getSuccess()));
                            // Toast.makeText(getContext(), "Successfully submit!!", Toast.LENGTH_SHORT).show();
                            // Creating user login session
                            // For testing i am stroing name, email as follow
                            // Use user real data
                            //  session.createLoginSession(password, username,id);

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
            public void onFailure(Call<OfflineMsgResponseData> call, Throwable t) {
                Log.d("ERRor", t.getMessage());
                //Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private JsonObject ApiJsonMap(String message) {

        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("id", cust_id);
            jsonObj_.put("name", name1);
            jsonObj_.put("user", idCustomer);//user id
            jsonObj_.put("desitination", "ARTIST");//user id
            jsonObj_.put("artist_user_id", useridContactor);//contractor user id
            jsonObj_.put("msg", message);


            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());

            //print parameter
            Log.e("MY gson.JSON:  ", "AS PARAMETER  " + gsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gsonObject;
    }


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

                String name = list.get(0).getStageName();
               // onlineStatus = Boolean.parseBoolean(list.get(0).getStatus());
                textView.setText(name);
             /*   if (onlineStatus) {
                    strOnlinestatus = "Online";
                    textonline.setText(strOnlinestatus);
                    textonline.setTextColor(Color.parseColor("#6bd505"));
                    statusimg.setImageDrawable(getResources().getDrawable(R.drawable.status));

                } else {
                    strOnlinestatus = "Offline";
                    textonline.setText(strOnlinestatus);
                    textonline.setTextColor(Color.parseColor("#8c8888"));
                    statusimg.setImageDrawable(getResources().getDrawable(R.drawable.status_grey));
                }

                room = String.valueOf(idCustomer) + useridContactor;*/



            }

            @Override
            public void onFailure(Call<ResponseContractorDetails> call, Throwable t) {
                //  Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();

            }
        });

    }


}