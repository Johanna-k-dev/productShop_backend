package com.greta.productShop.entity;


import java.time.LocalDate;

public class Invoice {

    private int id;
    private LocalDate date;
    private int userId; // Stocke l'ID de l'utilisateur
    private int productId; // Stocke l'ID du produit
    private String orderStatus;

    // 🔹 Constructeur vide
    public Invoice() {}

    // 🔹 Constructeur avec paramètres (pratique pour RowMapper)
    public Invoice(int id, LocalDate date, int userId, int productId, String orderStatus) {
        this.id = id;
        this.date = date;
        this.userId = userId;
        this.productId = productId;
        this.orderStatus = orderStatus;
    }

    // 🔹 Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setUser(User user) {
    }

    public void setProduct(Product product) {
    }

    public void setTotalAmount(double totalAmount) {

    }
}