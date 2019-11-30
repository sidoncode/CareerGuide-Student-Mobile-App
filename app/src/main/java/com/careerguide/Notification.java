package com.careerguide;

import java.io.Serializable;

public abstract class Notification implements Serializable
{
    private int id;
    private String userName, userId, userPic;

    public Notification(int id, String userName, String userId, String userPic) {
        this.id = id;
        this.userName = userName;
        this.userId = userId;
        this.userPic = userPic;
    }

    abstract public String getMessage();
    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPic() {
        return userPic;
    }

}
