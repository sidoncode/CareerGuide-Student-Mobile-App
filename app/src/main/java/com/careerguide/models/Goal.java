package com.careerguide.models;

import android.util.Log;

public class Goal {
    private String uid, name, icon_url,cat_placeholder;

    public Goal(String uid, String name, String icon_url,String cat_placeholder) {
        this.uid = uid;
        this.name = name;
        this.icon_url = icon_url;
        this.cat_placeholder=cat_placeholder;
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

    public String getIcon_url() {
        return icon_url;
    }
    public String getCat_placeholder() {
        return cat_placeholder;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }
}
