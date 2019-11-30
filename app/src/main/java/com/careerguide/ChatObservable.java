package com.careerguide;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Gaurav Gupta(9910781299) on 24/Jul/18.
 */

public class ChatObservable extends Observable {

    private String senderId;

    private static ArrayList<ChatObservable> chatObservables = new ArrayList<>();

    public ChatObservable(String senderId) {
        this.senderId = senderId;
    }

    public static ChatObservable getInstance(String senderId)
    {
        ChatObservable instance = null;
        for( ChatObservable chatObservable : chatObservables)
        {
            if(chatObservable.getSenderId().equals(senderId))
            {
                instance = chatObservable;
                break;
            }
        }
        if(instance == null)
        {
            instance = new ChatObservable(senderId);
            chatObservables.add(instance);
        }
        return instance;
    }

    public void change(ChatMessage chatMessage)
    {
        setChanged();
        notifyObservers(chatMessage);
    }


    public void change(String[] response)
    {
        setChanged();
        notifyObservers(response);
    }

    public String getSenderId() {
        return senderId;
    }

    public static ArrayList<ChatObservable> getChatObservables() {
        return chatObservables;
    }
}
