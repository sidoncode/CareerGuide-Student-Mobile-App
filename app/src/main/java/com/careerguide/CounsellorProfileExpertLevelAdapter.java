package com.careerguide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CounsellorProfileExpertLevelAdapter extends RecyclerView.Adapter<CounsellorProfileExpertLevelAdapter.MyViewHolder>  {

    private Context mContext;
    private List<CounsellorProfileExpertLevelModel> student_education_level_list;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewExpertLevel;

        MyViewHolder(View view) {
            super(view);
            textViewExpertLevel = view.findViewById(R.id.textViewExpertLevel);
        }
    }


    public CounsellorProfileExpertLevelAdapter(Context mContext, List<CounsellorProfileExpertLevelModel> student_education_level_list) {
        this.mContext = mContext;
        Log.e("#adapter" , "-->" +student_education_level_list);
        this.student_education_level_list = student_education_level_list;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.counselor_profile_expert_level_recyclerview_single_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        CounsellorProfileExpertLevelModel objDataModels = student_education_level_list.get(position);
        holder.textViewExpertLevel.setText(objDataModels.getExpertLevel());

    }

    @Override
    public int getItemCount() {
        Log.e("#adapterdd" , "-->" +student_education_level_list.size());
        return student_education_level_list.size();
    }
}
