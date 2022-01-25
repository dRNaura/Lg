package com.lg_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lg_project.R;
import com.lg_project.modelclass.AllStealthModel;
import java.util.ArrayList;
public class AllStealthAdapter  extends RecyclerView.Adapter<AllStealthAdapter.MyViewHolder> {

    Context context;
    ArrayList<AllStealthModel> list;
    AllStealthAdapter.OnClicked onClicked;

    public AllStealthAdapter(Context context, ArrayList<AllStealthModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public AllStealthAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_stealth_items, parent, false);
        // set the view's size, margins, paddings and layout parameters
        AllStealthAdapter.MyViewHolder vh = new AllStealthAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(AllStealthAdapter.MyViewHolder holder, final int position) {

        AllStealthModel model=list.get(position);
        holder.title.setText(model.title);

        if(model.selected.equalsIgnoreCase("")){
            holder.img_selected.setVisibility(View.GONE);
        }else{
            holder.img_selected.setVisibility(View.VISIBLE);
        }

        holder.lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClicked !=null){
                    onClicked.setOnClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        RelativeLayout lay;
        ImageView img_selected;
        public MyViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            lay=itemView.findViewById(R.id.lay);
            img_selected=itemView.findViewById(R.id.img_selected);
        }
    }
    public void setOnClicked(AllStealthAdapter.OnClicked onClicked) {
        this.onClicked = (AllStealthAdapter.OnClicked) onClicked;
    }

    public interface OnClicked {
        void setOnClick(int position);
    }
}
