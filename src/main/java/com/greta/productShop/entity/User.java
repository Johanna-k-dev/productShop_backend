package com.greta.productShop.entity;

public class User {

    private int id;
    private String name;
    private String firstName;
    private String email;
    private String address;
    private String postalNumber;
    private String phoneNumber;
    private String password;
    private String role;

    public User() {}

    public User(int id, String name, String firstName, String email, String address, String postalNumber, String phoneNumber,String password, String role) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.address = address;
        this.postalNumber = postalNumber;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
    }

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
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

    public String getPostalNumber() { return postalNumber; }
    public void setPostalNumber(String postalNumber) { this.postalNumber = postalNumber; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
