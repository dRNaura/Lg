package com.lg_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.lg_project.R;

import java.util.ArrayList;

public class CustomAdapterPayment extends RecyclerView.Adapter<CustomAdapterPayment.MyViewHolder> {

    ArrayList<String> date;
    ArrayList<String> price;
    ArrayList<String> user_dec;
    ArrayList<String> contractor_dec;
    ArrayList<String> mode;

    Context context;
    public CustomAdapterPayment(Context context, ArrayList<String> date,ArrayList<String> price,
                                ArrayList<String> user_dec,ArrayList<String> contractor_dec,ArrayList<String> mode) {
        this.context = context;
        this.date = date;
        this.price = price;
        this.user_dec = user_dec;
        this.contractor_dec = contractor_dec;
        this.mode = mode;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout_payment, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items

        holder.date.setText(date.get(position));
        holder.price.setText(price.get(position));
        holder.user_dec.setText(user_dec.get(position));
        holder.contractor_dec.setText(contractor_dec.get(position));
        holder.mode.setText(mode.get(position));



    }
    @Override
    public int getItemCount() {
        return date.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView date,price,user_dec,contractor_dec,mode;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            date=(TextView)itemView.findViewById(R.id.date);
            price=(TextView)itemView.findViewById(R.id.price);
            user_dec=(TextView)itemView.findViewById(R.id.user_des);
            contractor_dec=(TextView)itemView.findViewById(R.id.contractor_des);
            mode=(TextView)itemView.findViewById(R.id.payment_mode);


        }
    }
}