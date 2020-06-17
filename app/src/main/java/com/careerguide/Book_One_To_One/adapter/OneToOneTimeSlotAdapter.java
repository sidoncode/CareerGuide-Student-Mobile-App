package com.careerguide.Book_One_To_One.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.careerguide.Book_One_To_One.activity.NewOneToOneRegisteration;
import com.careerguide.Book_One_To_One.model.OneToOneTimeSlotModel;
import com.careerguide.R;

import java.util.ArrayList;

class OneToOneTimeSlotAdapter extends RecyclerView.Adapter<OneToOneTimeSlotAdapter.MyViewHolder> {

    private Context mContext;
    private int parentPostion;
    private OneToOneBatchSlotAdapter parentAdapter;



    private ArrayList<OneToOneTimeSlotModel> timeSlotsList=new ArrayList<>();


    public OneToOneTimeSlotAdapter(Context mContext,OneToOneBatchSlotAdapter parentAdapter,Integer parentPostion, ArrayList<OneToOneTimeSlotModel> timeSlotsList) {
        this.mContext=mContext;
        this.timeSlotsList.clear();
        notifyDataSetChanged();
        this.timeSlotsList=timeSlotsList;
        this.parentAdapter=parentAdapter;
        this.parentPostion=parentPostion;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTimeSlot;
        MyViewHolder(View view) {
            super(view);
            textViewTimeSlot=view.findViewById(R.id.textViewTimeSlot);

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_to_one_time_slot, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textViewTimeSlot.setText(timeSlotsList.get(position).getSlotTime());
        if (timeSlotsList.get(position).getAvailable())
            holder.textViewTimeSlot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View clickedView) {
                    parentAdapter.updateClickedTimeSlot(clickedView);
                    ((NewOneToOneRegisteration) mContext).setHostId(timeSlotsList.get(position).getCounselorId());
                    ((NewOneToOneRegisteration) mContext).setHostFullName(timeSlotsList.get(position).getCounselorName());
                    ((NewOneToOneRegisteration) mContext).setHostImageUrl(timeSlotsList.get(position).getCounselorImageUrl());
                    ((NewOneToOneRegisteration) mContext).setHostEmail(timeSlotsList.get(position).getCounselorEmail());
                }
            });
        else{
            holder.textViewTimeSlot.setBackground(mContext.getResources().getDrawable(R.drawable.round_corner_grey));//use for booked slots
        }

    }

    @Override
    public int getItemCount() {
        return timeSlotsList.size();
    }


}
