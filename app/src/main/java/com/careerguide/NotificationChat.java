package com.careerguide;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Gaurav Gupta(9910781299) on 24/Jul/18
 */

public class NotificationChat extends Notification implements Serializable
{
    private ChatMessage chatMessage;

    public NotificationChat(int id, String userId, String userName, String userPic, ChatMessage chatMessage) {
        super(id,userName,userId,userPic);
        this.chatMessage = chatMessage;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    @Override
    public String getMessage() {
        String message = chatMessage.getMessage();
        return message;
    }
}
