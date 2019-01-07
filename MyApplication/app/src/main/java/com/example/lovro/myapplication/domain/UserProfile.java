package com.example.lovro.myapplication.domain;

import com.squareup.moshi.Json;

public class UserProfile {
    @Json(name = "username")
    private String username;

    @Json(name = "name")
    private String name;

    @Json(name = "lastName")
    private String lastName;

    @Json(name = "cardNumber")
    private String cardNumber;

    @Json(name = "address")
    private String address;

    @Json(name = "town")
    private Town town;

    @Json(name = "email")
    private String email;

    public UserProfile(String username, String name, String lastName, String cardNumber, String address, Town town, String email) {
        this.username = username;
        this.name = name;
        this.lastName = lastName;
        this.cardNumber = cardNumber;
        this.address = address;
        this.town = town;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
