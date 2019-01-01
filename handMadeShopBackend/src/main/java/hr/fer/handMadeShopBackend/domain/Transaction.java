package hr.fer.handMadeShopBackend.domain;

import javax.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private AdOrder adOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdOrder getAdOrder() {
        return adOrder;
    }

    public void setAdOrder(AdOrder adOrder) {
        this.adOrder = adOrder;
    }

}
