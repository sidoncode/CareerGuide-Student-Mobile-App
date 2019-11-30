package com.careerguide;

import java.io.Serializable;

/**
 * Created by Gaurav Gupta(9910781299) on 19/Dec/17-Tuesday.
 */

public class QuestionTextBased extends Question implements Serializable
{
    private String title;

    public QuestionTextBased(String section, int srNo, String type, String pessageRef, String title) {
        super(section, srNo, type, pessageRef);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
