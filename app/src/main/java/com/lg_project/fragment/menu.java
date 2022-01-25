package com.lg_project.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.activity.MainActivity2;
import com.lg_project.modelclass.menu.ResponseMenuImage;
import com.lg_project.modelclass.menu.ResponseMenuImageData;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lg_project.activity.MainActivity2.drawerLayout;

public class menu extends Fragment {

    ImageView back;
    PhotoView menu;
    TextView titletxt;
//    WebView webView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        MainActivity2.heading.setText(getResources().getString(R.string.show_menu));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_menu, container, false);

        back=(ImageView)v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
//                ShowMenu menu=new ShowMenu() ;
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.lay_menu,menu).commit();

//                Intent intent=new Intent(getContext(), MainActivity2.class);
//                startActivity(intent);
//                onDestroy();
            }
        });


        return v;
    }

    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

//        MainActivity2.l1.setVisibility(View.GONE);

            String path = getArguments().getString("path",null);
            String title = getArguments().getString("title",null);

        // Toast.makeText(getContext(), ""+path, Toast.LENGTH_SHORT).show();

//        MainActivity2.l1.setVisibility(View.GONE);



//        drawerLayout.setScrimColor(Color.TRANSPARENT);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);



        titletxt=(TextView)v.findViewById(R.id.login_text);
        titletxt.setText(title);

        menu=(PhotoView) v.findViewById(R.id.image_menu);
//        webView=v.findViewById(R.id.webView);

//        webView.getSettings().setBuiltInZoomControls(true);
//        WebSettings settings = webView.getSettings();
//        settings.setJavaScriptEnabled(true);
//
//
//
//
//        String url = "http://docs.google.com/gview?embedded=true&url=" + path;
////        Log.i(TAG, "Opening PDF: " + url);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl(url);




        Picasso.get()
                .load(path)
                .into(menu);


    }


    public void menuimage(String id)
    {
        /** Create handle for the RetrofitInstance interface*/
        Api api = RetrofitInstance.getRetrofitInstance().create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseMenuImage> call = api.menuimage(id);

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseMenuImage>() {
            @Override
            public void onResponse(Call<ResponseMenuImage> call, Response<ResponseMenuImage> response) {

                Log.d("SUCCESS", String.valueOf(new Gson().toJson(response)));
                try {
                    String errorbody = "", errorCode = "";
                    if (response.errorBody() != null) {
                        errorbody = response.errorBody().string();
                        JSONObject responseObject = new JSONObject(errorbody);
                        if (responseObject.has("message") && !responseObject.isNull("message")) {
                            errorCode = responseObject.getString("message");
                        }
                        if (errorCode.equalsIgnoreCase("Unautherized access.")) {
//                            session.logoutUser();
                            Toast.makeText(getContext(), "" + getResources().getString(R.string.session), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(getContext(), "" +errorCode, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            Log.d("SUCCESS", String.valueOf(response.body().getSuccess()));
                            List<ResponseMenuImageData> list = response.body().getData();
                            for (int i = 0; i < list.size(); i++) {
                                Log.d("ID", String.valueOf(list.get(i).getId()));
                                Log.d("Name", String.valueOf(list.get(i).getPath()));
                            }

                            //  menu.setImageURI(Uri.parse(list.get(0).getPath()));
                               /* Glide.with(getContext())
                                .load(list.get(0).getPath())
                                .into(menu);*/

                            if (list.size() > 0) {
//                                Picasso.get()
//                                        .load(list.get(0).getPath())
//                                        .into(menu);

                            }

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
            public void onFailure(Call<ResponseMenuImage> call, Throwable t) {
                Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });
    }
}