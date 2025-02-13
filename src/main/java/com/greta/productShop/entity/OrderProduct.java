package com.greta.productShop.entity;

public class OrderProduct {

    private int id;
    private int orderId;
    private int userId;
    private int productId;
    private int quantity;

    // Constructeur vide
    public OrderProduct() {}

    // Constructeur avec paramètres
    public OrderProduct(int orderId,int userId, int productId, int quantity) {
        this.orderId = orderId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "id=" + id +
                ", orderId=" + orderId +
                ",userId="+ userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }


}
