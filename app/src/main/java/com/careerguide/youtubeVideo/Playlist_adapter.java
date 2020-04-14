package com.careerguide.youtubeVideo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.careerguide.R;
import java.util.List;

public class Playlist_adapter extends RecyclerView.Adapter<Playlist_adapter.MyViewHolder> {

    private List<PlayList> PlayList;
    private Activity activity;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        LinearLayout PlaylistItem;
        MyViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);
            PlaylistItem = view.findViewById(R.id.PlaylistItem);
            tv_name.setOnClickListener(v -> {
            });
        }
    }

    Playlist_adapter(List<PlayList> PlayList_all, Activity activity) {
        this.activity  = activity;
        this.PlayList = PlayList_all;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_playlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PlayList playList_obj = PlayList.get(position);
        Typeface font = Typeface.createFromAsset(activity.getAssets() , "fonts/Montserrat-SemiBold.ttf");
        holder.tv_name.setTypeface(font);
        holder.tv_name.setText(playList_obj.getTitle());
        holder.PlaylistItem.setOnClickListener(v -> {
            Log.e("inside click","-->");
            Intent intent = new Intent(activity , CM_youtubePlaylist.class);
            intent.putExtra("playlist_id",playList_obj.getID() );
            v.getContext().startActivity(intent);
//            Bundle bundle = new Bundle();
//            bundle.putString("playlist_id",playList_obj.getID() );
//            CM_youtubePlaylist framentgobj = new CM_youtubePlaylist();
//            framentgobj.setArguments(bundle);
        });
    }

    @Override
    public int getItemCount() {
        return PlayList.size();
    }
}
