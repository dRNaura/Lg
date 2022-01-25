package com.lg_project.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;
import com.lg_project.adapter.MainActivityAdapter;
import com.lg_project.fragment.ContactUs;
import com.lg_project.fragment.Home;
import com.lg_project.fragment.MyCredit;
import com.lg_project.fragment.MyFavourite;
import com.lg_project.fragment.Setting;
import com.lg_project.fragment.ShowMenu;
import com.lg_project.fragment.StleathMode;
import com.lg_project.fragment.menu;
import com.lg_project.modelclass.menu.MenuData;
import com.lg_project.modelclass.menu.ResponseMenu;
import com.lg_project.modelclass.notifymsg.OfflineMsgResponseData;

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

public class MainActivity extends AppCompatActivity {
    public static LinearLayout contactus,food,credit,l1,stealth,logout,myFavourites,lay_setting,l1_menu,buycredit;
    public static ImageView menu;
    public static TextView heading;
    ArrayList<String> title=new ArrayList<>();
    ArrayList<Integer> id=new ArrayList<Integer>();

    String deviceToken="";
    RecyclerView recyclerView;

    // Session Manager Class
    SessionManageent session;
    MainActivityAdapter mainadapter;
    public static DrawerLayout drawerLayout;
    int idd=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  menu();
        // Session class instance
        session = new SessionManageent(getApplicationContext());

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
                    // Log and toast

                }
            });
        }catch (Exception e){
            Log.e("Firebase Exception", e.toString());
        }

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // email
         idd = Integer.parseInt(user.get(SessionManageent.KEY_ID));
        Log.e("ERER", String.valueOf(idd));


    /*    recyclerView=(RecyclerView)findViewById(R.id.recycler_menu);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        mainadapter = new MainActivityAdapter(this,title,id);
        recyclerView.setAdapter(mainadapter); // set the Adapter to RecyclerView
*/



        heading=(TextView)findViewById(R.id.heading_text);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);


        final LinearLayout content = (LinearLayout) findViewById(R.id.content);
        l1 = (LinearLayout) findViewById(R.id.l1);

        logout=(LinearLayout) findViewById(R.id.l8_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
            }
        });




        //LinearLayout
        contactus=(LinearLayout)findViewById(R.id.l4_contactus);
       // food=(LinearLayout)findViewById(R.id.l1_food);
        credit=(LinearLayout)findViewById(R.id.l5_credit);
        stealth=(LinearLayout)findViewById(R.id.l6);
        myFavourites=(LinearLayout)findViewById(R.id.lay_fav);
       // lay_setting=(LinearLayout)findViewById(R.id.lay_setting);
        l1_menu=(LinearLayout)findViewById(R.id.l1_menu);

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
                getSupportFragmentManager().beginTransaction().replace(R.id.drawerLayout,myFavourite).addToBackStack(null).commit();
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

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu menu=new menu() ;
                Bundle args = new Bundle();
                args.putInt("id", 0);
                menu.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.drawerLayout,menu).addToBackStack(null).commit();
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

        stealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StleathMode menu=new StleathMode() ;
                getSupportFragmentManager().beginTransaction().replace(R.id.drawerLayout,menu).addToBackStack(null).commit();
                overridePendingTransition(R.anim.enter, R.anim.exit);
                drawerLayout.closeDrawers();
            }
        });
        //Imageview
        menu=(ImageView)findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,
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

       // updateDevice();

        //homefragment
        Home home=new Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.home_content,home).commit();
        overridePendingTransition(R.anim.enter, R.anim.exit);



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
                if (response.isSuccessful()) {
                    if(response.body().getSuccess())
                    {
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
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Something Wrong !!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseMenu> call, Throwable t) {
                Log.d("ERRor",t.getMessage());
                //Toast.makeText(Login.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateDevice() {
        /** Create handle for the RetrofitInstance interface*/


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user", idd);
            jsonObject.put("deviceToken",deviceToken);
            jsonObject.put("platform","android");
            jsonObject.put("user_type",2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Api api = RetrofitInstance.getRetrofitInstance().create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<OfflineMsgResponseData> call = api.updateDevice(body);

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<OfflineMsgResponseData>() {
            @Override
            public void onResponse(Call<OfflineMsgResponseData> call,Response<OfflineMsgResponseData> response) {

                Log.e("SUCCESS", String.valueOf(response.body()));
                // Log.d("SUCCESSLOG", String.valueOf(response.body().getSuccess()));
                if (response.isSuccessful()) {
                    if(response.body().getSuccess())
                    {
//                        Toast.makeText(MainActivity.this, "Success !!", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
//                        Toast.makeText(MainActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
//                    Toast.makeText(MainActivity.this, "Something Wrong !!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<OfflineMsgResponseData> call, Throwable t) {
                Log.d("ERRor",t.getMessage());
                //Toast.makeText(Login.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}