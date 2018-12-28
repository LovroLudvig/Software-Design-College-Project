package hr.fer.handMadeShopBackend.domain;

import javax.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String address;

    @ManyToOne
    private Town town;
    private String cardNumber;
    private Double price;

    @OneToOne
    private AdOrder adOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public AdOrder getAdOrder() {
        return adOrder;
    }

    public void setAdOrder(AdOrder adOrder) {
        this.adOrder = adOrder;
    }
}
