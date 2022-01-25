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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lg_project.activity.MainActivity2;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;
import com.lg_project.adapter.CustomAdapterCredit;
import com.lg_project.modelclass.credithistory.CreditHistoryData;
import com.lg_project.modelclass.credithistory.ResponseCreditHistory;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MyCredit extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> date,name,price;
    Button buy;
    TextView credit;
    LinearLayout back;

    private CustomAdapterCredit customAdapter;
    private SessionManageent session;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        MainActivity2.heading.setText(R.string.mycredits);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_my_credit, container, false);


        return v;
    }

    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        //Log.d("REST", String.valueOf(Customer.getCredit()));

        MainActivity2.l1.setVisibility(View.VISIBLE);
        session = new SessionManageent(getContext());
        HashMap<String, String> user = session.getUserDetails();
        String email = user.get(SessionManageent.KEY_Credit);
        String id = user.get(SessionManageent.KEY_CUST_ID);
        Log.d("ERER", String.valueOf(email));

        credit=(TextView)v.findViewById(R.id.credit_text);
        back=(LinearLayout) v.findViewById(R.id.back);

        if (null != email && email.length() > 0 )
        {
            int endIndex = email.lastIndexOf(".");
            if (endIndex != -1)
            {
                String newstr = email.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
                //credit.setText(""+Integer.parseInt(String.format("%f",Double.parseDouble(email))));
                credit.setText(newstr+" Credits");
            }
        }

        //credit.setText(String.format("%.2f", Double.parseDouble(email)));



        // email


        buy=(Button)v.findViewById(R.id.buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyCredit mergePdf = new BuyCredit();
                FragmentManager fragmentManager1 = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.home_content, mergePdf);
                fragmentTransaction1.commit();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity2.class);
                startActivity(intent);
                onDestroy();
            }
        });

        date=new ArrayList<>();
        name=new ArrayList<>();
        price=new ArrayList<>();

        recyclerView=(RecyclerView)v.findViewById(R.id.recycle_credit);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        customAdapter = new CustomAdapterCredit(getContext(),date,name,price);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
        //  call the constructor of CustomAdapter to send the reference and data to Adapter


        History(id);



    }
        public void History(String id) {
            Retrofit retrofit = RetrofitInstance.getLocale(getContext());
            Api api = retrofit.create(Api.class);

        /** Call the method with parameter in the interface to get the notice data*/
        Call<ResponseCreditHistory> call = api.credithistory(id);

        /**Log the URL called*/
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseCreditHistory>() {
            @Override
            public void onResponse(Call<ResponseCreditHistory> call, Response<ResponseCreditHistory> response) {

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
                         //   Toast.makeText(getContext(), "" + getResources().getString(R.string.session), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Toast.makeText(getContext(), "" +errorCode, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<CreditHistoryData> list = response.body().getData();
                    for (int i1 = 0; i1 < list.size(); i1++) {
                        Log.d("Date", String.valueOf(list.get(i1).getDate()));
                        date.add(list.get(i1).getDate());
                        Log.d("Name", String.valueOf(list.get(i1).getDetail()));
                        name.add(list.get(i1).getBeneficiary());
                        Log.d("Amount", String.valueOf(list.get(i1).getAmount()));
                        price.add(String.valueOf(list.get(i1).getAmount()));

                    }
                    customAdapter.notifyDataSetChanged();


                } catch (Exception e) {

                }
            }
            @Override
            public void onFailure(Call<ResponseCreditHistory> call, Throwable t) {
                //  Log.d("ERRor",t.getMessage());
                Toast.makeText(getContext(), ""+getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
            }
        });

    }


}