package com.careerguide;

public class LiveChatMessage {
    public LiveChatMessage(String name, String content) {
        mName = name;
        mContent = content;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    private String mName;
    private String mContent;
}
