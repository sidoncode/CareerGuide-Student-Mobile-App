package com.careerguide.universalsearch;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.careerguide.CounsellorProfile;
import com.careerguide.R;
import com.careerguide.Video_player;
import com.careerguide.blog.activity.CategoryActivity;
import com.careerguide.blog.model.CategoryDetails;
import com.careerguide.models.Counsellor;
import com.careerguide.youtubeVideo.CommonEducationModel;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mayank on 7/1/2020.
 * Mayank Develops
 * mayankdevelops@gmail.com
 */

public class ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private  ArrayList<Object> mList;

    private Context mContext;


    int lastPosition=-1;

    CardListener cardListener;
    private String mContentType;

    public ContentAdapter(Context context, ArrayList<Object> items,CardListener listener) {
        mList = items;
        mContext=context;


        cardListener=listener;

    }

    public interface CardListener {

        void onCardClick( int position);

    }




    void sessionFilter(String query){
        ArrayList<Object> temp = new ArrayList();
        for(Object d: Utility.sessionListForSearch){

            if (d instanceof CommonEducationModel) {
                CommonEducationModel session = (CommonEducationModel)d;
                if(session.getTitle().toLowerCase().contains(query.toLowerCase())){
                    temp.add(d);
                }
            }

        }

        updateList(temp);
    }


    void articleFilter(String query){
        ArrayList<Object> temp = new ArrayList();
        for(Object d: Utility.articleListForSearch){

            if (d instanceof CategoryDetails) {
                CategoryDetails article = (CategoryDetails) d;
                if(article.getTitle().toLowerCase().contains(query.toLowerCase())){
                    temp.add(d);
                }
            }

        }

        updateList(temp);
    }

    void counsellorFilter(String query){

        ArrayList<Object> temp = new ArrayList();
        for(Object d: Utility.counsellorListForSearch) {


            if (d instanceof Counsellor) {
                Counsellor counsellor = (Counsellor) d;
                if ((counsellor.getFirst_name() + counsellor.getLast_name()).toLowerCase().contains(query.toLowerCase())) {
                    temp.add(d);
                }
            }
        }
        updateList(temp);

    }




    public void updateList(ArrayList<Object> list){
        mList = list;
        notifyDataSetChanged();
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {






        switch (viewType) {

            case Utility.COUNSELLOR_SEARCH_TYPE:
                View counsellorViews = LayoutInflater.from(
                        parent.getContext()).inflate(R.layout.item_counsellor_universal_search,
                        parent, false);
                return new CounsellorViewHolder(counsellorViews);


                case Utility.LIVE_SESSION_SEARCH_TYPE:
                View sessionView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_session_universal_search, parent, false);
                return new SessionViewHolder(sessionView);


                case Utility.ARTICLE_SEARCH_TYPE:
                    View articleView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_article_universal_search, parent, false);
                    return new ArticleViewHolder(articleView);


        }
        return null;


    }



    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
     /*   Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.up_from_bottom
                        : R.anim.empty);
        holder.itemView.startAnimation(animation);*/

        lastPosition = position;



        int viewType = getItemViewType(position);
        switch (viewType) {
            case Utility.COUNSELLOR_SEARCH_TYPE:
                final CounsellorViewHolder counsellorViewHolder = (CounsellorViewHolder)holder;
                final Counsellor counsellor = (Counsellor) mList.get(position);
                Glide.with(mContext).load(counsellor.getAvatar()).into(counsellorViewHolder.mProfilePic);
                counsellorViewHolder.mNameView.setText(counsellor.getFirst_name()+" "+counsellor.getLast_name());
                counsellorViewHolder.mLiveMinutesView.setText(String.valueOf(counsellor.getLive_minutes())+" Live Minutes");
                counsellorViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, CounsellorProfile.class);
                        intent.putExtra("host_name" , counsellor.getFirst_name()+" "+counsellor.getLast_name());
                        intent.putExtra("host_img" , counsellor.getAvatar());
                        intent.putExtra("host_email" , counsellor.getUsername());
                        mContext.startActivity(intent);
                    }
                });
                break;
            case Utility.LIVE_SESSION_SEARCH_TYPE:
                final SessionViewHolder sessionViewHolder = (SessionViewHolder) holder;
                final CommonEducationModel session = (CommonEducationModel) mList.get(position);
                Glide.with(mContext).load(session.getImgurl()).into(sessionViewHolder.thumbnail);
                sessionViewHolder.mTitleView.setText(session.getTitle());
                sessionViewHolder.mAuthorView.setText(session.getFullName());
                sessionViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext , Video_player.class);
                        intent.putExtra("video_id" , session.getVideoId());
                        intent.putExtra("live_video_url" , session.getVideourl());
                        intent.putExtra("Fullname" , session.getFullName());
                        intent.putExtra("imgurl" , session.getImgurl());
                        intent.putExtra("title" , session.getTitle());
                        intent.putExtra("host_email" , session.getEmail());
                        intent.putExtra("video_views" , session.getVideoViews());
                        intent.putExtra("host_img" , session.getProfilePicUrl());
                        view.getContext().startActivity(intent);
                    }
                });
                break;
            case Utility.ARTICLE_SEARCH_TYPE:
                final ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
                final CategoryDetails article = (CategoryDetails) mList.get(position);
                Glide.with(mContext).load(article.getPic_url()).into(articleViewHolder.mThumbnail);
                articleViewHolder.mTitleView.setText(article.getTitle());
                articleViewHolder.mDescView.setText(article.getDesc());
                articleViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, CategoryActivity.class);
                        intent.putExtra("data", new Gson().toJson(mList.get(position)));
                        intent.putExtra("pos", position);
                        mContext.startActivity(intent);
                    }
                });
                break;


            default:



        }





    }






    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {



        Object recyclerViewItem = mList.get(position);
        if (recyclerViewItem instanceof CommonEducationModel) {
            return Utility.LIVE_SESSION_SEARCH_TYPE;
        }
        if (recyclerViewItem instanceof Counsellor) {
            return Utility.COUNSELLOR_SEARCH_TYPE;
        }
        if (recyclerViewItem instanceof CategoryDetails) {
            return Utility.ARTICLE_SEARCH_TYPE;
        }

        return Utility.VIDEO_SEARCH_TYPE;
    }


    public class SessionViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mTitleView;
        private final TextView mAuthorView;
        private final ImageView thumbnail;


        public String mItem;

        private SessionViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = view.findViewById(R.id.tv_session_title);
            mAuthorView=view.findViewById(R.id.tv_author);
            thumbnail=view.findViewById(R.id.iv_session_thumb);
        }
    }

    public class CounsellorViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mNameView;
        private final TextView mLiveMinutesView;
        private final ImageView mProfilePic;

        CounsellorViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.tv_name);
            mLiveMinutesView=view.findViewById(R.id.tv_live_minutes);
            mProfilePic=view.findViewById(R.id.iv_avatar);
        }
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mTitleView;
        private final TextView mDescView;
        private final ImageView mThumbnail;

        ArticleViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = view.findViewById(R.id.id_title);
            mDescView=view.findViewById(R.id.id_desc);
            mThumbnail=view.findViewById(R.id.id_pic);
        }
    }
}
