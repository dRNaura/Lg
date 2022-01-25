package com.lg_project.fragment;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.lg_project.EighthLauncherAlias;
import com.lg_project.FifthLauncherAlias;
import com.lg_project.FourLauncherAlias;
import com.lg_project.LgLogoAlias;
import com.lg_project.SeventhLauncherAlias;
import com.lg_project.SixthLauncherAlias;
import com.lg_project.ThirdLauncherAlias;
import com.lg_project.activity.MainActivity2;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.FirstLauncherAlias;
import com.lg_project.R;
import com.lg_project.SecondLauncherAlias;
import com.lg_project.Session.SessionManageent;
import com.lg_project.modelclass.AllStealthModel;
import com.lg_project.modelclass.FavResponseData;
import com.lg_project.modelclass.notifymsg.OfflineMsgResponseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StleathMode extends Fragment {

    LinearLayout back;
    public SwitchCompat switch_,switch_notification;
    SessionManageent session;
    String deviceToken="",user_type="",userID="",  stealth_msg="";
    RelativeLayout rl_alternate_message;
    TextView t8;
    public boolean isAppDisguiseChecked=false;
    ImageView football,contactustodate,weather,clock,callcontact,password,inbox,task;
    PackageManager packageManager;
    Handler mHandler;
    ProgressDialog mProgressBar;
    ArrayList<AllStealthModel> arrAllStealthList=new ArrayList<>();
    int stealth_code=0;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        MainActivity2.l1.setVisibility(View.VISIBLE);
        MainActivity2.heading.setText(R.string.stealthmode);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_stleath_mode, container, false);
        return v;
    }

    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        session = new SessionManageent(getContext());

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // email
        userID = user.get(SessionManageent.KEY_ID);
        user_type = user.get(SessionManageent.KEY_USER_TYPE);
        Log.e("ERER", String.valueOf(userID));
        HashMap<String, String> setting = session.getPreferedSetting();
        String stealth_mode=setting.get(SessionManageent.KEY_STEALTH_MODE);
         stealth_msg=setting.get(SessionManageent.KEY_STEALTH_MSG);
         stealth_code= Integer.parseInt(setting.get(SessionManageent.KEY_STEALTH_CODE));
        Log.d("STEALTHMODE",stealth_mode);
        Log.d("STEALTCODE",""+stealth_code);


        FirebaseApp.initializeApp(getContext());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel("MyNotifications","MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
        try{
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    deviceToken=task.getResult().getToken();
                    // Get new Instance ID token
                    Log.e("firebase_token",deviceToken);
                    // Log and toast

                }
            });
        }catch (Exception e){
            Log.e("Firebase Exception", e.toString());
        }


        back=v.findViewById(R.id.back);
        switch_=v.findViewById(R.id.switch_);
        switch_notification=v.findViewById(R.id.switch_notification);
        rl_alternate_message=v.findViewById(R.id.rl_alternate_message);
        t8=v.findViewById(R.id.t8);
        football=(ImageView)v.findViewById(R.id.appicon_football);
        contactustodate=(ImageView)v.findViewById(R.id.contactustodate);
        weather=(ImageView)v.findViewById(R.id.weather);
        clock=(ImageView)v.findViewById(R.id.clock);
        callcontact=(ImageView)v.findViewById(R.id.callcontact);
        password=(ImageView)v.findViewById(R.id.password);
        inbox=(ImageView)v.findViewById(R.id.inbox);
        task=(ImageView)v.findViewById(R.id.task);

        t8.setText(stealth_msg);

        if(stealth_mode.equalsIgnoreCase("1")){
            isAppDisguiseChecked=true;
            switch_.setChecked(true);
            switch_notification.setClickable(true);
        }else{
            switch_.setChecked(false);
            isAppDisguiseChecked=false;
            switch_notification.setChecked(false);
            switch_notification.setClickable(false);
            football.setClickable(false);
            contactustodate.setClickable(false);
            weather.setClickable(false);
            clock.setClickable(false);
            callcontact.setClickable(false);
            password.setClickable(false);
            inbox.setClickable(false);
            task.setClickable(false);
        }
