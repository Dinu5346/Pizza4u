package com.pizza4u.models;

import com.google.type.DateTime;

public class OrderModel {
    String userID;
    String orderId;
    String status;
    Float total;
    DateTime date;
    Double longitude;
    Double latitude;


    public OrderModel() {
    }

    public OrderModel(String userID,String orderId,String status, Float price, DateTime date, Double longitude, Double latitude) {
        this.userID=userID;
        this.orderId = orderId;
        this.status=status;
        this.total = price;
        this.date = date;
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

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }
}
