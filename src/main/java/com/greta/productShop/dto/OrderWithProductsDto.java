package com.greta.productShop.dto;


import com.greta.productShop.entity.OrderProduct;

import java.util.List;

public class OrderWithProductsDto {
    private double total;
    private List<OrderProduct> items;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<OrderProduct> getItems() {
        return items;
    }

    public void setItems(List<OrderProduct> items) {
        this.items = items;
    }

}