//        switch_.getTrackDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.red), PorterDuff.Mode.SRC_IN);
//        switch_notification.getTrackDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.red), PorterDuff.Mode.SRC_IN);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), MainActivity2.class);
                startActivity(intent);
                onDestroy();
            }
        });
        switch_.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAppDisguiseChecked=isChecked;
                Log.d("ISAPP", String.valueOf(isAppDisguiseChecked));
                if(isChecked){
                    switch_notification.setClickable(true);
                    switch_notification.setBackgroundResource(R.drawable.btn_shape);
                    football.setClickable(true);
                    contactustodate.setClickable(true);
                    weather.setClickable(true);
                    clock.setClickable(true);
                    callcontact.setClickable(true);
                    password.setClickable(true);
                    inbox.setClickable(true);
                    task.setClickable(true);
                    configure_stealth(1);
                }else{
                    switch_notification.setClickable(false);
                    switch_notification.setBackgroundResource(R.drawable.btn_shape_grey);
                    football.setClickable(false);
                    contactustodate.setClickable(false);
                    weather.setClickable(false);
                    clock.setClickable(false);
                    callcontact.setClickable(false);
                    password.setClickable(false);
                    inbox.setClickable(false);
                    task.setClickable(false);
                    configure_stealth(0);
                }
            }
        });

        switch_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isAppDisguiseChecked){
                    if(isChecked){
                        updateDevice();
                    }else{
                        deleteDevice();
                    }
                }else{

                }

            }
        });

        rl_alternate_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAppDisguiseChecked){
                    AllStealthFragment menu=new AllStealthFragment() ;
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_content,menu).commit();
                    getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                }else{

                }

            }
        });

        packageManager=getContext().getPackageManager();


        if(isAppDisguiseChecked){
            football.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog optionDialog = new Dialog(getContext());
                    optionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    optionDialog.setContentView(R.layout.change_icon_alert);
                    optionDialog.setCancelable(true);
                    Window window = optionDialog.getWindow();
                    window.setLayout(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.CENTER);
                    ImageView image_profile = optionDialog.findViewById(R.id.imagealert);
                    Glide.with(getContext()).load(football.getDrawable()).placeholder(R.drawable.lg_logo).into(image_profile);

                    Button ok=optionDialog.findViewById(R.id.ok_submit);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            optionDialog.dismiss();

                            mHandler=new Handler();
                            mProgressBar= new ProgressDialog(getContext());
                            mProgressBar.setMax(100);
                            mProgressBar.setCancelable(false);
                            mProgressBar.setMessage("Please Wait.....!!!!");
                            mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            try {
                                mProgressBar.show();
                            }
                            catch(Exception e)
                            {
                                Log.d("EXCEPTION",e.getMessage());
                            }
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), FirstLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), SecondLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), LgLogoAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), ThirdLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), FourLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), FifthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), SixthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), SeventhLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), EighthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i <= 100; i++) {
                                        final int currentProgressCount = i;
                                        try {
                                            Thread.sleep(50);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        //Update the value background thread to UI thread
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgressBar.setProgress(currentProgressCount);
                                                if(currentProgressCount==100)
                                                {
                                                    mProgressBar.dismiss();
                                                    mProgressBar = null;
                                                    getActivity().finishAffinity();
                                                    Toast.makeText(getContext(), "App icon changed successfully !!",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            }).start();
                        }
                    });

                    optionDialog.show();

                }
            });

        }



          if(isAppDisguiseChecked){
              contactustodate.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                      final Dialog optionDialog = new Dialog(getContext());
                      optionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                      optionDialog.setContentView(R.layout.change_icon_alert);
                      optionDialog.setCancelable(true);
                      Window window = optionDialog.getWindow();
                      window.setLayout(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.WRAP_CONTENT);
                      window.setGravity(Gravity.CENTER);
                      ImageView image_profile = optionDialog.findViewById(R.id.imagealert);
                      Glide.with(getContext()).load(contactustodate.getDrawable()).placeholder(R.drawable.lg_logo).into(image_profile);

                      Button ok=optionDialog.findViewById(R.id.ok_submit);
                      ok.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              packageManager.setComponentEnabledSetting(new ComponentName(getContext(), FirstLauncherAlias.class),
                                      PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                              packageManager.setComponentEnabledSetting(new ComponentName(getContext(), SecondLauncherAlias.class),
                                      PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
                              packageManager.setComponentEnabledSetting(new ComponentName(getContext(), LgLogoAlias.class),
                                      PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                              packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), ThirdLauncherAlias.class),
                                      PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                              packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), FourLauncherAlias.class),
                                      PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                              packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), FifthLauncherAlias.class),
                                      PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                              packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), SixthLauncherAlias.class),
                                      PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                              packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), SeventhLauncherAlias.class),
                                      PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                              packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), EighthLauncherAlias.class),
                                      PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                              mHandler=new Handler();
                              mProgressBar= new ProgressDialog(getContext());
                              mProgressBar.setMax(100);
                              mProgressBar.setCancelable(false);
                              mProgressBar.setMessage("Please Wait.....!!!!");
                              mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                              try {
                                  mProgressBar.show();
                              }
                              catch(Exception e)
                              {
                                  Log.d("EXCEPTION",e.getMessage());
                              }
                              new Thread(new Runnable() {
                                  @Override
                                  public void run() {
                                      for (int i = 0; i <= 100; i++) {
                                          final int currentProgressCount = i;
                                          try {
                                              Thread.sleep(50);
                                          } catch (InterruptedException e) {
                                              e.printStackTrace();
                                          }
                                          //Update the value background thread to UI thread
                                          mHandler.post(new Runnable() {
                                              @Override
                                              public void run() {
                                                  mProgressBar.setProgress(currentProgressCount);
                                                  if(currentProgressCount==100)
                                                  {
                                                      optionDialog.dismiss();
                                                      mProgressBar.dismiss();
                                                      getActivity().finishAffinity();
                                                      Toast.makeText(getContext(), "App icon changed successfully !!",Toast.LENGTH_SHORT).show();
                                                  }
                                              }
                                          });
                                      }
                                  }
                              }).start();
                          }
                      });

                      optionDialog.show();

                  }
              });

          }


        if(isAppDisguiseChecked){
            weather.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog optionDialog = new Dialog(getContext());
                    optionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    optionDialog.setContentView(R.layout.change_icon_alert);
                    optionDialog.setCancelable(true);
                    Window window = optionDialog.getWindow();
                    window.setLayout(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.CENTER);
                    ImageView image_profile = optionDialog.findViewById(R.id.imagealert);
                    Glide.with(getContext()).load(weather.getDrawable()).placeholder(R.drawable.lg_logo).into(image_profile);

                    Button ok=optionDialog.findViewById(R.id.ok_submit);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            packageManager.setComponentEnabledSetting(new ComponentName(getContext(), FirstLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getContext(), SecondLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getContext(), LgLogoAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), ThirdLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), FourLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), FifthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), SixthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), SeventhLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), EighthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            mHandler=new Handler();
                            mProgressBar= new ProgressDialog(getContext());
                            mProgressBar.setMax(100);
                            mProgressBar.setCancelable(false);
                            mProgressBar.setMessage("Please Wait.....!!!!");
                            mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            try {
                                mProgressBar.show();
                            }
                            catch(Exception e)
                            {
                                Log.d("EXCEPTION",e.getMessage());
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i <= 100; i++) {
                                        final int currentProgressCount = i;
                                        try {
                                            Thread.sleep(50);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        //Update the value background thread to UI thread
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgressBar.setProgress(currentProgressCount);
                                                if(currentProgressCount==100)
                                                {
                                                    optionDialog.dismiss();
                                                    mProgressBar.dismiss();
                                                    getActivity().finishAffinity();
                                                    Toast.makeText(getContext(), "App icon changed successfully !!",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            }).start();
                        }
                    });

                    optionDialog.show();

                }
            });

        }

        if(isAppDisguiseChecked){
            clock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog optionDialog = new Dialog(getContext());
                    optionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    optionDialog.setContentView(R.layout.change_icon_alert);
                    optionDialog.setCancelable(true);
                    Window window = optionDialog.getWindow();
                    window.setLayout(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.CENTER);
                    ImageView image_profile = optionDialog.findViewById(R.id.imagealert);
                    Glide.with(getContext()).load(clock.getDrawable()).placeholder(R.drawable.lg_logo).into(image_profile);

                    Button ok=optionDialog.findViewById(R.id.ok_submit);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            packageManager.setComponentEnabledSetting(new ComponentName(getContext(), FirstLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getContext(), SecondLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getContext(), LgLogoAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), ThirdLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), FourLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), FifthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), SixthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), SeventhLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), EighthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            mHandler=new Handler();
                            mProgressBar= new ProgressDialog(getContext());
                            mProgressBar.setMax(100);
                            mProgressBar.setCancelable(false);
                            mProgressBar.setMessage("Please Wait.....!!!!");
                            mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            try {
                                mProgressBar.show();
                            }
                            catch(Exception e)
                            {
                                Log.d("EXCEPTION",e.getMessage());
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i <= 100; i++) {
                                        final int currentProgressCount = i;
                                        try {
                                            Thread.sleep(50);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        //Update the value background thread to UI thread
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgressBar.setProgress(currentProgressCount);
                                                if(currentProgressCount==100)
                                                {
                                                    optionDialog.dismiss();
                                                    mProgressBar.dismiss();
                                                    getActivity().finishAffinity();
                                                    Toast.makeText(getContext(), "App icon changed successfully !!",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            }).start();
                        }
                    });

                    optionDialog.show();

                }
            });

        }

        if(isAppDisguiseChecked){
            callcontact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog optionDialog = new Dialog(getContext());
                    optionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    optionDialog.setContentView(R.layout.change_icon_alert);
                    optionDialog.setCancelable(true);
                    Window window = optionDialog.getWindow();
                    window.setLayout(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.CENTER);
                    ImageView image_profile = optionDialog.findViewById(R.id.imagealert);
                    Glide.with(getContext()).load(callcontact.getDrawable()).placeholder(R.drawable.lg_logo).into(image_profile);

                    Button ok=optionDialog.findViewById(R.id.ok_submit);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            packageManager.setComponentEnabledSetting(new ComponentName(getContext(), FirstLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getContext(), SecondLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getContext(), LgLogoAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), ThirdLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), FourLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), FifthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), SixthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), SeventhLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), EighthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            mHandler=new Handler();
                            mProgressBar= new ProgressDialog(getContext());
                            mProgressBar.setMax(100);
                            mProgressBar.setCancelable(false);
                            mProgressBar.setMessage("Please Wait.....!!!!");
                            mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            try {
                                mProgressBar.show();
                            }
                            catch(Exception e)
                            {
                                Log.d("EXCEPTION",e.getMessage());
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i <= 100; i++) {
                                        final int currentProgressCount = i;
                                        try {
                                            Thread.sleep(50);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        //Update the value background thread to UI thread
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgressBar.setProgress(currentProgressCount);
                                                if(currentProgressCount==100)
                                                {
                                                    optionDialog.dismiss();
                                                    mProgressBar.dismiss();
                                                    getActivity().finishAffinity();
                                                    Toast.makeText(getContext(), "App icon changed successfully !!",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            }).start();
                        }
                    });

                    optionDialog.show();

                }
            });

        }

        if(isAppDisguiseChecked){
            password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog optionDialog = new Dialog(getContext());
                    optionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    optionDialog.setContentView(R.layout.change_icon_alert);
                    optionDialog.setCancelable(true);
                    Window window = optionDialog.getWindow();
                    window.setLayout(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.CENTER);
                    ImageView image_profile = optionDialog.findViewById(R.id.imagealert);
                    Glide.with(getContext()).load(password.getDrawable()).placeholder(R.drawable.lg_logo).into(image_profile);

                    Button ok=optionDialog.findViewById(R.id.ok_submit);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            packageManager.setComponentEnabledSetting(new ComponentName(getContext(), FirstLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getContext(), SecondLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getContext(), LgLogoAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), ThirdLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), FourLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), FifthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), SixthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), SeventhLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), EighthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            mHandler=new Handler();
                            mProgressBar= new ProgressDialog(getContext());
                            mProgressBar.setMax(100);
                            mProgressBar.setCancelable(false);
                            mProgressBar.setMessage("Please Wait.....!!!!");
                            mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            try {
                                mProgressBar.show();
                            }
                            catch(Exception e)
                            {
                                Log.d("EXCEPTION",e.getMessage());
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i <= 100; i++) {
                                        final int currentProgressCount = i;
                                        try {
                                            Thread.sleep(50);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        //Update the value background thread to UI thread
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgressBar.setProgress(currentProgressCount);
                                                if(currentProgressCount==100)
                                                {
                                                    optionDialog.dismiss();
                                                    mProgressBar.dismiss();
                                                    getActivity().finishAffinity();
                                                    Toast.makeText(getContext(), "App icon changed successfully !!",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            }).start();
                        }
                    });

                    optionDialog.show();

                }
            });

        }

        if(isAppDisguiseChecked){
            inbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog optionDialog = new Dialog(getContext());
                    optionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    optionDialog.setContentView(R.layout.change_icon_alert);
                    optionDialog.setCancelable(true);
                    Window window = optionDialog.getWindow();
                    window.setLayout(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.CENTER);
                    ImageView image_profile = optionDialog.findViewById(R.id.imagealert);
                    Glide.with(getContext()).load(inbox.getDrawable()).placeholder(R.drawable.lg_logo).into(image_profile);

                    Button ok=optionDialog.findViewById(R.id.ok_submit);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            packageManager.setComponentEnabledSetting(new ComponentName(getContext(), FirstLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getContext(), SecondLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getContext(), LgLogoAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), ThirdLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), FourLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), FifthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), SixthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), SeventhLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), EighthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            mHandler=new Handler();
                            mProgressBar= new ProgressDialog(getContext());
                            mProgressBar.setMax(100);
                            mProgressBar.setCancelable(false);
                            mProgressBar.setMessage("Please Wait.....!!!!");
                            mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            try {
                                mProgressBar.show();
                            }
                            catch(Exception e)
                            {
                                Log.d("EXCEPTION",e.getMessage());
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i <= 100; i++) {
                                        final int currentProgressCount = i;
                                        try {
                                            Thread.sleep(50);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        //Update the value background thread to UI thread
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgressBar.setProgress(currentProgressCount);
                                                if(currentProgressCount==100)
                                                {
                                                    optionDialog.dismiss();
                                                    mProgressBar.dismiss();
                                                    getActivity().finishAffinity();
                                                    Toast.makeText(getContext(), "App icon changed successfully !!",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            }).start();
                        }
                    });

                    optionDialog.show();

                }
            });

        }

        if(isAppDisguiseChecked){
            task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog optionDialog = new Dialog(getContext());
                    optionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    optionDialog.setContentView(R.layout.change_icon_alert);
                    optionDialog.setCancelable(true);
                    Window window = optionDialog.getWindow();
                    window.setLayout(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.WRAP_CONTENT);
                    window.setGravity(Gravity.CENTER);
                    ImageView image_profile = optionDialog.findViewById(R.id.imagealert);
                    Glide.with(getContext()).load(task.getDrawable()).placeholder(R.drawable.lg_logo).into(image_profile);

                    Button ok=optionDialog.findViewById(R.id.ok_submit);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            packageManager.setComponentEnabledSetting(new ComponentName(getContext(), FirstLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getContext(), SecondLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getContext(), LgLogoAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), ThirdLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), FourLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), FifthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), SixthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), SeventhLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                            packageManager.setComponentEnabledSetting(new ComponentName(getActivity(), EighthLauncherAlias.class),
                                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
                            mHandler=new Handler();
                            mProgressBar= new ProgressDialog(getContext());
                            mProgressBar.setMax(100);
                            mProgressBar.setCancelable(false);
                            mProgressBar.setMessage("Please Wait.....!!!!");
                            mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            try {
                                mProgressBar.show();
                            }
                            catch(Exception e)
                            {
                                Log.d("EXCEPTION",e.getMessage());
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i <= 100; i++) {
                                        final int currentProgressCount = i;
                                        try {
                                            Thread.sleep(50);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        //Update the value background thread to UI thread
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mProgressBar.setProgress(currentProgressCount);
                                                if(currentProgressCount==100)
                                                {
                                                    optionDialog.dismiss();
                                                    mProgressBar.dismiss();
                                                    getActivity().finishAffinity();
                                                    Toast.makeText(getContext(), "App icon changed successfully !!",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            }).start();
                        }
                    });

                    optionDialog.show();

                }
            });

        }










        getAllStealth();
    }
    public void updateDevice() {
        /** Create handle for the RetrofitInstance interface*/
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", userID);
            jsonObject.put("deviceToken",deviceToken);
            jsonObject.put("platform","android");
            jsonObject.put("user_type",2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Api api = retrofit.create(Api.class);
        Call<OfflineMsgResponseData> call = api.updateDevice(body);
        Log.e("URL Called", call.request().url() + "");
        call.enqueue(new Callback<OfflineMsgResponseData>() {
            @Override
            public void onResponse(Call<OfflineMsgResponseData> call,Response<OfflineMsgResponseData> response) {

                Log.e("SUCCESS", String.valueOf(response.body()));
                // Log.d("SUCCESSLOG", String.valueOf(response.body().getSuccess()));
                String errorbody = "", errorCode = "";
                try {
                    if (response.errorBody() != null) {
                        errorbody = response.errorBody().string();
                        JSONObject responseObject = new JSONObject(errorbody);
                        errorCode = responseObject.getString("message");
                        session.logoutUser();
                        Toast.makeText(getContext(), "" + getResources().getString(R.string.session), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

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
            public void onFailure(Call<OfflineMsgResponseData> call, Throwable t) {
                Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteDevice() {
        /** Create handle for the RetrofitInstance interface*/
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", userID);
            jsonObject.put("platform","android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Api api = retrofit.create(Api.class);
        Call<FavResponseData> call = api.deleteDevice(body);
        Log.e("URL Called", call.request().url() + "");

        call.enqueue(new Callback<FavResponseData>() {
            @Override
            public void onResponse(Call<FavResponseData> call,Response<FavResponseData> response) {

                Log.e("SUCCESS", String.valueOf(response.body()));

                String errorbody="",errorCode="";
                try {
                    if (response.errorBody() != null) {
                        errorbody = response.errorBody().string();
                        JSONObject responseObject = new JSONObject(errorbody);
                        errorCode = responseObject.getString("message");
                        session.logoutUser();
                        Toast.makeText(getContext(), "" + getResources().getString(R.string.session), Toast.LENGTH_SHORT).show();
                        return;
                    }
                if (response.isSuccessful()) {
                    if(response.body().getSuccess())
                    {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();

                }
            }catch (Exception e) {

                }
            }


            @Override
            public void onFailure(Call<FavResponseData> call, Throwable t) {
                Log.e("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void configure_stealth(int stealth_mode) {
        /** Create handle for the RetrofitInstance interface*/

        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        JSONObject jsonObject = new JSONObject();
        try {
            //chnage
            jsonObject.put("user", userID);//user id
            jsonObject.put("app_icon", "test");//user id
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
                        errorCode = responseObject.getString("message");
                        session.logoutUser();
                        Toast.makeText(getContext(), "" + getResources().getString(R.string.session), Toast.LENGTH_SHORT).show();
                        return;

                    }

                    if (response.body() != null) {
                        String respo = response.body().string();
                        JSONObject responseObject = new JSONObject(respo);
                        Log.e(" List", responseObject.toString());
                        String successstatus = responseObject.optString("success");
                        String message = responseObject.optString("message");

                        if (successstatus.equalsIgnoreCase("true")) {

                            HashMap<String, String> setting = session.getPreferedSetting();
                            int stealth_code= Integer.parseInt(setting.get(SessionManageent.KEY_STEALTH_CODE));
                            String app_icon=setting.get(SessionManageent.KEY_APP_ICON);

                            Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                            session.preferedSetting(app_icon,stealth_code,stealth_mode,"");
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
//                            sessionManageent.logoutUser();
//                            Toast.makeText(getContext(), "" + getResources().getString(R.string.session), Toast.LENGTH_SHORT).show();
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
                                    t8.setText( model.title);
                                }
                                arrAllStealthList.add(model);
                            }
//                            allStealthAdapter.notifyDataSetChanged();



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