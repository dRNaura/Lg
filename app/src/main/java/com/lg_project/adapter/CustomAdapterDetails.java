package com.lg_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.lg_project.R;

import java.util.ArrayList;

public class CustomAdapterDetails extends RecyclerView.Adapter<CustomAdapterDetails.MyViewHolder> {
    ArrayList<String> personImages;
    Context context;
    OnClicked onClicked;
    public CustomAdapterDetails(Context context, ArrayList<String> personImages) {
        this.context = context;
        this.personImages = personImages;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_rowlayout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

     //   holder.image.setImageResource(personImages.get(position));
        Glide.with(context).load(personImages.get(position)).placeholder(R.drawable.lg_logo).into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClicked !=null){
                    onClicked.setOnItem(position);
                }
            }
        });


    }
    @Override
    public int getItemCount() {
        return personImages.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public void setOnItemClicked(CustomAdapterDetails.OnClicked onClicked) {
        this.onClicked = (CustomAdapterDetails.OnClicked) onClicked;
    }

    public interface OnClicked {
        void setOnItem(int position);
    }

}