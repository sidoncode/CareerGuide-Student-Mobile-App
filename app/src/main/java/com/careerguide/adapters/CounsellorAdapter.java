package com.careerguide.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.careerguide.CounsellorProfile;
import com.careerguide.R;
import com.careerguide.models.Counsellor;
import com.careerguide.models.topics_model;

import java.util.List;

public class CounsellorAdapter extends RecyclerView.Adapter<CounsellorAdapter.CounsellorViewHolder> {
    Context context;
    List<Counsellor> CounsellorList;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public class CounsellorViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_container;
        ImageView iv_avatar;
        TextView tv_name, tv_live_minutes;

        public CounsellorViewHolder(View itemView) {
            super(itemView);
            ll_container = itemView.findViewById(R.id.ll_container);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_live_minutes = itemView.findViewById(R.id.tv_live_minutes);

            // This code is used to get the screen dimensions of the user's device
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
//            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams((int)(width/2.5), RecyclerView.LayoutParams.WRAP_CONTENT);
//            params.setMargins(15, 5, 15, 10);
//            ll_container.setLayoutParams(params);

            // Set the ViewHolder width to be a third of the screen size, and height to wrap content
//            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams((int)(width/2.5), RecyclerView.LayoutParams.WRAP_CONTENT);
//            params.setMargins(10, 5, 0, 10);
//            ll_container.setLayoutParams(params);
        }
    }

    public CounsellorAdapter(Context context, List<Counsellor> CounsellorList) {
        this.context = context;
        this.CounsellorList = CounsellorList;
    }

    @Override
    public CounsellorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_counsellors, parent, false);
        return new CounsellorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CounsellorViewHolder holder, int position) {
        final Counsellor Counsellor=CounsellorList.get(position);

        holder.tv_name.setText(Counsellor.getFirst_name()+" "+Counsellor.getLast_name());
        holder.tv_live_minutes.setText("32k live minutes");

       // Glide.with(context).load(Counsellor.getAvatar()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(holder.iv_avatar);
        Glide.with(context).load(Counsellor.getAvatar()).into(holder.iv_avatar);

        holder.ll_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CounsellorProfile.class);
                intent.putExtra("host_name" , Counsellor.getFirst_name()+" "+Counsellor.getLast_name());
                intent.putExtra("host_img" , Counsellor.getAvatar());
                intent.putExtra("host_email" , Counsellor.getUsername());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CounsellorList.size();
    }

}
