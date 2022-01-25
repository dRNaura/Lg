package com.lg_project.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lg_project.Api.Api;
import com.lg_project.Api.RetrofitInstance;
import com.lg_project.R;
import com.lg_project.Session.SessionManageent;
import com.lg_project.activity.ShowImage;
import com.lg_project.fragment.EditProfile;
import com.lg_project.modelclass.deleteimage.ResponseDeleteImage;
import com.lg_project.modelclass.profilepic.ResponseProfilePic;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CustomAdapterImages extends RecyclerView.Adapter<CustomAdapterImages.MyViewHolder> {

    List<String> images;
    List<String> image_id;
    List<String> image_status;
    Context context;
    AlertDialog alertDialog;
    public CustomAdapterImages(Context context, List<String> image, List<String> image_id, List<String> image_status) {
        this.context = context;
        this.images = image;
        this.image_id=image_id;
        this.image_status=image_status;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayoutpics, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in itemsfinal
       // Log.d("IMAGES_STATUS",image_status.get(position)+" "+image_status);
        Drawable drawable = new ColorDrawable(context.getResources().getColor(R.color.img_forground));
        try {
            if (image_status.get(position).equals("Pending")) {
                holder.imageView.setForeground(drawable);
                holder.approval.setVisibility(View.VISIBLE);
                holder.approval.setText(context.getResources().getString(R.string.approval_pending));
                holder.imageView.setEnabled(false);

            } else if (image_status.get(position).equals("Rejected")) {
                holder.imageView.setForeground(drawable);
                holder.approval.setVisibility(View.VISIBLE);
                holder.approval.setText(context.getResources().getString(R.string.approval_rejected));
                holder.imageView.setEnabled(false);
            } else {
                holder.approval.setVisibility(View.GONE);
                holder.imageView.setEnabled(true);
            }
        }
        catch(Exception e)
        {

        }


        Glide.with(context).load(images.get(position)).placeholder(R.drawable.lg_logo).into(holder.imageView);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SessionManageent session = new SessionManageent(context);

                    HashMap<String, String> user = session.getUserDetails();


                    // email
                    int id = Integer.parseInt(user.get(SessionManageent.KEY_CUST_ID));
                    Log.d("ERER", String.valueOf(id));

                    try {
                       String dd= image_id.get(position);


                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogView = inflater.inflate(R.layout.option, null);
                    dialogBuilder.setView(dialogView);
                    dialogBuilder.setCancelable(false);

                    ImageView close = (ImageView) dialogView.findViewById(R.id.close_alert);
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    TextView view1 = (TextView) dialogView.findViewById(R.id.txt_view);
                    view1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            Intent intent = new Intent(context, ShowImage.class);
                            intent.putExtra("Image", images.get(position));
                            context.startActivity(intent);

                        }
                    });

                    TextView delete = (TextView) dialogView.findViewById(R.id.txt_delete);
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            delete(String.valueOf(id), image_id.get(position));

                        }
                    });

                    TextView profile = (TextView) dialogView.findViewById(R.id.txt_setasprofile);
                    profile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            profile(String.valueOf(id), image_id.get(position));
                        }
                    });


                    alertDialog = dialogBuilder.create();
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();

                    }
                    catch(Exception e)
                    {
                      //  Toast.makeText(context, "trrrr", Toast.LENGTH_SHORT).show();
                        alert(context.getResources().getString(R.string.message));
                    }

                }
            });
    }
    @Override
    public int getItemCount() {
        return images.size();
    }

    public void alert(String msg)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder
                // .setMessage(Html.fromHtml("<p style='text-align:center;'>Please Fill the required details</p><h3 style='text-align:center;'>Click Yes to Exit !</h4>"))
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                            }
                        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        ImageView imageView;
        TextView approval;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            imageView=(ImageView) itemView.findViewById(R.id.images);
            approval=(TextView) itemView.findViewById(R.id.approvaltext);

        }
    }

 public void delete(String id,String imageid)
    {
        ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.show();

        Retrofit retrofit = RetrofitInstance.getLocale(context);
        Api apiService = retrofit.create(Api.class);

       Call<ResponseDeleteImage> call = apiService.deleteimage(id,imageid);

        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseDeleteImage>() {
            @Override
            public void onResponse(Call<ResponseDeleteImage> call, Response<ResponseDeleteImage> response) {
             //  Log.d("RES",response.body().getMessage());
               progressDialog.dismiss();
                Toast.makeText(context, "Image Delete Successfully!!!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("custom-action-local-broadcast");
                intent.putExtra("name","success");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

            }

            @Override
            public void onFailure(Call<ResponseDeleteImage> call, Throwable t) {
                //Log.d("ERRor",t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(context, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    public void profile(String id,String image_id) {
        Retrofit retrofit = RetrofitInstance.getLocale(context);
        Api apiService = retrofit.create(Api.class);

        //*//** Call the method with parameter in the interface to get the notice data*//*
        Call<ResponseProfilePic> call = apiService.profilepic(ApiJsonMap(id, image_id));

       // *//**Log the URL called*//*
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<ResponseProfilePic>() {
            @Override
            public void onResponse(Call<ResponseProfilePic> call, Response<ResponseProfilePic> response) {

                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        Log.d("SUCCESS", String.valueOf(response.body().getMessage()));
                        Toast.makeText(context, "Profile Pic Set!!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent("custom-action-local-broadcast");
                        intent.putExtra("name", "success");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                        // Creating user login session
                        // For testing i am stroing name, email as follow
                        // Use user real data

                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "" + context.getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseProfilePic> call, Throwable t) {
                Log.d("ERRor", t.getMessage());
                //Toast.makeText(Login.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private JsonObject ApiJsonMap(String id, String imageid) {


        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("customer_id", id);
            jsonObj_.put("pic_id", imageid);



            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());

            //print parameter
            Log.e("MY gson.JSON:  ", "AS PARAMETER  " + gsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gsonObject;
    }
}