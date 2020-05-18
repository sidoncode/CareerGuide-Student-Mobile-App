package com.careerguide;


public class BaseLiveCurrentUsersModel {
    private int uid;
    private String fname;
    private String lname;

    public BaseLiveCurrentUsersModel(int uid,String fname,String lname){
        this.uid=uid;
        this.fname=fname;
        this.lname=lname;
    }

    public int getUid(){
        return uid;
    }

    public String getFname(){
        return fname;
    }

    public String getLname(){
        return lname;
    }

}