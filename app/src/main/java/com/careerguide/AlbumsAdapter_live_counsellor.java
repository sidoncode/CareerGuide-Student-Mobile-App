package com.careerguide;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;

/**
 * Created by Rachit.
 */
public class AlbumsAdapter_live_counsellor extends RecyclerView.Adapter<AlbumsAdapter_live_counsellor.MyViewHolder> {

    private Context mContext;
    private List<Album_live_counsellor> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        ImageView thumbnail, overflow;

        MyViewHolder(View view) {
            super(view);
            title =  view.findViewById(R.id.title);
            count = view.findViewById(R.id.count);
            thumbnail =view.findViewById(R.id.thumbnail);
            overflow = view.findViewById(R.id.overflow);
        }
    }

    AlbumsAdapter_live_counsellor(Context mContext, List<Album_live_counsellor> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Album_live_counsellor album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.count.setText("Join Now");
        // loading album cover using Glide library
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);
//        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext() , ViewerLiveActivity.class);
//                intent.putExtra("Channel_name" , album.getchannelName());
//                intent.putExtra("name" , album.getName());
//                view.getContext().startActivity(intent);
//            }
//        });
    }

//    private void showPopupMenu(View view) {
//        Log.e("url" , "-->" );
//         inflate menu
//        PopupMenu popup = new PopupMenu(mContext, view);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.menu_album, popup.getMenu());
//        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
//        popup.show();
//
//
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
       // return albumList.size();
        return 1;
    }
}
