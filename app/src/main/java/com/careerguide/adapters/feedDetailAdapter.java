package com.careerguide.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.careerguide.Album;
import com.careerguide.R;
import com.careerguide.models.topics_model;
import java.util.List;

public class feedDetailAdapter extends RecyclerView.Adapter<feedDetailAdapter.feedDetailViewHolder> {
    Context context;
    List<topics_model> allTopicsItemList;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public class feedDetailViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_all_topic_item_container;
        TextView tv_name, tv_courses;

        public feedDetailViewHolder(View itemView) {
            super(itemView);
            ll_all_topic_item_container = itemView.findViewById(R.id.ll_all_topic_item_container);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_courses = itemView.findViewById(R.id.tv_courses);

            // This code is used to get the screen dimensions of the user's device
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
        }
    }

    public feedDetailAdapter(Context context, List<Album> allTopicsItemList) {
        this.context = context;
       // this.allTopicsItemList = allTopicsItemList;
    }

    @Override
    public feedDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_all_topics_item, parent, false);
        return new feedDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final feedDetailViewHolder holder, int position) {
        final topics_model allTopicsItem=allTopicsItemList.get(position);
        Log.e("#insdie adapter ","--> ");
        holder.tv_name.setText(allTopicsItem.getName());
        holder.tv_courses.setText(allTopicsItem.getCount()+" Classes");
        holder.ll_all_topic_item_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, TopicActivity.class);
//                intent.putExtra("title", allTopicsItem.getName());
//                intent.putExtra("topic_group_id", allTopicsItem.getUid());
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allTopicsItemList.size();
    }

}
