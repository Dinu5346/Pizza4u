package com.pizza4u.models;

public class OrderItemModel {
    String userID;
    String orderID;
    String pizzaName;
    int count;
    Float subTotal;
    String size;

    public OrderItemModel() {
    }

    public OrderItemModel(String userID,String orderID,String pizzaName, int count, Float price,String size) {
        this.userID=userID;
        this.orderID=orderID;
        this.pizzaName = pizzaName;
        this.count = count;
        this.subTotal = price;
        this.size=size;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Float subTotal) {
        this.subTotal = subTotal;
    }
}
