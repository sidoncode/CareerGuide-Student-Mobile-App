package com.careerguide;

import android.util.Log;
import android.widget.ImageView;

public class DataModels {

    private String RestaurantName = "";
    private String Desc = "";
    private String imgSrc;
    private String channel_name;


    public void setRestaurantName(String _RestaturantName) {
        this.RestaurantName = _RestaturantName;
        Log.e("#name123","===>" +_RestaturantName);
    }

    public String getRestaurantName() {
        return this.RestaurantName;
    }

    public void setchannelname( String channel_name) {
        this.channel_name = channel_name;
    }
    public String getchannelname() {
        return this.channel_name;
    }


    public void setImgSrc(String _imgSrc) {
        Log.e("#img","===>" +_imgSrc);
        this.imgSrc = _imgSrc;
    }
    public String getImgSrc() {
        return this.imgSrc;

    }
}
