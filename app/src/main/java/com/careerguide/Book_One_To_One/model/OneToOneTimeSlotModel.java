package com.careerguide.Book_One_To_One.model;

public class OneToOneTimeSlotModel {
    String slotTime;
    Boolean available;

    public OneToOneTimeSlotModel(String slotTime,Boolean available)
    {
        this.slotTime=slotTime;
        this.available=available;
    }

    public Boolean getAvailable() {
        return available;
    }

    public String getSlotTime() {
        return slotTime;
    }
}
