package com.lg_project.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.lg_project.R;
import com.lg_project.fragment.Details;
import com.lg_project.fragment.menu;

import java.util.ArrayList;

public class CustomAdapterMenu extends RecyclerView.Adapter<CustomAdapterMenu.MyViewHolder> {

    ArrayList<String> title;
    ArrayList<String> id;
    ArrayList<String> path;
    CustomAdapterMenu.OnClicked onClicked;

    Context context;
    public CustomAdapterMenu(Context context, ArrayList<String> title,ArrayList<String> id,ArrayList<String> path) {
        this.context = context;
        this.title = title;
        this.id = id;
        this.path=path;
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
        // set the data in items

        holder.title.setText(title.get(position));
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
        return title.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView title;
        RelativeLayout relativeLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's

            title=itemView.findViewById(R.id.menutitle);
            relativeLayout=itemView.findViewById(R.id.relativeee);


        }
    }

    public void setOnClicked(CustomAdapterMenu.OnClicked addToFav) {
        this.onClicked = (CustomAdapterMenu.OnClicked) addToFav;
    }

    public interface OnClicked {
        void setOnItem(int position);
    }
}