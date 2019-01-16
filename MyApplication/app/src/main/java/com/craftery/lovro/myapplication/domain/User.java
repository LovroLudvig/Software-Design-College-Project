package com.craftery.lovro.myapplication.domain;

import com.squareup.moshi.Json;

import java.util.List;

public class User {

    @Json(name = "email")
    private String email;

    @Json(name = "passwordHash")
    private String password;

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

    @Json(name = "roles")
    private List<Role> roles;

    @Json(name = "userStatus")
    private Status userStatus;

    @Json(name = "town")
    private Town town;

    public User(String email,String password,String username,String name,String cardNumber,String address,List<Role> roles,Status userStatus,Town town){
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.cardNumber = cardNumber;
        this.name = name;
        this.roles = roles;
        this.town = town;
        this.userStatus = userStatus;
    }

    public User(String username){
        this.username=username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setUserStatus(Status userStatus) {
        this.userStatus = userStatus;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public User(String email, String username, String name, String cardNumber, String address, Town town){
        this.username = username;
        this.email = email;
        this.address = address;
        this.cardNumber = cardNumber;
        this.name = name;
        this.town = town;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Town getTown() {
        return town;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public Status getUserStatus() {
        return userStatus;
    }

}
