package com.careerguide;

import java.io.Serializable;

public class CounsellorProfileExpertLevelModel implements Serializable{
    private String expertLevel;

    public CounsellorProfileExpertLevelModel(String expertLevel){
        this.expertLevel=expertLevel;
    }

    public String getExpertLevel() {
        return expertLevel;
    }
}
