package com.lg_project.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.lg_project.R;
import com.lg_project.fragment.Details;
import com.lg_project.modelclass.ContactorModel;
import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> implements Filterable {
    Context context;
    AddToFav addToFav;
    DeleteFav deleteFav;
    String from="";
    ArrayList<ContactorModel> arraylist;
    ArrayList<ContactorModel> arraylist2;

    public CustomAdapter(Context context, ArrayList<ContactorModel> arraylist,String from){
        this.context = context;
        this.arraylist = arraylist;
        arraylist2=new ArrayList<>();
        arraylist2.addAll(arraylist);
        this.from = from;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
      //  Log.d("LISTT", String.valueOf(contactListFiltered.get(position).getStatus()));
       // Log.d("LISTTQQQQ", ref);
     //   String reff=contactListFiltered.get(position).getStatus();
       // AllContractorData contractorData=contactList.get(position);
       /* if(reff.equals(ref))
        {
            Log.d("LISTTQQQQ", contactListFiltered.get(position).getStageName());
            holder.name.setText(contactListFiltered.get(position).getStageName());
            holder.age.setText(""+contactListFiltered.get(position).getAge());
            holder.height.setText(contactListFiltered.get(position).getHeight());
            Glide.with(context).load(contactListFiltered.get(position).getProfilePic()).into(holder.image);
        }


*/
        if(from.equalsIgnoreCase("")){
            holder.delete_fav.setVisibility(View.GONE);
            holder.add_fav.setVisibility(View.VISIBLE);
        }else{
            holder.delete_fav.setVisibility(View.VISIBLE);
            holder.add_fav.setVisibility(View.GONE);
        }

        ContactorModel model=arraylist.get(position);
        if(model.cntrctFav==0){
                holder.add_fav.setImageResource(R.drawable.fav_empty);
            }else{
                holder.add_fav.setImageResource(R.drawable.fav_filled);
            }

        if(String.valueOf(model.cntrctState).equalsIgnoreCase("New")){
            holder.txtNew.setVisibility(View.VISIBLE);
        }else{
            holder.txtNew.setVisibility(View.GONE);
        }

        Log.d("DATA",model.cntrct_Age+" "+model.cntrct_Height+" "+
                model.cntrct_Weight+" "+model.cntrct_Stats+" "+model.body_type_desc+
                " "+model.hair_color+" "+model.race_desc);

     /*   if(model.cntrct_Height==null)
        {
            holder.sep1.setVisibility(View.GONE);
        }
        else if(model.cntrct_Weight==null)
        {
            holder.sep2.setVisibility(View.GONE);
        }
        else if(model.cntrct_Stats==null)
        {
            holder.sep2.setVisibility(View.GONE);
        }
        else if(model.body_type_desc==null)
        {
            holder.sep6.setVisibility(View.GONE);
        }
        else if(model.body_type==0)
        {
            holder.sep4.setVisibility(View.GONE);
        }
        else if(model.hair_color==null)
        {
            holder.sep5.setVisibility(View.GONE);
        }*/

        holder.name.setText(model.stage_Name);
        holder.age.setText(model.cntrct_Age+" "+"yrs");
        holder.height.setText(model.cntrct_Height);
        holder.third.setText(model.cntrct_Weight+" lbs");
        holder.txt_stat.setText("'"+model.cntrct_Stats+"'");
        holder.txt_bd_type.setText(model.body_type_desc);
        holder.txt_hair_color.setText(model.hair_color);
        holder.txtRaceDesc.setText(model.race_desc);
       // holder.image.setImageResource(personImages.get(position));
        Glide.with(context).load(model.profile_pic).placeholder(R.drawable.lg_logo).into(holder.image);
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open another activity on item click
            }
        });
        holder.relativee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(from.equalsIgnoreCase("")){
                    //Toast.makeText(context, ""+model.id, Toast.LENGTH_SHORT).show();
                    String strid= String.valueOf(model.id);
                    String strimage= model.profile_pic;
                    Details mergePdf = new Details();
                    Bundle bundle = new Bundle();
                    bundle.putString("id",strid);
                    bundle.putString("image",strimage);
                    mergePdf.setArguments(bundle);
                    FragmentManager fragmentManager1 = ((AppCompatActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                    fragmentTransaction1.add(R.id.drawerLayout, mergePdf);
                    fragmentTransaction1.addToBackStack(null);
                    fragmentTransaction1.commit();
//                }
//                else{
//                    Toast.makeText(context, ""+model.id, Toast.LENGTH_SHORT).show();
//                    String strid= String.valueOf(model.id);
//                    String strimage= model.profile_pic;
//                    Details mergePdf = new Details();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("id",strid);
//                    bundle.putString("image",strimage);
//                    mergePdf.setArguments(bundle);
//                    FragmentManager fragmentManager1 = ((AppCompatActivity) context).getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
//                    fragmentTransaction1.replace(R.id.drawerLayout, mergePdf);
//                    fragmentTransaction1.commit();
//                }

            }
        });

        holder.delete_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteFav !=null){
                    deleteFav.setOnDeleteItem(position,model.id);
                }
            }
        });

        holder.add_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addToFav !=null){
                    if(String.valueOf(model.cntrctFav).equalsIgnoreCase("0")){
                        addToFav.setOnAddItem(position,model.id);
                    }else{
                        if(deleteFav !=null){
                            deleteFav.setOnDeleteItem(position,model.id);
                        }
                    }

                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name,age,height,third,txt_stat,txtNew,txt_bd_type,txt_hair_color;
        TextView txtRaceDesc;
        ImageView image,add_fav,delete_fav;
        ImageView sep1,sep2,sep4,sep5,sep6;
        RelativeLayout relativee;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.name);
            age = (TextView) itemView.findViewById(R.id.age);




            txt_stat = (TextView) itemView.findViewById(R.id.txt_stat);
            image = (ImageView) itemView.findViewById(R.id.image);
            add_fav = (ImageView) itemView.findViewById(R.id.add_fav);
            delete_fav = (ImageView) itemView.findViewById(R.id.delete_fav);
            relativee=(RelativeLayout)itemView.findViewById(R.id.relativee);
            txtNew=(TextView) itemView.findViewById(R.id.txtNew);

            sep1=(ImageView)itemView.findViewById(R.id.sep1);
            height = (TextView) itemView.findViewById(R.id.height);

            sep2=(ImageView)itemView.findViewById(R.id.sep2);
            third = (TextView) itemView.findViewById(R.id.third);

            sep4=(ImageView)itemView.findViewById(R.id.sep4);
            txt_bd_type=(TextView) itemView.findViewById(R.id.txt_bd_type);

            sep5=(ImageView)itemView.findViewById(R.id.sep5);
            txt_hair_color=(TextView) itemView.findViewById(R.id.txt_hair_color);

            sep6=(ImageView)itemView.findViewById(R.id.sep6);
            txtRaceDesc=(TextView) itemView.findViewById(R.id.txtRaceDesc);

        }
    }

    public void setOnAddClicked(CustomAdapter.AddToFav addToFav) {
        this.addToFav = (CustomAdapter.AddToFav) addToFav;
    }

    public interface AddToFav {
        void setOnAddItem(int position,int favId);
    }

    public void setOnDeleteClicked(CustomAdapter.DeleteFav deleteFav) {
        this.deleteFav = (CustomAdapter.DeleteFav) deleteFav;
    }

    public interface DeleteFav {
        void setOnDeleteItem(int position,int favId);
    }

    public void updateList(ArrayList<ContactorModel> arraylist,String from){
        this.arraylist = arraylist;
        this.from=from;

        notifyDataSetChanged();
    }

    public void filter(int minage, int maxage) {
        if (minage == 0) {
            arraylist.clear();
            arraylist.addAll(arraylist2);
        } else {
            for (ContactorModel d : arraylist2) {
                if (d.cntrct_Age>=minage && d.cntrct_Age<=maxage) {
                    arraylist.add(d);
                }
            }
        }
        notifyDataSetChanged();
    }

}