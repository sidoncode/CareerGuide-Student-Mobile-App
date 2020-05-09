package com.careerguide;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CurrentLiveCounsellorsAdapter extends RecyclerView.Adapter<CurrentLiveCounsellorsAdapter.MyViewHolder> {

    private Context mContext;
    private List<CurrentLiveCounsellorsModel> listDataModels;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtRestaurantName , txtdesc;
        ImageView img;

        MyViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.imgRestaurant);
            txtRestaurantName = view.findViewById(R.id.txtRestaurantName);
            txtdesc = view.findViewById(R.id.txtDesc);
        }
    }


    public CurrentLiveCounsellorsAdapter(Context mContext, List<CurrentLiveCounsellorsModel> listDataModels) {
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


        CurrentLiveCounsellorsModel objDataModels = listDataModels.get(position);
        holder.txtRestaurantName.setText(objDataModels.getCounsellorName());
        holder.txtdesc.setText("Live Now");
        Glide.with(mContext).load(objDataModels.getImgSrc()).into(holder.img);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext() , ViewerLiveActivity.class);
            Log.e("name-->","" +objDataModels.getchannelname());
            intent.putExtra("Channel_name" , objDataModels.getchannelname());
            intent.putExtra("name" , objDataModels.getCounsellorName());
            view.getContext().startActivity(intent);

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
//            txtDesc = (TextView) view.findViewById(R.id.txk[j[]tDesc);
//
//        }
//    }
}