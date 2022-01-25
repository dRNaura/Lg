package com.lg_project.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;
import com.lg_project.fragment.BookATable;
import com.lg_project.fragment.BuyCredit;
import com.lg_project.fragment.Chat;
import com.lg_project.fragment.ContactUs;
import com.lg_project.fragment.EditProfile;
import com.lg_project.fragment.HighFiveFragment;
import com.lg_project.fragment.Home;
import com.lg_project.fragment.ManageProfile;
import com.lg_project.fragment.Messages;
import com.lg_project.fragment.MyCredit;
import com.lg_project.fragment.MyFavourite;
import com.lg_project.fragment.PdfView;
import com.lg_project.fragment.Setting;
import com.lg_project.fragment.ShowMenu;
import com.lg_project.fragment.StleathMode;
import com.lg_project.modelclass.SocketManager;
import com.lg_project.modelclass.menu.MenuData;
import com.lg_project.modelclass.menu.ResponseMenu;
import com.lg_project.modelclass.notifymsg.OfflineMsgResponseData;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public LinearLayout lay_home,l1_messages,contactus,food,credit,stealth,logout,lay_high_five,
            myFavourites,lay_setting,l1_menu,lay_profile,l7_selectlanguage,buycredit,l8_bookatable;
    public static LinearLayout l1;
    public static ImageView menu;
    public static TextView heading;
    ArrayList<String> title=new ArrayList<>();
    ArrayList<Integer> id=new ArrayList<Integer>();
    String deviceToken="";
    // Session Manager Class
    SessionManageent session;
    String applanguage;
    public static DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    int email=0;
    NavigationView nav_view;
    boolean doubleBackToExitPressedOnce = false;
    Socket mSocket;
    private boolean isConnected;
    private SocketManager socketManager;
    String mLatestVersionName="",username="",password="",custid="",userFullName="",socketID;
    private int userid=0;

    Context context;
    Resources resources;
    Timer timer;
    //    private View view_contractor,view_Messages,view_Notificaion;
//    private RelativeLayout lay_Contactors,lay_Messages,lay_Notification;
//    private ImageView img_noti,img_msg,img_contract ;
//    private TextView txt_contactor,txt_Messages,txt_Notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManageent(getApplicationContext());
        HashMap<String, String> language = session.getAppLanguage();
        // email
        applanguage=language.get(SessionManageent.KEY_APP_LANGUAGE);
        setAppLocale(applanguage);

        setContentView(R.layout.activity_main_drawer);

        Intent intent = getIntent();
        if(intent != null && intent.getStringExtra("id")!= null && !(intent.getStringExtra("id")).equals("") &&
                ((intent.getStringExtra("action").equals("OFFLINE_ARTIST")) ||
                        (intent.getStringExtra("action").equals("OFFLINE_CUSTOMER"))) ) {
            String id = intent.getStringExtra("id");
            String userr = intent.getStringExtra("user");
            String from = intent.getStringExtra("from");
           // Toast.makeText(this, id + " " + userr + " " + from, Toast.LENGTH_SHORT).show();

            Chat fragment2 = new Chat();
            Bundle bundle = new Bundle();
            bundle.putInt("idCustomer", Integer.parseInt(id));
            bundle.putInt("useridCustomer", Integer.parseInt(userr));
            bundle.putString("from", "fromPush");
            fragment2.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.drawerLayout, fragment2);
            fragmentTransaction.commit();
           // drawerLayout.closeDrawers();

        }
        Log.e("AppLanguage",applanguage);

        socketManager = (SocketManager)getApplicationContext();
        mSocket = socketManager.getSocket();
        mSocket.on(Socket.EVENT_CONNECT,onConnected);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnected);
        mSocket.on(Socket.EVENT_CONNECT_ERROR,onConnecttionError);
        mSocket.on("subscribe-customer-ack",onSubscribe);
       // mSocket.on("unsubscribe-customer-ack",onUnSubscribe);
        mSocket.connect();

        HashMap<String, String> user = session.getUserDetails();
        // email
        userid=Integer.parseInt(user.get(SessionManageent.KEY_ID));
        custid = user.get(SessionManageent.KEY_CUST_ID);
        username = user.get(SessionManageent.KEY_EMAIL);
        userFullName = user.get(SessionManageent.KEY_FIRSTNAME);

        HashMap<String, String> socket = session.getSocketID();
        socketID=socket.get(session.KEY_SOCKET_ID);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", socketID);
            jsonObject.put("email", username);
            jsonObject.put("username", userFullName);
            jsonObject.put("user", userid);
            jsonObject.put("room", "customer");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("subscribe-customer",jsonObject);


