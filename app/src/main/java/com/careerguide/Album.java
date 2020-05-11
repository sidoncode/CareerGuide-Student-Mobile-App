package com.careerguide;


import java.util.ArrayList;

/**
 * Created by Lincoln on 18/05/16.
 */
public class Album {
    private String id;
    private String name;
    private String live_caption;
    private String thumbnail;
    private String channel_name;
    private String host_email;
    private String class_cat;
    private String Counsellor_Avatar;
    private String videoviews;
    private String videoid;
    private String[] strArray;
    ArrayList<live_counsellor_session> videourls = new ArrayList<>();
    public Album() {
    }

    Album(String id,String name, String live_caption, String thumbnail, String channel_name, ArrayList<live_counsellor_session> videourls, String host_email, String class_cat, String Counsellor_Avatar,String videoviews,String videoid) {
        this.id=id;
        this.name = name;
        this.host_email = host_email;
        this.live_caption = live_caption;
        this.thumbnail = thumbnail;
        this.channel_name = channel_name;
        this.videourls = videourls;
        this.class_cat = class_cat;
        this.Counsellor_Avatar = Counsellor_Avatar;
        this.videoviews=videoviews;
        this.videoid=videoid;
    }

    public  String getId(){return  id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getlive_caption() {
        return live_caption;
    }

    public void setlive_caption(String live_caption) {
        this.live_caption = live_caption;
    }

    public ArrayList<live_counsellor_session> getVideourls() {
        return videourls;
    }

    public String gethost_email() {
        return host_email;
    }

    public void setHost_email(String thumbnail) {
        this.host_email = host_email;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getchannelName() {
        return channel_name;
    }

    public String getClass_cat(){
        return class_cat;
    }

    public String getCounsellor_Avatar(){
        return Counsellor_Avatar;
    }

    public void setChannelName(String name) {
        this.channel_name = channel_name;
    }

    public String getVideoViews(){ return videoviews; }

    public String getVideoId() { return videoid; }
}