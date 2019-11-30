package com.careerguide;

import java.io.Serializable;
import java.util.ArrayList;

public class live_counsellor_session implements Serializable {
    String id;
    String picUrl;
    String title;
    String name;
    String imgurl;
    String videourl;


    public live_counsellor_session(String email, String name,String imgurl,String videourl, String title , String picUrl) {
        this.id = email;
        this.name = name;
        this.imgurl = imgurl;
        this.videourl = videourl;
        this.title = title;
        this.picUrl = picUrl;
    }

    public String getId() {
        return id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getTitle() {
        return title;
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
