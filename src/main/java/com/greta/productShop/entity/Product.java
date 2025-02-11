package com.greta.productShop.entity;

public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String posterPath;



    public Product(){};

    public  Product(int id, String name, double price,int quantity,String description,String posterPath){
        this.id =id;
        this.name =name;
        this.price =price;
        this.quantity = quantity;
        this.description= description;
        this.posterPath=posterPath;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPoster_path(String poster_path) {
        this.posterPath = poster_path;
    }
}
