package com.careerguide;

/**
 * Created by Rachit
 */
public class Album_live_counsellor {
    private String name;
    private String live_caption;
    private String thumbnail;
    private String channel_name;

    Album_live_counsellor(String name, String live_caption, String thumbnail, String channel_name) {
        this.name = name;
        this.live_caption = live_caption;
        this.thumbnail = thumbnail;
        this.channel_name = channel_name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
//
//    public String getlive_caption() {
//        return live_caption;
//    }
//
//    public void setlive_caption(String live_caption) {
//        this.live_caption = live_caption;
//    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

//    public String getchannelName() {
//        return channel_name;
//    }
//
//    public void setChannelName(String name) {
//        this.channel_name = channel_name;
//    }

}
