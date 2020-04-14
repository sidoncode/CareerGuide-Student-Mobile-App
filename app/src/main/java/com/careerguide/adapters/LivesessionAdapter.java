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
import com.bumptech.glide.Glide;
import com.careerguide.Album;
import com.careerguide.R;
import com.careerguide.Video_player;

import java.util.List;

/**
 * Created by Rachit
 */
public class LivesessionAdapter extends RecyclerView.Adapter<LivesessionAdapter.MyViewHolder> {

    private Context mContext;
    private List<Album> albumList;
    class MyViewHolder extends RecyclerView.ViewHolder {
         TextView title, count;
         ImageView thumbnail, overflow;

         MyViewHolder(View view) {
            super(view);
            title =  view.findViewById(R.id.title);
            count =  view.findViewById(R.id.count);
            thumbnail =  view.findViewById(R.id.thumbnail);
            overflow = view.findViewById(R.id.overflow);
        }
    }

    public LivesessionAdapter(Context mContext, List<Album> albumList) {
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
        Album album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.count.setText(album.getlive_caption());
        // loading album cover using Glide library
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(view -> {
            Log.e("urls" , "==> " +album.getVideourls().get(position).getVideourl());
            Intent intent = new Intent(view.getContext() , Video_player.class);
            intent.putExtra("live_video_url" , album.getVideourls().get(position).getVideourl());
            intent.putExtra("Fullname" , album.getName());
            intent.putExtra("imgurl" , album.getThumbnail());
            intent.putExtra("host_email" , album.gethost_email());
            view.getContext().startActivity(intent);
        });
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
}
