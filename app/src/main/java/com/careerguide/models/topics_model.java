package com.careerguide.models;

public class topics_model {

    private String uid, name;
    private int count;

    public topics_model(String uid, String name, int count) {
        this.uid = uid;
        this.name = name;
        this.count = count;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
