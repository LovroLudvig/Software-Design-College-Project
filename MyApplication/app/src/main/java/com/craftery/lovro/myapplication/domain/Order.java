package com.craftery.lovro.myapplication.domain;

import com.squareup.moshi.Json;

public class Order {

    @Json(name = "id")
    private Long id;

    @Json(name = "price")
    private Double price;

    @Json(name = "name")
    private String name;

    @Json(name = "lastName")
    private String lastName;

    @Json(name = "address")
    private String address;

    @Json(name = "town")
    private Town town;

    @Json(name = "cardNumber")
    private String cardNumber;

    @Json(name = "status")
    private OrderStatus status;

    @Json(name = "dimension")
    private Dimension dimension;

    @Json(name = "style")
    private Style style;

    @Json(name = "advertisement")
    private Offer offer;

    public Order(Long id, Double price, String name, String lastName, String address, Town town,String cardNumber,OrderStatus status,Dimension dimension,Style style,Offer offer){
        this.id = id;
        this.price = price;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.town = town;
        this.cardNumber = cardNumber;
        this.status = status;
        this.dimension = dimension;
        this.style = style;
        this.offer = offer;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public Double getPrice() {
        return price;
    }

    public Town getTown() {
        return town;
    }

    public String getAddress() {
        return address;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public Offer getOffer() {
        return offer;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getLastName() {
        return lastName;
    }

    public Style getStyle() {
        return style;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public void setTown(Town town) {
        this.town = town;
    }
}
