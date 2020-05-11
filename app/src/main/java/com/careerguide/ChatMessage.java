package com.careerguide;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class ChatMessage implements Serializable{
    private String co_id;
    private String msgId;
    private String message;
    private String image;
    private ChatType chatType;
    private Date date;
    private boolean isReceived;

    enum ChatType
    {
        MSG,
        IMAGE;
    }

    ChatMessage(String co_id, String msgId, String message, String image, ChatType chatType, Date date, boolean isReceived) {
        this.co_id = co_id;
        this.msgId = msgId;
        this.message = message;
        this.image = image;
        this.chatType = chatType;
        this.date = date;
        this.isReceived = isReceived;
    }

    public String getCo_id() {
        return co_id;
    }

    public String getMsgId() {
        return msgId;
    }

    public String getMessage() {
        return message;
    }

    public String getImage() {
        return image;
    }

    ChatType getChatType() {
        return chatType;
    }

    public Date getDate() {
        return date;
    }

    public boolean isReceived() {
        return isReceived;
    }

    public void setId(String id) {
        this.msgId = id;
    }

    public String getTime()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        String minute = Integer.toString(calendar.get(Calendar.MINUTE));
        minute = minute.length() == 1 ? "0" + minute : minute;
        int hour = calendar.get(Calendar.HOUR);
        return (hour == 0 ? 12 : hour) + ":" + minute + " " + (calendar.get(Calendar.AM_PM)==0?"AM":"PM");

    }

    String getDateString()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        return ((calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR));
    }

    boolean isOnSameDate(ChatMessage chatMessage)
    {
        Calendar calendarCurr = Calendar.getInstance();
        calendarCurr.setTimeInMillis(date.getTime());
        Calendar calendarPrev = Calendar.getInstance();
        calendarPrev.setTimeInMillis(chatMessage.getDate().getTime());
        return calendarPrev.get(Calendar.DAY_OF_MONTH) == calendarCurr.get(Calendar.DAY_OF_MONTH) && calendarPrev.get(Calendar.MONTH) == calendarCurr.get(Calendar.MONTH) && calendarPrev.get(Calendar.YEAR) == calendarCurr.get(Calendar.YEAR);
    }

    public void setImage(String image) {
        this.image = image;
    }
}
