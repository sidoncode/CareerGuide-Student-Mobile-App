package com.careerguide.payment;

public class PaymentDetailModel {

    private String name, email, order_id, amount, payment_status, datetime,validate_till;

    public PaymentDetailModel(String name, String email, String order_id, String amount, String payment_status, String datetime, String validate_till) {
        this.name = name;
        this.email = email;
        this.order_id = order_id;
        this.amount = amount;
        this.payment_status = payment_status;
        this.datetime = datetime;
        this.validate_till = validate_till;
    }

    public String getemail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public String getOrder_id() {
        return order_id;
    }
    public String getAmount() {
        return amount;
    }
    public String getPayment_status() {
        return payment_status;
    }
    public String getDatetime() {
        return datetime;
    }
    public String getValidate_till() {
        return validate_till;
    }


    public void setName(String name) {
        this.name = name;
    }


}
