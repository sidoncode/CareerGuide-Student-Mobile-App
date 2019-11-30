package com.careerguide;

import java.io.Serializable;

/**
 * Created by Gaurav Gupta(9910781299) on 19/Dec/17-Tuesday.
 */

public class Question implements Serializable
{
    private String section;
    private int srNo;
    private String type;
    private String pessageRef;

    public Question(String section, int srNo, String type, String pessageRef) {
        this.section = section;
        this.srNo = srNo;
        this.type = type;
        this.pessageRef = pessageRef;
    }

    public String getSection() {
        return section;
    }

    public int getSrNo() {
        return srNo;
    }

    public String getType() {
        return type;
    }

    public String getPessageRef() {
        return pessageRef;
    }
}
