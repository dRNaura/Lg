package com.lg_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.lg_project.activity.EnterPinActivity;
import com.lg_project.activity.Login;
import com.lg_project.activity.MainActivity2;
import com.lg_project.Session.SessionManageent;

import java.util.HashMap;

public class Splash extends AppCompatActivity {
    String id="",user="",from="",action="";
    SessionManageent session;
    public static final String MainPP_SP = "MainPP_data";
    Context mContext = this;
    private static final int REQUEST= 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        session = new SessionManageent(getApplicationContext());
        Intent intent = getIntent();
        if( intent != null) {
            id = intent.getStringExtra("id");
            user = intent.getStringExtra("user");
            action = intent.getStringExtra("action");
            from = intent.getStringExtra("from");
        }
/*
        // Session class instance
        session = new SessionManageent(getApplicationContext());
        Intent intent = getIntent();
        if( intent != null) {
            id = intent.getStringExtra("id");
            user = intent.getStringExtra("user");
            action = intent.getStringExtra("action");
            from = intent.getStringExtra("from");
        }

        Thread thread=new Thread()
        {
            public void run()
            {
                try {
                    Thread.sleep(3000);
                }
                catch (Exception e)
                {

                }
                finally {
                    boolean pinAsked = session.isPinAsked();
                    boolean isLoggedIn = session.isLoggedIn();
                    Log.e("pinAsked", String.valueOf(pinAsked));
                    Log.e("isLoggedIn", String.valueOf(pinAsked));

                    if(!isLoggedIn){
                        Intent i = new Intent(Splash.this, Login.class);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        Splash.this.startActivity(i);
                    }
                    else{
                        if(pinAsked){
                            Intent i = new Intent(Splash.this, EnterPinActivity.class);
                            i.putExtra("email","");
                            startActivity(i);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();

                        }else{
                            Intent intent=new Intent(Splash.this, MainActivity2.class);
                            if(from != null) {
                                intent.putExtra("id", id);
                                intent.putExtra("user", user);
                                intent.putExtra("action", action);
                                intent.putExtra("from", from);
                            }
                            else
                            {
                                intent.putExtra("id", "");
                                intent.putExtra("user", "");
                                intent.putExtra("action", "");
                                intent.putExtra("from", "");
                            }
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                        }

                    }

                }
            }
        };
        thread.start();
    }*/

        SharedPreferences settings = getSharedPreferences(MainPP_SP, 0);
        HashMap<String, String> map = (HashMap<String, String>) settings.getAll();

        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("TAG","@@@ IN IF Build.VERSION.SDK_INT >= 23");
            String[] PERMISSIONS = {android.Manifest.permission.CAMERA,
                    android.Manifest.permission.INTERNET,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION

            };

            if (!hasPermissions(mContext, PERMISSIONS)) {
                Log.d("TAG","@@@ IN IF hasPermissions");
                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
            } else {
                Log.d("TAG","@@@ IN ELSE hasPermissions");
                callNextActivity();
            }
        }




        else {
            Log.d("TAG","@@@ IN ELSE  Build.VERSION.SDK_INT >= 23");
            callNextActivity();
        }
//        if(getIntent().getStringExtra("from") !=null){
//            String id=getIntent().getStringExtra("id");
//            String user=getIntent().getStringExtra("user");
//            String from=getIntent().getStringExtra("from");
//            Intent ss = new Intent(Splash.this, MainActivity2.class);
//            ss.putExtra("id",id);
//            ss.putExtra("user",user);
//            ss.putExtra("from",from);
//            ss.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            ss.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            ss.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            ss.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//            startActivity(ss);
//            finish();
//            Chat fragment2 = new Chat();
//            Bundle bundle = new Bundle();
//            bundle.putString("from","push") ;
//            bundle.putString("id", getIntent().getStringExtra("id"));
//            bundle.putString("user", getIntent().getStringExtra("user"));
//            fragment2.setArguments(bundle);
//            FragmentManager fragmentManager = this.getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.home_content, fragment2);
//            fragmentTransaction.commit();

//        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG","@@@ PERMISSIONS grant");
                    callNextActivity();
                } else {
                    Log.d("TAG","@@@ PERMISSIONS Denied");
                    Toast.makeText(mContext, "Permissions Denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }



    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("ⓘ Exit ! " + getString(R.string.app_name));
        alertDialogBuilder
                .setMessage(Html.fromHtml("<p style='text-align:center;'>Please Fill the required details</p><h3 style='text-align:center;'>Click Yes to Exit !</h4>"))
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(0);

                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void callNextActivity()
    {
        Thread thread=new Thread()
        {
            public void run()
            {
                try {
                    Thread.sleep(3000);
                }
                catch (Exception e)
                {

                }
                finally {
                    boolean pinAsked = session.isPinAsked();
                    boolean isLoggedIn = session.isLoggedIn();
                    Log.e("pinAsked", String.valueOf(pinAsked));
                    Log.e("isLoggedIn", String.valueOf(pinAsked));

                    if(!isLoggedIn){
                        Intent i = new Intent(Splash.this, Login.class);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        Splash.this.startActivity(i);
                    }
                    else{
                        if(pinAsked){
                            Intent i = new Intent(Splash.this, EnterPinActivity.class);
                            i.putExtra("email","");
                            startActivity(i);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();

                        }else{
                            Intent intent=new Intent(Splash.this, MainActivity2.class);
                            if(from != null) {
                                intent.putExtra("id", id);
                                intent.putExtra("user", user);
                                intent.putExtra("action", action);
                                intent.putExtra("from", from);
                            }
                            else
                            {
                                intent.putExtra("id", "");
                                intent.putExtra("user", "");
                                intent.putExtra("action", "");
                                intent.putExtra("from", "");
                            }
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                        }

                    }

                }
            }
        };
        thread.start();
    }
}