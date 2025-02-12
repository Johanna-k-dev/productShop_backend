package com.greta.productShop.entity;

public class User {
    private int id ;
    private String  name;
    private String firstName;
    private String email;
    private String address;
    private int postalNumber;
    private  String phoneNumber;

    // 🔹 Constructeur vide
    public User() {}

    public User(int id, String name, String firstName, String email, String address, int postalNumber,String phoneNumber){
        this.id= id;
        this.name =name;
        this.firstName = firstName;
        this.email = email;
        this.address =address;
        this.postalNumber= postalNumber;
        this.phoneNumber=phoneNumber;
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

    public String getFirst_name() {
        return firstName;
    }

    public void setFirst_name(String first_name) {
        this.firstName = first_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPostal_number() {
        return postalNumber;
    }

    public void setPostal_number(int postal_number) {
        this.postalNumber = postal_number;
    }

    public String getPhone_number() {
        return phoneNumber;
    }

    public void setPhone_number(String phone_number) {
        this.phoneNumber = phone_number;
    }

}
