package com.pizza4u.models;

import com.google.type.DateTime;

public class OrderModel {
    String userID;
    String orderId;
    String status;
    Double total;
    String dateTime;
    String longitude;
    String latitude;


    public OrderModel() {
    }

    public OrderModel(String userID,String orderId,String status, Double price, String date, String longitude, String latitude) {
        this.userID=userID;
        this.orderId = orderId;
        this.status=status;
        this.total = price;
        this.dateTime = date;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
