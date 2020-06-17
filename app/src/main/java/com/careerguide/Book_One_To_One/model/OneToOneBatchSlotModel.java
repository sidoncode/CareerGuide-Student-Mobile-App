package com.careerguide.Book_One_To_One.model;

import java.util.ArrayList;

public class OneToOneBatchSlotModel {

    private String batchNo="";
    private String batchTiming="";
    private ArrayList<OneToOneTimeSlotModel> bookingSlotsList;

    public OneToOneBatchSlotModel(String batchNo,String batchTiming,ArrayList<OneToOneTimeSlotModel> bookingSlotsList){
        this.batchNo=batchNo;
        this.batchTiming=batchTiming;
        this.bookingSlotsList=bookingSlotsList;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public String getBatchTiming() {
        return batchTiming;
    }

    public ArrayList<OneToOneTimeSlotModel> getBookingSlotsList() {
        return bookingSlotsList;
    }
}
