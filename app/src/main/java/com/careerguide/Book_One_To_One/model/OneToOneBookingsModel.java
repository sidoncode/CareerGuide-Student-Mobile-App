package com.careerguide.Book_One_To_One.model;


public class OneToOneBookingsModel {

    String booking_id;
    String sessionHeld;
    String studentName;
    String counselorName;
    String dateBooked;
    String timeSlot;
    String channelName;
    String category;
    String profile_pic;
    String videoUrl;


    public  OneToOneBookingsModel(
            String booking_id,
            String sessionHeld,
            String counselorName,
            String studentName,
            String dateBooked,
            String timeSlot,
            String channelName,
            String category,
            String profile_pic,
            String videoUrl
    ) {
        this.booking_id = booking_id;
        this.sessionHeld = sessionHeld;
        this.counselorName = counselorName;
        this.studentName = studentName;
        this.dateBooked = dateBooked;
        this.timeSlot = timeSlot;
        this.channelName = channelName;
        this.category=category;
        this.profile_pic=profile_pic;
        this.videoUrl=videoUrl;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public String getSessionHeld() {
        return sessionHeld;
    }

    public String getCounselorName() {
        return counselorName;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getDateBooked() {
        return dateBooked;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getCategory() {
        return category;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public String getVideoUrl() { return videoUrl; }
}

