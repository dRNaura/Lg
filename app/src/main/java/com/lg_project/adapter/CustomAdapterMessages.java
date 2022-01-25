package com.lg_project.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lg_project.R;
import com.lg_project.modelclass.ContractorListModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//import com.lg_contractor.MainActivity;

public class CustomAdapterMessages extends RecyclerView.Adapter<CustomAdapterMessages.MyViewHolder> {

    ArrayList<ContractorListModel> list;
    Context context;
    OnClicked onClicked;
    Date newDate;

    public CustomAdapterMessages(Context context, ArrayList<ContractorListModel> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout_messages, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items

        ContractorListModel model = list.get(position);
        Log.d("ASIZE", String.valueOf(model.first_name+" "+model.online));
        holder.name.setText(model.first_name + " " + model.last_name);
        if (!model.last_msg.equalsIgnoreCase("")) {
            holder.msg.setText(model.last_msg);
        }

        if (model.online) {
            holder.status.setImageResource(R.drawable.status);
        } else {
            holder.status.setImageResource(R.drawable.status_grey);
        }

        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        if(!model.last_msg_date.equalsIgnoreCase("")) {
            try {
                newDate = spf.parse(model.last_msg_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            spf = new SimpleDateFormat("hh:mm a");
            String strDate1 = spf.format(newDate);
            holder.text_date.setText(strDate1);
        }



        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (onClicked != null) {
                    onClicked.setOnItem(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView msg, name, text_date;
        ImageView image, status, red;
        RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            msg = (TextView) itemView.findViewById(R.id.msg_text);
            name = (TextView) itemView.findViewById(R.id.name_txt);
            text_date = (TextView) itemView.findViewById(R.id.text_date);
            image = (ImageView) itemView.findViewById(R.id.profile);
            status = (ImageView) itemView.findViewById(R.id.status);
            red = (ImageView) itemView.findViewById(R.id.red_dot);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativee);
        }
    }

    public void setOnClicked(CustomAdapterMessages.OnClicked addToFav) {
        this.onClicked = (CustomAdapterMessages.OnClicked) addToFav;
    }

    public interface OnClicked {
        void setOnItem(int position);
    }
}