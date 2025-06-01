package com.greta.productShop.entity;

public class Invoice {
    private int id;
    private int orderId;
    private double totalAmount;
    private String date;

    public Invoice() {}

    public Invoice(int id, int orderId, double totalAmount, String date) {
        this.id = id;
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setTotalAmount(Double total) {
        this.totalAmount = total;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

