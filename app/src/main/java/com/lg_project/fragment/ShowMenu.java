package com.lg_project.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.lg_project.activity.MainActivity2;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;
import com.lg_project.adapter.CustomAdapterMenu;
import com.lg_project.modelclass.menu.MenuData;
import com.lg_project.modelclass.menu.ResponseMenu;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ShowMenu extends Fragment {
    PhotoView image_menu;
    LinearLayout back;
    RecyclerView recyclerView;
    CustomAdapterMenu customAdapter;
    ArrayList<String> title,id,path;
    SessionManageent sessionManageent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_menu, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        MainActivity2.heading.setText(R.string.menu);
    }

    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        sessionManageent = new SessionManageent(getContext());

        findViews(v);
    }
    public void findViews(View v){
        image_menu=v.findViewById(R.id.image_menu);
        back=v.findViewById(R.id.back);

        MainActivity2.l1.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity2.class);
                startActivity(intent);
                onDestroy();
            }
        });
        title=new ArrayList<>();
        id=new ArrayList<>();
        path=new ArrayList<>();

        menu();


        recyclerView=(RecyclerView)v.findViewById(R.id.recycle_menu);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager);
        customAdapter = new CustomAdapterMenu(getContext(), title, id, path);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView// set LayoutManager to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter


//        Picasso.get()
//                .load(R.mipmap.food_menuImage)
//                .into(image_menu);

        customAdapter.setOnClicked(new CustomAdapterMenu.OnClicked() {
            @Override
            public void setOnItem(int position) {
                String someFilepath = path.get(position);
                String extension = someFilepath.substring(someFilepath.lastIndexOf("."));
                Log.e("extension",extension);
                if(extension.equalsIgnoreCase(".pdf")){
                    PdfView mergePdf = new PdfView();
                    Bundle bundle = new Bundle();
                    bundle.putString("path",path.get(position));
                    bundle.putString("title",title.get(position));
                    mergePdf.setArguments(bundle);
                    FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                    fragmentTransaction1.add(R.id.drawerLayout, mergePdf);
                    fragmentTransaction1.addToBackStack(null);
                    fragmentTransaction1.commit();
                }else{
                    menu mergePdf = new menu();
                    Bundle bundle = new Bundle();
                    bundle.putString("path",path.get(position));
                    bundle.putString("title",title.get(position));
                    mergePdf.setArguments(bundle);
                    FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                    fragmentTransaction1.add(R.id.drawerLayout, mergePdf);
                    fragmentTransaction1.addToBackStack(null);
                    fragmentTransaction1.commit();
                }

            }
        });

    }

    public void menu()
    {
        Retrofit retrofit = RetrofitInstance.getLocale(getContext());
        Api api = retrofit.create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseMenu> call = api.menu();

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseMenu>() {
            @Override
            public void onResponse(Call<ResponseMenu> call, Response<ResponseMenu> response) {

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
                            sessionManageent.logoutUser();
                           // Toast.makeText(getContext(), "" + getResources().getString(R.string.session), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(getContext(), "" +errorCode, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            Log.d("SUCCESS", String.valueOf(response.body().getSuccess()));
                            List<MenuData> list = response.body().getData();
                            for (int i = 0; i < list.size(); i++) {
                                Log.d("ID", String.valueOf(list.get(i).getId()));
                                Log.d("Nsme", String.valueOf(list.get(i).getTitle()));

                                id.add(String.valueOf(list.get(i).getId()));
                                title.add(String.valueOf(list.get(i).getTitle()));
                                path.add(String.valueOf(list.get(i).getPath()));

                            }

                            customAdapter.notifyDataSetChanged();



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
            public void onFailure(Call<ResponseMenu> call, Throwable t) {
                Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });
    }


}