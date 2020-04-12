package com.careerguide.blog;

public class DataMembers {

    int postId;
    String postTitle;
    String postCode;
    String imgurl;
    String posturl;
    String postDesc;

    public DataMembers(int postId, String postTitle, String postCode, String imgurl, String posturl , String postDesc){
        this.postId = postId;
        this.imgurl = imgurl;
        this.postCode = postCode;
        this.postTitle = postTitle;
        this.posturl = posturl;
        this.postDesc = postDesc;
    }

    public String getPostTitle(){
        return  postTitle;
    }

    public int getPostId(){
        return  postId;
    }

    public String getPostCode(){
        return  postCode;
    }

    public String getPosturl(){
        return  posturl;
    }

    public String getImgurl(){
        return  imgurl;
    }

    public String getPostDesc(){
        return  postDesc;
    }

}