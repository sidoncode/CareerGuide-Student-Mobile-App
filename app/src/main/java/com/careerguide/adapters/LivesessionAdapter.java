package com.careerguide.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.careerguide.Album;
import com.careerguide.R;
import com.careerguide.Video_player;
import com.careerguide.youtubeVideo.CommonEducationModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rachit
 * Updated by Albino
 */
public class LivesessionAdapter extends RecyclerView.Adapter<LivesessionAdapter.MyViewHolder> {

    private Context mContext;
    private List<CommonEducationModel> albumList;
    class MyViewHolder extends RecyclerView.ViewHolder {
         TextView title, count,tv_published,session_title;
         ImageView thumbnail, overflow;
         CircleImageView civ_user_profile_pc;

         MyViewHolder(View view) {
            super(view);
            title =  view.findViewById(R.id.title);
            count =  view.findViewById(R.id.count);
            tv_published=view.findViewById(R.id.tv_published);
            civ_user_profile_pc=view.findViewById(R.id.civ_user);
            session_title=view.findViewById(R.id.session_title);
            thumbnail =  view.findViewById(R.id.thumbnail);
            overflow = view.findViewById(R.id.overflow);
        }
    }

    public LivesessionAdapter(Context mContext, List<CommonEducationModel> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
        Log.e("#albumadapter" , "-->" +albumList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.counsellor_feeds, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        CommonEducationModel video = albumList.get(position);
        holder.title.setText(video.getFullName());

        holder.session_title.setText(video.getTitle());
        if(video.getVideoViews().contains("null"))
            holder.count.setText("");//no views yet
        else
            holder.count.setText("Views:"+video.getVideoViews());

        String formatedDate=getPublishedDays(video.getVideourl().substring(video.getVideourl().indexOf("+")+1,video.getVideourl().indexOf("+")+11));
        holder.tv_published.setText(formatedDate);
        //holder.tv_published.setText("2 days ago");//pending, not complete

        // loading album cover using Glide library
        Glide.with(mContext).load(video.getImgurl()).into(holder.thumbnail);
        Glide.with(mContext).load(video.getProfilePicUrl()).into(holder.civ_user_profile_pc);
        holder.thumbnail.setOnClickListener(view -> {
            Log.e("urls" , "==> " +albumList.get(position).getVideourl());
            Intent intent = new Intent(view.getContext() , Video_player.class);
            intent.putExtra("video_id" , video.getVideoId());
            intent.putExtra("live_video_url" , video.getVideourl());
            intent.putExtra("Fullname" , video.getFullName());
            intent.putExtra("imgurl" , video.getImgurl());
            intent.putExtra("title" , video.getTitle());
            intent.putExtra("host_email" , video.getEmail());
            intent.putExtra("video_views" , video.getVideoViews());
            intent.putExtra("host_img" , video.getProfilePicUrl());
            view.getContext().startActivity(intent);
        });
    }

    private String getPublishedDays(String publishedOnDate){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String today = formatter.format(date);
        long days=0;
        Log.i("dddfddd",publishedOnDate+"");

        try {
            Date date1 = formatter.parse(today);
            Date date2 = formatter.parse(publishedOnDate);
            long diff = date1.getTime() - date2.getTime();
            days=TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            Log.i("dddddd",days+"");

            Log.i("dddeddd",days+"");
            if(days==0){
                return "Today";
            }else{
                if(days<31)
                    return days+" day(s) ago";
                else
                if(days>31&days<366){
                    return days/31+" month(s) ago";
                }else
                    return days/365+" year(s) ago";
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return "Long ago ";
        }



    }


//    private void showPopupMenu(View view) {
//
//        Log.e("url" , "-->" );
//         inflate menu
//        PopupMenu popup = new PopupMenu(mContext, view);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.menu_album, popup.getMenu());
//        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
//        popup.show();
//    }

    /**
     * Click listener for popup menu items
     */
//    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
//
//        public MyMenuItemClickListener() {
//        }
//
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//            switch (menuItem.getItemId()) {
//                case R.id.action_add_favourite:
//                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
//                    return true;
//                case R.id.action_play_next:
//                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
//                    return true;
//                default:
//            }
//            return false;
//        }
//    }

    @Override
    public int getItemCount() {
        Log.e("#adapteree" , "-->" +albumList.size());
        return albumList.size();
    }


    public void updateList(List<CommonEducationModel> newvideoList){
        this.albumList.clear();
        this.albumList=newvideoList;
    }
}
