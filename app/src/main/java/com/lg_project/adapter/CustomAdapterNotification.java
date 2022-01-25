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

public class CustomAdapterNotification extends RecyclerView.Adapter<CustomAdapterNotification.MyViewHolder> {

    ArrayList<String> notify;
    ArrayList<String> date;
    Date msgDate;

    Context context;
    public CustomAdapterNotification(Context context, ArrayList<String> notify, ArrayList<String> date) {
        this.context = context;
        this.notify = notify;
        this.date = date;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout_notification, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        SimpleDateFormat sp =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            msgDate = sp.parse(date.get(position));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String date1=null;
//            newDate = spf.parse(currentDate);
        sp = new SimpleDateFormat("dd-MM-yyyy");
        date1 = sp.format(msgDate);


        holder.notify.setText(notify.get(position));
        holder.date.setText(date1);

    }
    @Override
    public int getItemCount() {
        return date.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView notify,date;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            date=(TextView)itemView.findViewById(R.id.date);
            notify=(TextView)itemView.findViewById(R.id.notification);
        }
    }
}