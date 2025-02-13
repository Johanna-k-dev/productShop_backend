package com.greta.productShop.entity;


import java.time.LocalDate;

public class Order {

    private int id;
    private LocalDate date;

    private Double total;

    // Constructeur vide
    public Order() {}

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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}