package com.careerguide;

/**
 * Created by Gaurav Gupta(9910781299) on 19/Dec/17-Tuesday.
 */

public class OptionTextBased extends Option
{
    String value;

    public OptionTextBased(int srNo, String key, String value) {
        super(srNo, key);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
