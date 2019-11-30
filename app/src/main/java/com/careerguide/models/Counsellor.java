package com.careerguide.models;

import android.util.Log;

public class Counsellor {


    private String uid, username, first_name, last_name, avatar;
    private double live_minutes;

    public Counsellor(String uid, String username, String first_name, String last_name, String avatar, double live_minutes) {
        this.uid = uid;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.avatar = avatar;
        this.live_minutes = live_minutes;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAvatar() {
        Log.e("Avatar" , "-->" + avatar);
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public double getLive_minutes() {
        return live_minutes;
    }

    public void setLive_minutes(double live_minutes) {
        this.live_minutes = live_minutes;
    }

}
