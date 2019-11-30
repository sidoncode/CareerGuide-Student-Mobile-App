package com.careerguide;

import java.io.Serializable;

/**
 * Created by Gaurav Gupta(9910781299) on 19/Dec/17-Tuesday.
 */

public class QuestionImageBased extends QuestionTextBased implements Serializable
{
    private String imageUrl;

    public QuestionImageBased(String section, int srNo, String type, String pessageRef, String title, String imageUrl) {
        super(section, srNo, type, pessageRef, title);
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
