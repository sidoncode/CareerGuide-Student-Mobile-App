package com.careerguide.youtubeVideo;

public class PlayList {

    private String title;
    private String PlaylistId;

    PlayList(String PlaylistId, String title) {
        this.title = title;
        this.PlaylistId = PlaylistId;
    }

    public String getTitle() {
        return title;
    }
    public String getID() {
        return PlaylistId;
    }
}
