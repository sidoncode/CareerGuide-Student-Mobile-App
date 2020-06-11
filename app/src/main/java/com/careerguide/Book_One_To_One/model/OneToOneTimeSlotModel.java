package com.careerguide.Book_One_To_One.model;

import org.json.JSONArray;

public class OneToOneTimeSlotModel {
    String slotTime;
    Boolean available;
    String counselorName;
    String counselorId;
    String counselorEmail;
    String counselorImageUrl;



    public OneToOneTimeSlotModel(String slotTime,Boolean available,String counselorName,String counselorId,String counselorEmail,String counselorImageUrl)
    {
        this.slotTime=slotTime;
        this.available=available;
        this.counselorName=counselorName;
        this.counselorId=counselorId;
        this.counselorImageUrl=counselorImageUrl;
        this.counselorEmail=counselorEmail;
    }

    public Boolean getAvailable() {
        return available;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public String getCounselorName() {
        return counselorName;
    }

    public String getCounselorId() {
        return counselorId;
    }

    public String getCounselorImageUrl() {
        return counselorImageUrl;

    }

    public String getCounselorEmail() {
        return counselorEmail;
    }
}





