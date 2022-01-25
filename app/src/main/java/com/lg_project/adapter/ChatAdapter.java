package com.lg_project.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lg_project.R;
import com.lg_project.modelclass.ChatModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.himanshusoni.chatmessageview.ChatMessageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    public Context context;
    ArrayList<ChatModel> list = new ArrayList<>();
    int userID;
    String ddtae,time,formattedDate;


    public ChatAdapter(Context context, ArrayList<ChatModel> list, int userID) {

        this.context = context;
        this.list = list;
        this.userID = userID;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_message_sender, txt_message_received, txt_message_receive_date, txt_message_send_date;
        LinearLayout lay_receiver, lay_sender;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_message_sender = itemView.findViewById(R.id.txt_message_sender);
            txt_message_received = itemView.findViewById(R.id.txt_message_received);
            lay_receiver = itemView.findViewById(R.id.lay_receiver);
            lay_sender = itemView.findViewById(R.id.lay_sender);
            txt_message_receive_date = itemView.findViewById(R.id.txt_message_receive_date);
            txt_message_send_date = itemView.findViewById(R.id.txt_message_send_date);

        }

    }


    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_message__item, parent, false);
        return new ChatAdapter.MyViewHolder(itemView);

    }

    public void add(ChatModel message) {
        list.add(message);
        notifyItemInserted(list.size() - 1);
    }
    public void addItem(ChatModel model) {
        list.add(model);
        notifyItemInserted(list.size());
    }

    @Override
    public void onBindViewHolder(final ChatAdapter.MyViewHolder viewHolder, final int i) {

      /*  ChatModel model=list.get(i);

        Log.d("FROM", String.valueOf(model.from_user));
        Log.d("USER_ID", String.valueOf(userID));
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat spf_time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date newDate = null;
        try {
            newDate = spf.parse(model.createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf_time = new SimpleDateFormat("hh:mm a");
        spf = new SimpleDateFormat("MMM, dd yyyy");
        ddtae = spf.format(newDate);
        time = spf_time.format(newDate);
        Log.d("DDATE",ddtae);
        Log.d("Time",time);

        Date c = Calendar.getInstance().getTime();


        SimpleDateFormat df = new SimpleDateFormat("MMM, dd yyyy", Locale.getDefault());
        formattedDate = df.format(c);
        Log.d("Current date",formattedDate);

        if(model.from_user==userID){
            viewHolder.sender_linear.setVisibility(View.VISIBLE);
            viewHolder.sender.setVisibility(View.VISIBLE);
            viewHolder.sender.setShowArrow(true);
            viewHolder.txt_message_sender.setVisibility(View.VISIBLE);
            viewHolder.Txt_message_sender_date.setVisibility(View.VISIBLE);
            viewHolder.txt_message_sender.setText(model.msg);
            if(ddtae.equalsIgnoreCase(formattedDate)) {
                viewHolder.Txt_message_sender_date.setText("Today at "+time);
            }
            else
            {
                viewHolder.Txt_message_sender_date.setText(ddtae);
            }
        }else{
            viewHolder.receiver_linear.setVisibility(View.VISIBLE);
            viewHolder.receiver.setVisibility(View.VISIBLE);
            viewHolder.receiver.setShowArrow(true);
            viewHolder.txt_message_received.setVisibility(View.VISIBLE);
            viewHolder.Txt_message_received_date.setVisibility(View.VISIBLE);
            viewHolder.txt_message_received.setText(model.msg);
            if(ddtae.equalsIgnoreCase(formattedDate)) {
                viewHolder.Txt_message_received_date.setText("Today at "+time);
            }
            else
            {
                viewHolder.Txt_message_received_date.setText(ddtae);
            }
        }

*/
       // holder.date.setText(ddtae);


        ChatModel model = list.get(i);

        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = formatter.format(todayDate);

//        String currentDate = java.text.DateFormat.getDateTimeInstance().format(new Date());

//        SimpleDateFormat spf = new SimpleDateFormat("MMM dd,yyyy hh:mm:ss a");
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date newDate = null;
        Date msgDate = null;
        try {
//            newDate = spf.parse(currentDate);
            msgDate = sp.parse(model.createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        spf = new SimpleDateFormat("yyyy-MM-dd");
        sp = new SimpleDateFormat("yyyy-MM-dd");
//        String strToday = spf.format(newDate);
        String strMsgDate = sp.format(msgDate);


        if (model.from_user == userID) {
            viewHolder.txt_message_sender.setText(model.msg);
            viewHolder.lay_sender.setVisibility(View.VISIBLE);
            viewHolder.lay_receiver.setVisibility(View.GONE);

            if (!model.createdAt.equalsIgnoreCase("")) {
                SimpleDateFormat spf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date newDate2 = null;
                try {
                    newDate2 = spf2.parse(model.createdAt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(currentDate.equalsIgnoreCase(strMsgDate)) {
                    spf2 = new SimpleDateFormat("hh:mm a");
                } else {
                    spf2 = new SimpleDateFormat("hh:mm a, dd MMM");
                }
                String strDate1 = spf2.format(newDate2);
                viewHolder.txt_message_send_date.setText(strDate1);
            }
        } else {
            viewHolder.txt_message_received.setText(model.msg);
            viewHolder.lay_receiver.setVisibility(View.VISIBLE);
            viewHolder.lay_sender.setVisibility(View.GONE);

            if (!model.createdAt.equalsIgnoreCase("")) {
                SimpleDateFormat spf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date newDate2 = null;
                try {
                    newDate2 = spf2.parse(model.createdAt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (currentDate.equalsIgnoreCase(strMsgDate)) {
                    spf2 = new SimpleDateFormat("hh:mm a");
                } else {
                    spf2 = new SimpleDateFormat("hh:mm a, dd MMM");
                }

                String strDate1 = spf2.format(newDate2);
                viewHolder.txt_message_receive_date.setText(strDate1);
            }
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}