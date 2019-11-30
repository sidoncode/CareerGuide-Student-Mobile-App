package com.careerguide;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.MyViewHolder> {

    private Context mContext;
    private List<DataModels> listDataModels;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtRestaurantName , txtdesc;
        public ImageView img;

        public MyViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.imgRestaurant);
            txtRestaurantName = (TextView) view.findViewById(R.id.txtRestaurantName);
            txtdesc = (TextView) view.findViewById(R.id.txtDesc);

        }
    }


    public MyCustomAdapter( Context mContext , List<DataModels> listDataModels) {
        this.mContext = mContext;
        Log.e("#adapter" , "-->" +listDataModels);
        this.listDataModels = listDataModels;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardinterface, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        DataModels objDataModels = listDataModels.get(position);
        holder.txtRestaurantName.setText(objDataModels.getRestaurantName());
        holder.txtdesc.setText("Live Now");

        Glide.with(mContext).load(objDataModels.getImgSrc()).into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext() , ViewerLiveActivity.class);
                intent.putExtra("Channel_name" , objDataModels.getchannelname());
                intent.putExtra("name" , objDataModels.getRestaurantName());
                view.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e("#adapterdd" , "-->" +listDataModels.size());
        return listDataModels.size();
    }

//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        TextView txtRestaurantName, txtDesc;
//        ImageView img;
//
//        public MyViewHolder(View view) {
//            super(view);
//            img = (ImageView) view.findViewById(R.id.imgRestaurant);
//            txtRestaurantName = (TextView) view.findViewById(R.id.txtRestaurantName);
//            txtDesc = (TextView) view.findViewById(R.id.txtDesc);
//
//        }
//    }
}