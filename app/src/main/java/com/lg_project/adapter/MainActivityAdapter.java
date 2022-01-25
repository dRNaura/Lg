package com.lg_project.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.lg_project.activity.MainActivity2;
import com.lg_project.R;
import com.lg_project.fragment.menu;
import java.util.ArrayList;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> title;
    ArrayList<Integer> id;

    public MainActivityAdapter(Context context,ArrayList<String> title,ArrayList<Integer> id) {
        this.context=context;
        this.title=title;
        this.id=id;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout_menu, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.textView.setText(title.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+id.get(position), Toast.LENGTH_SHORT).show();
                menu men=new menu();
                Bundle args = new Bundle();
                args.putInt("id", id.get(position));
                men.setArguments(args);
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.drawerLayout, men);
                fragmentTransaction.commit();
                MainActivity2.drawerLayout.closeDrawers();
            }
        });

    }
    @Override
    public int getItemCount() {
        return title.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);

            textView=(TextView)itemView.findViewById(R.id.title);


        }
    }
}