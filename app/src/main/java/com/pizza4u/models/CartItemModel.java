package com.pizza4u.models;

public class CartItemModel {
    String userID;
    String pizzaName;
    String size;
    Float subTotal;
    Float unitPrice;
    int count;
    String docId;

    public CartItemModel() {
    }

    public CartItemModel(String userID, String pizzaName,String size, Float price,Float unitPrice, int count,String docId) {
        this.userID=userID;
        this.size=size;
        this.pizzaName = pizzaName;
        this.subTotal = price;
        this.unitPrice=unitPrice;
        this.count = count;
        this.docId=docId;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public Float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Float subTotal) {
        this.subTotal = subTotal;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
