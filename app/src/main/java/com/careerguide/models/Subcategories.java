package com.careerguide.models;

import android.util.Log;

public class Subcategories {

    private String uid, name , parent_cat;

    public Subcategories(String uid, String name , String parent_cat) {
        this.uid = uid;
        this.name = name;
        this.parent_cat = parent_cat;

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        Log.e("##name","-->" +name);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getparent_cat() {
        Log.e("##name","-->" +parent_cat);
        return parent_cat;
    }

    public void setParent_cat(String parent_cat) {
        this.parent_cat = parent_cat;
    }
}
