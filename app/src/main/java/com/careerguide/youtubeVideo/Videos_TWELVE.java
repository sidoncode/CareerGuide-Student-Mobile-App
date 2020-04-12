package com.careerguide.youtubeVideo;

import java.io.Serializable;

public class Videos_TWELVE implements Serializable {
    String email;
    String picUrl;
    String title;
    String name;
    String imgurl;
    String videourl;


    public Videos_TWELVE(String email, String name, String imgurl, String videourl, String title , String picUrl) {
        this.email = email;
        this.name = name;
        this.imgurl = imgurl;
        this.videourl = videourl;
        this.title = title;
        this.picUrl = picUrl;
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


}