//        new VersionChecker().execute();
        VersionChecker versionChecker = new VersionChecker();
        try {
            mLatestVersionName = versionChecker.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //  menu();
        // Session class instance


        // Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();


        FirebaseApp.initializeApp(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel("MyNotifications","MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
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
                    updateDevice(deviceToken);
                    // Log and toast

                }
            });
        }catch (Exception e){
            Log.e("Firebase Exception", e.toString());
        }

        // get user data from session
        // email
        email = Integer.parseInt(user.get(SessionManageent.KEY_ID));
        Log.e("ERER", String.valueOf(email));


    /*    recyclerView=(RecyclerView)findViewById(R.id.recycler_menu);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        mainadapter = new MainActivityAdapter(this,title,id);
        recyclerView.setAdapter(mainadapter); // set the Adapter to RecyclerView
*/

//        view_contractor=findViewById(R.id.view_contractor);
//        view_Messages=findViewById(R.id.view_Messages);
//        view_Notificaion=findViewById(R.id.view_Notificaion);
//
//        lay_Contactors=findViewById(R.id.lay_Contactors);
//        lay_Messages=findViewById(R.id.lay_Messages);
//        lay_Notification=findViewById(R.id.lay_Notification);
//
//        img_noti=findViewById(R.id.img_noti);
//        img_msg=findViewById(R.id.img_msg);
//        img_contract=findViewById(R.id.img_contract);
//
//        txt_contactor=findViewById(R.id.txt_contactor);
//        txt_Messages=findViewById(R.id.txt_Messages);
//        txt_Notification=findViewById(R.id.txt_Notification);

        heading=(TextView)findViewById(R.id.heading_textt);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        final LinearLayout content = (LinearLayout) findViewById(R.id.content);
        l1 =findViewById(R.id.l1);

        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity2.this, drawerLayout,
                R.string.open, R.string.close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();



        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        View header = nav_view.getHeaderView(0);
        logout = header.findViewById(R.id.l8_logout);

        //LinearLayout
        contactus=header.findViewById(R.id.l4_contactus);
        //food=header.findViewById(R.id.l1_food);
        credit=header.findViewById(R.id.l6_mycredit);
        lay_high_five=header.findViewById(R.id.lay_high_five);
        stealth=header.findViewById(R.id.l6);
        myFavourites=header.findViewById(R.id.lay_fav);
        lay_setting=header.findViewById(R.id.lay_setting);
        l1_menu=header.findViewById(R.id.l1_menu);
        lay_profile=header.findViewById(R.id.lay_profile);
        l1_messages=header.findViewById(R.id.l1_messages);
        lay_home=header.findViewById(R.id.lay_home);

        l7_selectlanguage=header.findViewById(R.id.l7);

        buycredit=(LinearLayout)header.findViewById(R.id.l5_credit);
        buycredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BuyCredit contactUs=new BuyCredit();
                getSupportFragmentManager().beginTransaction().replace(R.id.home_content,contactUs).addToBackStack(null).commit();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                drawerLayout.closeDrawers();
            }
        });

        l8_bookatable=(LinearLayout)header.findViewById(R.id.l8);
        l8_bookatable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://15.207.210.221:8686/"));
                startActivity(browserIntent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                drawerLayout.closeDrawers();
            }
        });
        lay_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setting setting=new Setting();
                getSupportFragmentManager().beginTransaction().replace(R.id.drawerLayout,setting).addToBackStack(null).commit();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                drawerLayout.closeDrawers();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", socketID);
                    jsonObject.put("email", username);
                    jsonObject.put("username", userFullName);
                    jsonObject.put("user", userid);
                    jsonObject.put("room", "customer");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSocket.emit("unsubscribe-customer",jsonObject);
                session.logoutUser();
                finish();

            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactUs contactUs=new ContactUs();
                getSupportFragmentManager().beginTransaction().replace(R.id.home_content,contactUs).addToBackStack(null).commit();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                drawerLayout.closeDrawers();
            }
        });

        myFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFavourite myFavourite=new MyFavourite();
                getSupportFragmentManager().beginTransaction().replace(R.id.home_content,myFavourite).addToBackStack(null).commit();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                drawerLayout.closeDrawers();
            }
        });

        lay_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfile editProfile=new EditProfile();
                getSupportFragmentManager().beginTransaction().replace(R.id.home_content,editProfile).addToBackStack(null).commit();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                drawerLayout.closeDrawers();
            }
        });

        credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCredit menu=new MyCredit() ;
                getSupportFragmentManager().beginTransaction().replace(R.id.home_content,menu).addToBackStack(null).commit();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                drawerLayout.closeDrawers();
            }
        });
        lay_high_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HighFiveFragment menu=new HighFiveFragment() ;
                getSupportFragmentManager().beginTransaction().replace(R.id.home_content,menu).addToBackStack(null).commit();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                drawerLayout.closeDrawers();
            }
        });

        stealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StleathMode menu=new StleathMode() ;
                getSupportFragmentManager().beginTransaction().replace(R.id.home_content,menu).addToBackStack(null).commit();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                drawerLayout.closeDrawers();
            }
        });
        //Imageview
        menu=(ImageView)findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity2.this, drawerLayout,
                        R.string.open, R.string.close) {

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        super.onDrawerSlide(drawerView, slideOffset);
                        float slideX = drawerView.getWidth() * slideOffset;
                        content.setTranslationX(slideX);
                    }
                };
                drawerLayout.addDrawerListener(actionBarDrawerToggle);
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        l1_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowMenu menu=new ShowMenu() ;
                getSupportFragmentManager().beginTransaction().replace(R.id.home_content,menu).addToBackStack(null).commit();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                drawerLayout.closeDrawers();
            }
        });

        l7_selectlanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog optionDialog = new Dialog(MainActivity2.this);
                optionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                optionDialog.setContentView(R.layout.select_language_alert);
                optionDialog.setCancelable(true);
                Window window = optionDialog.getWindow();
                window.setLayout(GridLayoutManager.LayoutParams.MATCH_PARENT, GridLayoutManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);

                RadioGroup radioGroup=(RadioGroup)optionDialog.findViewById(R.id.radioGroup);
                Button select=(Button)optionDialog.findViewById(R.id.select);

                if(applanguage.equalsIgnoreCase("en")){
                    radioGroup.check(R.id.radioenglish);
                }else{
                    radioGroup.check(R.id.radiospanish);
                }

                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        RadioButton genderradioButton = (RadioButton)optionDialog.findViewById(selectedId);
                        if(selectedId==-1){
                            //Toast.makeText(MainActivity2.this,"Nothing selected", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(genderradioButton.getText().equals("English") || genderradioButton.getText().equals("Inglesa"))
                            {

                                session.AppLanguage("en");
                                setAppLocale("en");
                                restartActivity();
                            }
                            else
                            {
                                session.AppLanguage("es");
                                setAppLocale("es");
                                restartActivity();

                            }
                           // Toast.makeText(MainActivity2.this,genderradioButton.getText(), Toast.LENGTH_SHORT).show();

                        }
                        optionDialog.dismiss();
                    }
                });

                optionDialog.show();
                drawerLayout.closeDrawers();
            }
        });
        lay_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                heading.setText("Contractors");
                Home home=new Home();
                getSupportFragmentManager().beginTransaction().replace(R.id.home_content,home).commit();
                overridePendingTransition(R.anim.enter, R.anim.exit);

                drawerLayout.closeDrawers();
            }
        });
        l1_messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                heading.setText(R.string.msgs);
                Messages contactUs=new Messages();
                getSupportFragmentManager().beginTransaction().replace(R.id.home_content,contactUs).addToBackStack(null).commit();
               /* Intent intent = new Intent(MainActivity2.this,MainActivity3.class);
                startActivity(intent);*/

                overridePendingTransition(R.anim.enter, R.anim.exit);
                drawerLayout.closeDrawers();

