package com.careerguide.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.careerguide.R;
import com.careerguide.blog.RecyclerAdapter;

import java.util.ArrayList;

public class LeaderAdapter extends RecyclerView.Adapter<LeaderAdapter.ViewHolder> {


    Context context;
    ArrayList<String> names;
    ArrayList<String> points;
    ArrayList<String> rank;

    public LeaderAdapter(Context context, ArrayList<String> names, ArrayList<String> points, ArrayList<String> rank) {
        this.context = context;
        this.names = names;
        this.points = points;
        this.rank = rank;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_leader_board,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String n=names.get(position);
        String r=rank.get(position);
        String p=points.get(position);


        holder.rank.setText(r);
        holder.name.setText(n);
        holder.points.setText(p);


    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView rank,name,points;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            rank=itemView.findViewById(R.id.rank);
            name=itemView.findViewById(R.id.name);
            points=itemView.findViewById(R.id.points);
        }
    }
}
