package com.careerguide.models;

import android.util.Log;

public class TopicGroupItem {
    private String uid, name, author_name, author_user_name, goal_uid, cover_photo, starts_at, topic_group_name;
    private int item_count;

    public TopicGroupItem(String uid, String name, String author_name, String author_user_name, String goal_uid, String cover_photo, String starts_at, String topic_group_name, int item_count) {
        this.uid = uid;
        this.name = name;
        this.author_name = author_name;
        this.author_user_name = author_user_name;
        this.goal_uid = goal_uid;
        this.cover_photo = cover_photo;
        this.starts_at = starts_at;
        this.topic_group_name = topic_group_name;
        this.item_count = item_count;
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

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getCover_photo() {
        Log.e("cover_photo","-->" +cover_photo);
        return cover_photo;
    }

    public void setCover_photo(String cover_photo) {
        this.cover_photo = cover_photo;
    }

    public String getStarts_at() {
        return starts_at;
    }

    public void setStarts_at(String starts_at) {
        this.starts_at = starts_at;
    }

    public String getTopic_group_name() {
        return topic_group_name;
    }

    public void setTopic_group_name(String topic_group_name) {
        this.topic_group_name = topic_group_name;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }

    public String getAuthor_user_name() {
        return author_user_name;
    }

    public void setAuthor_user_name(String author_user_name) {
        this.author_user_name = author_user_name;
    }

    public String getGoal_uid() {
        return goal_uid;
    }

    public void setGoal_uid(String goal_uid) {
        this.goal_uid = goal_uid;
    }
}
