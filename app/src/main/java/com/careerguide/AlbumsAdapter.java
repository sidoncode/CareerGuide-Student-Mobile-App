package com.careerguide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.careerguide.activity.feedDetailActivity;

import java.util.List;

/**
 * Created by Rachit
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Album> albumList;
    private CardView ll_story;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, count , tv_topic_group_name;
        ImageView thumbnail, overflow;
        RecyclerView rv_items;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            count =  view.findViewById(R.id.count);
            thumbnail =  view.findViewById(R.id.thumbnail);
            overflow =  view.findViewById(R.id.overflow);
            ll_story = itemView.findViewById(R.id.ll_story);
            tv_topic_group_name = view.findViewById(R.id.tv_topic_group_name);
            rv_items = itemView.findViewById(R.id.recycler_view);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
            CardView.LayoutParams params = new CardView.LayoutParams((int)(width/1.25), CardView.LayoutParams.MATCH_PARENT);
            params.setMargins(30, 5, 20, 5);
            ll_story.setLayoutParams(params);
        }
    }

    AlbumsAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
        Log.e("#albumadapter" , "-->" +albumList);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_videos_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
            Album album = albumList.get(position);
            holder.title.setText(album.getName());
            holder.count.setText(album.getlive_caption());
            holder.tv_topic_group_name.setText(album.getClass_cat());
            // loading album cover using Glide library
            Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);
           /*holder.thumbnail.setOnClickListener(view -> {
                Log.e("urls" , "==> " +album.getVideourls().get(position).getVideourl());
                Intent intent = new Intent(view.getContext() , feedDetailActivity.class);
                intent.putExtra("live_video_url" , album.getVideourls().get(position).getVideourl());
                intent.putExtra("title" , album.getlive_caption());
                intent.putExtra("class_cat",album.getClass_cat());
                intent.putExtra("Fullname" , album.getName());
                intent.putExtra("imgurl" , album.getThumbnail());
                intent.putExtra("host_email" , album.gethost_email());
                view.getContext().startActivity(intent);
            });
            holder.title.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), CounsellorProfile.class);
                intent.putExtra("host_name" ,album.getName() );
                intent.putExtra("host_img" , album.getCounsellor_Avatar());
                intent.putExtra("host_email" , album.gethost_email());
                v.getContext().startActivity(intent);
            });*/
        holder.itemView.setOnClickListener(view -> {
            Log.e("urls" , "==> " +album.getVideourls().get(position).getVideourl());
            Intent intent = new Intent(view.getContext() , feedDetailActivity.class);
            intent.putExtra("id" , album.getId());
            intent.putExtra("live_video_url" , album.getVideourls().get(position).getVideourl());
            intent.putExtra("title" , album.getlive_caption());
            intent.putExtra("class_cat",album.getClass_cat());
            intent.putExtra("Fullname" , album.getName());
            intent.putExtra("imgurl" , album.getThumbnail());
            intent.putExtra("host_email" , album.gethost_email());
            intent.putExtra("video_views" , album.getVideoViews());
            view.getContext().startActivity(intent);
        });
    }


    private void showPopupMenu(View view) {

        Log.e("url" , "-->" );
        // inflate menu
//        PopupMenu popup = new PopupMenu(mContext, view);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.menu_album, popup.getMenu());
//        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
//        popup.show();


    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        Log.e("#adapteree" , "-->" +albumList.size());
        return albumList.size();
    }
}
