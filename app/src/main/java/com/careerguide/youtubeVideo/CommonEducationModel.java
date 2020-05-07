package com.careerguide.youtubeVideo;

import java.io.Serializable;

public class CommonEducationModel  implements Serializable {
    private String userid;
    private String email;
    private String picUrl;
    private String title;
    private String name;
    private String imgurl;
    private String videourl;
    String videoviews;
    private String videoid;


    public CommonEducationModel(String id,String email, String name, String imgurl, String videourl, String title , String picUrl,String videoviews,String videoid) {
        this.userid=id;
        this.email = email;
        this.name = name;
        this.imgurl = imgurl;
        this.videourl = videourl;
        this.title = title;
        this.picUrl = picUrl;
        this.videoviews=videoviews;
        this.videoid=videoid;

    }


    public String getUserId() { return userid; }

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

    public String getVideoViews() { return videoviews; }

    public String getVideoId() { return videoid; }


}
