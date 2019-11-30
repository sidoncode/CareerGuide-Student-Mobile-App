package com.careerguide;

import java.io.Serializable;
import java.util.ArrayList;

public class Counsellor implements Serializable{
    String id;
    String firstName;
    String lastName;
    String picUrl;
    String videocall_channel;
    String title;
    float rating;
    ArrayList<String> expertise = new ArrayList<>();
    String name;
    String imgurl;
    String videourl;


    public Counsellor(String id, String firstName, String lastName, String picUrl,String videocall_channel_name, String title, float rating, ArrayList<String> expertise) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.picUrl = picUrl;
        this.title = title;
        this.rating = rating;
        this.expertise = expertise;
        this.videocall_channel = videocall_channel_name;
    }


    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getvideochannel() {
        return videocall_channel;
    }

    public float getRating() {
        return rating;
    }

    public ArrayList<String> getExpertise() {
        return expertise;
    }

    public String getName() {
        return firstName + " " + lastName;
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
