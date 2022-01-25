package com.lg_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lg_project.R;

import java.util.ArrayList;

public class CustomAdapterCreditPlan extends RecyclerView.Adapter<CustomAdapterCreditPlan.MyViewHolder> {

    ArrayList<String> creditamount;
    ArrayList<String> dollaramount;
    ArrayList<String> planid;
    ArrayList<String> cost;


    OnBuyClicked onBuyClicked;
    Context context;
    public CustomAdapterCreditPlan(Context context,ArrayList<String> creditamount,
                                   ArrayList<String> dollaramount,ArrayList<String> planid,ArrayList<String> cost) {
        this.context = context;
        this.creditamount = creditamount;
        this.dollaramount = dollaramount;
        this.planid = planid;
        this.cost = cost;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout_creditplan, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        if (null != creditamount.get(position) && creditamount.get(position).length() > 0 )
        {
            int endIndex = creditamount.get(position).lastIndexOf(".");
            if (endIndex != -1)
            {
                String newstr = creditamount.get(position).substring(0, endIndex); // not forgot to put check if(endIndex != -1)
                //credit.setText(""+Integer.parseInt(String.format("%f",Double.parseDouble(email))));
                holder.credit.setText(newstr+" Credits");
            }
        }


       holder.dollar.setText("$"+dollaramount.get(position));
       holder.buy.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(onBuyClicked !=null){
                   onBuyClicked.setOnBuy(position);
               }

           }
       });
    }
    @Override
    public int getItemCount() {
        return creditamount.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView credit,dollar;
        Button buy;
        public MyViewHolder(View itemView) {
            super(itemView);

            credit=(TextView)itemView.findViewById(R.id.credit_amount);
            dollar=(TextView)itemView.findViewById(R.id.dollar_credit);

            buy=(Button)itemView.findViewById(R.id.buy);


        }
    }


    public void setOnBuyClicked(OnBuyClicked onBuyClicked) {
        this.onBuyClicked = (OnBuyClicked) onBuyClicked;
    }

    public interface OnBuyClicked {
        void setOnBuy(int position);
    }

}