//                Messages home=new Messages();
//                getSupportFragmentManager().beginTransaction().replace(R.id.home_content,home).commit();
//                overridePendingTransition(R.anim.enter, R.anim.exit);
//
//                drawerLayout.closeDrawers();
            }
        });



        //homefragment
        heading.setText("Contractors");
        Home home=new Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.home_content,home).commit();
        overridePendingTransition(R.anim.enter, R.anim.exit);




    }
    public static Context contextOfApplication;
    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    public void menu()
    {
        /** Create handle for the RetrofitInstance interface*/
        Api api = RetrofitInstance.getRetrofitInstance().create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseMenu> call = api.menu();

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseMenu>() {
            @Override
            public void onResponse(Call<ResponseMenu> call, Response<ResponseMenu> response) {

                Log.d("SUCCESS", String.valueOf(new Gson().toJson(response)));

                String errorbody = "", errorCode = "";
                try {
                    if (response.errorBody() != null) {
                        errorbody = response.errorBody().string();
                        JSONObject responseObject = new JSONObject(errorbody);
                        errorCode = responseObject.getString("message");
                        session.logoutUser();
                       // Toast.makeText(MainActivity2.this, "" + getResources().getString(R.string.session), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            Log.d("SUCCESS", String.valueOf(response.body().getSuccess()));
                            List<MenuData> list = response.body().getData();
                            for (int i = 0; i < list.size(); i++) {
                                Log.d("ID", String.valueOf(list.get(i).getId()));
                                Log.d("Nsme", String.valueOf(list.get(i).getTitle()));
                                id.add(list.get(i).getId());
                                title.add(list.get(i).getTitle());
                            }

                        }

                        //  List<Datum> list = response.body().getData();
                   /* for (int i = 0; i < list.size(); i++) {
                        Log.d("TEST", list.get(i).getFirstName());
                    }*/
                    } else {
                        Toast.makeText(MainActivity2.this, "" + getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseMenu> call, Throwable t) {
                Log.d("ERRor",t.getMessage());
                //Toast.makeText(Login.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateDevice(String token) {
        /** Create handle for the RetrofitInstance interface*/
        Retrofit retrofit = RetrofitInstance.getLocale(MainActivity2.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", userid);
            jsonObject.put("deviceToken",token);
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

                Log.e("DEVICESUCCESS", String.valueOf(response.body()));
                // Log.d("SUCCESSLOG", String.valueOf(response.body().getSuccess()));
                String errorbody = "", errorCode = "";
                try {
                    if (response.errorBody() != null) {
                        errorbody = response.errorBody().string();
                        JSONObject responseObject = new JSONObject(errorbody);
                        errorCode = responseObject.getString("message");
                        session.logoutUser();
                     //   Toast.makeText(MainActivity2.this, "" + getResources().getString(R.string.session), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            Toast.makeText(MainActivity2.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MainActivity2.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(MainActivity2.this, "" + getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<OfflineMsgResponseData> call, Throwable t) {
             //   Log.d("ERRor",t.getMessage());
                Toast.makeText(MainActivity2.this, ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        Log.e("backStack", "" + getSupportFragmentManager().getBackStackEntryCount());
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else {

            if (doubleBackToExitPressedOnce) {
                finishAffinity();
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Snackbar.make(drawerLayout, "Please Back again to exit", Snackbar.LENGTH_LONG)
                    .setAction("", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                        }
                    }).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }

    }
//    public void checkNavigation() {
//
//        if (getVisibleFragment() instanceof menu) {
//
//            tool_title.setText("LIST POS SALE");
//        } else if (getVisibleFragment() instanceof SaleDetailFragment) {
//            tool_title.setText("SALE DETAIL");
//        } else if (getVisibleFragment() instanceof ReturnSaleListFragment) {
//            tool_title.setText("RETURN SALE LIST");
//        } else if (getVisibleFragment() instanceof QuatationListFragment) {
//            tool_title.setText("LIST QUATATION");
//        } else if (getVisibleFragment() instanceof ListDraftFragment) {
//            tool_title.setText("LIST DRAFTS");
//        } else if (getVisibleFragment() instanceof CustomerListFragment) {
//            tool_title.setText("CUSTOMER LIST");
//        } else if (getVisibleFragment() instanceof CustomerViewDetail) {
//            tool_title.setText("VIEW CONTACT");
//        } else if (getVisibleFragment() instanceof SaleDetailFragment) {
//            tool_title.setText("SALE DEATIL");
//        } else if (getVisibleFragment() instanceof ReturnSaleDetailFragment) {
//            tool_title.setText("RETURN SALE DETAIL");
//        } else if (getVisibleFragment() instanceof ReturnSaleFragment) {
//            tool_title.setText("RETURN SALE");
//        } else if (getVisibleFragment() instanceof ShippingListFragment) {
//            tool_title.setText("DELIVERY ORDERS");
//        }
////        else if(getVisibleFragment() instanceof RepairListActivity) {
////            tool_title.setText("REPAIR LIST");
////        }
//        else {
//            tool_title.setText("DASHBOARD");
//
//
//        }
//    }
    private Fragment getVisibleFragment() {
        FragmentManager fragmentManager = MainActivity2.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    private Emitter.Listener onConnected = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            Log.e("connected", "connected");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    HashMap<String, String> socket = session.getSocketID();
                    String socketID=socket.get(session.KEY_SOCKET_ID);

                    if (!isConnected) {
                            session.socketID(mSocket.id());
                            Log.e("socket id", mSocket.id());
//                            Toast.makeText(MainActivity2.this, "Connected" + mSocket.id(), Toast.LENGTH_SHORT).show();
                            isConnected = true;

                    }
                }
            });
        }
    };


    private Emitter.Listener onDisconnected = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    isConnected = false;
//                    Toast.makeText(MainActivity2.this, "Disconnected", Toast.LENGTH_SHORT).show();

                }

            });
        }
    };

    private Emitter.Listener onConnecttionError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = false;
                    Object data = (Object) args[0];
                    Log.e("Error", data.toString());
