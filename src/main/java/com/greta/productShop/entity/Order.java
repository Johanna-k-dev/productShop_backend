package com.greta.productShop.entity;

import java.time.LocalDate;

public class Order {
    private int id;
    private int userId;
    private LocalDate date;
    private Double total;

    public Order() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}
