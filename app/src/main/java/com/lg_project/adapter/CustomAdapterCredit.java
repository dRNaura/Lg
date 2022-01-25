package com.lg_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.lg_project.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapterCredit extends RecyclerView.Adapter<CustomAdapterCredit.MyViewHolder> {

    ArrayList<String> date;
    ArrayList<String> name;
    ArrayList<String> price;

    Context context;
    public CustomAdapterCredit(Context context, ArrayList<String> date, ArrayList<String> name,
                               ArrayList<String> price) {
        this.context = context;
        this.date = date;
        this.name = name;
        this.price = price;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout_credit, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
       holder.name.setText(name.get(position));
        String ddtae="";
       if(!date.get(position).equalsIgnoreCase("")){
           SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
           Date newDate = null;
           try {
               newDate = spf.parse(date.get(position));
           } catch (ParseException e) {
               e.printStackTrace();
           }
           spf = new SimpleDateFormat("hh:mm a - MMM, dd yyyy");
           ddtae = spf.format(newDate);
           holder.date.setText(ddtae);
       }else{
           holder.date.setText(ddtae);
       }
        String credit= price.get(position);
        if (null != credit && credit.length() > 0 )
        {
            int endIndex = credit.lastIndexOf(".");
            if (endIndex != -1)
            {
                String newstr = credit.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
                //credit.setText(""+Integer.parseInt(String.format("%f",Double.parseDouble(email))));
                holder.price.setText(newstr);
            }
        }

       /* String[] pric_credi = credit.split("\\.");
        holder.price.setText(pric_credi[0]);*/
    }
    @Override
    public int getItemCount() {
        return name.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name,date,price;
        public MyViewHolder(View itemView) {
            super(itemView);

            name=(TextView)itemView.findViewById(R.id.name_cr);
            date=(TextView)itemView.findViewById(R.id.date_cr);
            price=(TextView)itemView.findViewById(R.id.price_cr);


        }
    }
}