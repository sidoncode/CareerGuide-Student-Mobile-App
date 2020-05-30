package com.careerguide.youtubeVideo;

import java.io.Serializable;

public class CommonEducationModel  implements Serializable {
    private String userid;
    private String email;
    private String profilepicurl;
    private String title;
    private String name;
    private String imgurl;
    private String videourl;
    private String videoviews;
    private String videoid;
    private String videoCategory;


    public CommonEducationModel(String id,String email, String name, String imgurl, String videourl, String title , String profilepicurl,String videoviews,String videoid,String videoCategory) {
        this.userid=id;
        this.email = email;
        this.name = name;
        this.imgurl = imgurl;
        this.videourl = videourl;
        this.title = title;
        this.profilepicurl = profilepicurl;
        this.videoviews=videoviews;
        this.videoid=videoid;
        this.videoCategory=videoCategory;

    }


    public String getUserId() { return userid; }

    public String getProfilePicUrl() {
        return profilepicurl;
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

    public String getVideoCategory() { return videoCategory; }
}