//                    Toast.makeText(MainActivity2.this, "Connection Error", Toast.LENGTH_SHORT).show();

                }

            });
        }
    };

    private Emitter.Listener onSubscribe = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Object data = (Object) args[0];
                    Log.e("onSubscribe",data.toString());
                }
            });
        }
    };

    private Emitter.Listener onUnSubscribe = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Object data = (Object) args[0];
                    Log.e("onUnSubscribe",data.toString());
                }
            });
        }
    };


    @Override
    protected void onStop() {
        super.onStop();

        Log.e("Stop","onstop called");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", socketID);
            jsonObject.put("email", username);
            jsonObject.put("username", userFullName);
            jsonObject.put("user", userid);
            jsonObject.put("room", "customer");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("UNSUBSCRIBE",jsonObject.toString());
        mSocket.emit("unsubscribe-customer",jsonObject);


    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("Stop","onstop called");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", socketID);
            jsonObject.put("email", username);
            jsonObject.put("username", userFullName);
            jsonObject.put("user", userid);
            jsonObject.put("room", "customer");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("unsubscribe-customer",jsonObject);
//        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("RESTART","onrestart called");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", socketID);
            jsonObject.put("email", username);
            jsonObject.put("username", userFullName);
            jsonObject.put("user", userid);
            jsonObject.put("room", "customer");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("subscribe-customer",jsonObject);
    }

    private void setAppLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);

    }



    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        // Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
        switch (id)
        {
            case R.id.nav_home:
                Toast.makeText(this, "Home click", Toast.LENGTH_SHORT).show();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public class VersionChecker extends AsyncTask<String, String, String> {

        private String newVersion;

        @Override
        protected String doInBackground(String... params) {

            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "trai.gov.in.dnd" + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".hAyfc .htlgb")
                        .get(3)
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;
        }
    }
//    public class VersionChecker extends AsyncTask<String, String, String> {
//
//        private String newVersion;
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            try {
//                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en")
//                        .timeout(30000)
//                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                        .referrer("http://www.google.com")
//                        .get()
//                        .select("div[itemprop=softwareVersion]")
//                        .first()
//                        .ownText();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return newVersion;
//        }
//    }

}