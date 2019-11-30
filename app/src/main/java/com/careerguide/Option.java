package com.careerguide;

import java.io.Serializable;

/**
 * Created by Gaurav Gupta(9910781299) on 19/Dec/17-Tuesday.
 */

public class Option implements Serializable
{
    int srNo;
    String key;

    public Option(int srNo, String key)
    {
        this.srNo = srNo;
        this.key = key;
    }

    public int getSrNo() {
        return srNo;
    }

    public String getKey() {
        return key;
    }
}
