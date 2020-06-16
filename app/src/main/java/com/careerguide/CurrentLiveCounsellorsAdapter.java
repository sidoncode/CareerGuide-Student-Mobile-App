package com.careerguide;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.careerguide.exoplayer.utils.PublicFunctions;
import com.careerguide.youtubeVideo.youtubeFeedDetail;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.io.File;
import java.util.List;

public class CurrentLiveCounsellorsAdapter extends RecyclerView.Adapter<CurrentLiveCounsellorsAdapter.MyViewHolder> {

    private Context mContext;
    private List<CurrentLiveCounsellorsModel> listDataModels;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtCounsellorName , txtdesc,shareWithOthers;
        LinearLayout backgroundLayout;
        ImageView imgCounsellor;
        LinearLayout live_msg;

        MyViewHolder(View view) {
            super(view);
            imgCounsellor = view.findViewById(R.id.imgCounsellor);
            txtCounsellorName = view.findViewById(R.id.txtCounsellorName);
            txtdesc = view.findViewById(R.id.txtDesc);
            backgroundLayout = view.findViewById(R.id.backgroundLayout);
            live_msg=view.findViewById(R.id.live_msg);
            shareWithOthers=view.findViewById(R.id.shareWithOthers);
        }
    }


    public CurrentLiveCounsellorsAdapter(Context mContext, List<CurrentLiveCounsellorsModel> listDataModels) {
        this.mContext = mContext;
        Log.e("#adapter" , "-->" +listDataModels);
        this.listDataModels = listDataModels;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_live_counsellors_single_item_recyclerview, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        CurrentLiveCounsellorsModel objDataModels = listDataModels.get(position);
        holder.txtCounsellorName.setText(objDataModels.getCounsellorName());
        holder.txtdesc.setText(objDataModels.getscheduleDescription());
        if(objDataModels.getscheduleDescription().contains("LIVE AT")){
            holder.txtCounsellorName.setTextColor(Color.BLACK);
            holder.txtdesc.setTextColor(Color.GRAY);
            holder.shareWithOthers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    video_sharee(objDataModels);
                }
            });
        }
        else {
            if (!objDataModels.getchannelname().contentEquals(""))
                 holder.live_msg.setVisibility(View.VISIBLE);
            holder.txtCounsellorName.setTextColor(Color.BLACK);
            holder.txtdesc.setTextColor(Color.BLACK);


            holder.shareWithOthers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    video_sharee(objDataModels);
                }
            });
        }
        Log.i("desssss",holder.txtdesc.getText().toString()+"___"+objDataModels.getscheduleDescription());

        if(objDataModels.getCounsellorName().contains("FaceBook.com")) {//handles facebook live
            Glide.with(mContext).load(objDataModels.getImgSrc()).into(holder.imgCounsellor);
            holder.backgroundLayout.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext() , youtubeFeedDetail.class);
                intent.putExtra("data_id" , objDataModels.getchannelname());//gets the channel id
                view.getContext().startActivity(intent);
            });
        }else {

            if (!objDataModels.getscheduleDescription().equals("")) {//handle if now counsellor is not live
                RequestOptions requestOptions = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                        .skipMemoryCache(true);

                holder.imgCounsellor.setImageResource(0);

                Glide.with(mContext).applyDefaultRequestOptions(requestOptions).load(objDataModels.getImgSrc()).into(holder.imgCounsellor);
                holder.backgroundLayout.setOnClickListener(view -> {
                    Intent intent = new Intent(view.getContext(), ViewerLiveActivity.class);
                    Log.e("name-->", "" + objDataModels.getchannelname());
                    intent.putExtra("Channel_name", objDataModels.getchannelname());
                    intent.putExtra("name", objDataModels.getCounsellorName());
                    intent.putExtra("imgurl" , objDataModels.getImgSrc());
                    intent.putExtra("title" , objDataModels.getTitle());
                    intent.putExtra("scheduledesc" , objDataModels.getscheduleDescription());
                    intent.putExtra("channel_link" , objDataModels.getchannelname());
                    view.getContext().startActivity(intent);

                });
            }
        }

    }

    @Override
    public int getItemCount() {
        Log.e("#adapterdd" , "-->" +listDataModels.size());
        return listDataModels.size();
    }


    public void video_sharee(CurrentLiveCounsellorsModel objDataModels) {


        String Channel_name = objDataModels.getchannelname();

        String Fullname = objDataModels.getCounsellorName();

        String title=objDataModels.getTitle();

        String host_image= objDataModels.getImgSrc();

        String scheduledesc= "\nGuide "+Fullname+" will be "+objDataModels.getscheduleDescription()+"\n Let me recommend this LIVE STREAM from CareerGuide.com -Must watch for you.\n Share with your friends and family too.  ";

        String fileName = host_image.substring(host_image.lastIndexOf('/') + 1);



        if (PublicFunctions.checkAccessStoragePermission ( (HomeActivity)mContext )) {
            if (!Utility.checkFileExist(fileName)) {
                Utility.downloadImage(fileName + "", host_image + "",(HomeActivity)mContext);
            }

            Toast.makeText(mContext,"Opening apps...",Toast.LENGTH_LONG).show();

            DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse("https://play.google.com/store/apps/details?id=com.careerguide&hl=en_US&sessionDetails={\"channel_name\":\""+Channel_name+"\",\"host_name\":\""+Fullname+"\",\"host_image\":\""+fileName+"\",\"schedule_desc\":\""+scheduledesc+"\",\"title\":\""+title+"\"}"))
                    .setDynamicLinkDomain("careerguidelivestream.page.link")
                    // Open links with this app on Android
                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.careerguide").build())
                    .setGoogleAnalyticsParameters(
                            new DynamicLink.GoogleAnalyticsParameters.Builder()
                                    .setSource("video")
                                    .setMedium("anyone")
                                    .setCampaign("example-video")
                                    .build())
                    .setSocialMetaTagParameters(
                            new DynamicLink.SocialMetaTagParameters.Builder()
                                    .setTitle(title+" by Guide "+Fullname+" from CareerGuide.com")
                                    .setDescription(scheduledesc)
                                    .setImageUrl(Uri.parse(host_image))
                                    .build())
                    .buildDynamicLink();
            Log.e("main", "Long refer Link"+ dynamicLink.getUri());
            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLongLink(dynamicLink.getUri())
                    .buildShortDynamicLink()
                    .addOnCompleteListener((HomeActivity)mContext, task -> {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Log.e("main","short Link" + shortLink);
                            Log.e("main","short Link" + flowchartLink);
                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                            StrictMode.setVmPolicy(builder.build());

                            File imgFile = Utility.getFile(fileName);
                            if (imgFile!=null){
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("image/*");
                                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imgFile.toString()) );
                                shareIntent.putExtra(Intent.EXTRA_TEXT, scheduledesc+ shortLink );
                                mContext.startActivity(Intent.createChooser(shareIntent, "Choose an app"));
                            }else {

                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("plain/text");
                                shareIntent.putExtra(Intent.EXTRA_TEXT, scheduledesc+ shortLink );
                                mContext.startActivity(Intent.createChooser(shareIntent, "Choose an app"));
                            }

                        } else
                        {
                            Log.e("Error","error--> "+task.getException());
                            // Error
                            // ...
                        }
                    });
        }
    }




//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        TextView txtRestaurantName, txtDesc;
//        ImageView img;
//
//        public MyViewHolder(View view) {
//            super(view);
//            img = (ImageView) view.findViewById(R.id.imgRestaurant);
//            txtRestaurantName = (TextView) view.findViewById(R.id.txtRestaurantName);
//            txtDesc = (TextView) view.findViewById(R.id.txk[j[]tDesc);
//
//        }
//    }
}