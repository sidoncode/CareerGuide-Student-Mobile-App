package com.careerguide.youtubeVideo;

import java.io.Serializable;

public class Videos_TEN implements Serializable {
    String email;
    String picUrl;
    String title;
    String name;
    String imgurl;
    String videourl;
    String videoviews;


    public Videos_TEN(String email, String name, String imgurl, String videourl, String title , String picUrl,String videoviews) {
        this.email = email;
        this.name = name;
        this.imgurl = imgurl;
        this.videourl = videourl;
        this.title = title;
        this.picUrl = picUrl;
        this.videoviews=videoviews;
    }


    public String getPicUrl() {
        return picUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getVideourl() {
        return videourl;
    }

    public String getVideoViews() {
        return videoviews;
    }


}
