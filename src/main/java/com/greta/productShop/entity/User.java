package com.greta.productShop.entity;

public class User {

    private int id;
    private String name;
    private String firstName;
    private String email;
    private String address;
    private int postalNumber;
    private String phoneNumber;

    public User() {}

    public User(int id, String name, String firstName, String email, String address, int postalNumber, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.address = address;
        this.postalNumber = postalNumber;
        this.phoneNumber = phoneNumber;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getPostalNumber() { return postalNumber; }
    public void setPostalNumber(int postalNumber) { this.postalNumber = postalNumber; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
