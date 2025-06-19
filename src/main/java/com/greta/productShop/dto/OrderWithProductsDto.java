package com.greta.productShop.dto;


import com.greta.productShop.entity.OrderProduct;

import java.util.List;

public class OrderWithProductsDto {
    private double total;
    private List<OrderProduct> items;

    // Getter pour total
    public double getTotal() {
        return total;
    }

    // Setter pour total
    public void setTotal(double total) {
        this.total = total;
    }

    // Getter pour items
    public List<OrderProduct> getItems() {
        return items;
    }

    // Setter pour items
    public void setItems(List<OrderProduct> items) {
        this.items = items;
    }

}
