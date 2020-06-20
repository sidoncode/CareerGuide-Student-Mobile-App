package com.careerguide.Book_One_To_One.model;

public class OneToOneChatModel {
    private int mUid;
    private String mUserImage;
    private String mMessage;
    private String mtimeSent;
    private boolean msent_received;


    public OneToOneChatModel(int uid, String message,String timeSent,String userImage,boolean sent_received) {
        mUid=uid;
        mMessage = message;
        mtimeSent=timeSent;
        mUserImage=userImage;
        msent_received=sent_received;
    }

    public int getmUid() {
        return mUid;
    }

    public String getmMessage() {
        return mMessage;
    }

    public String getmtimeSent() {
        return mtimeSent;
    }

    public String getmUserImage() {
        return mUserImage;
    }

    public boolean getmSent_Received() {
        return msent_received;
    }


}

