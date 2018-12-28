package hr.fer.handMadeShopBackend.domain;

import javax.persistence.*;

@Entity
public class AdOrder {

    @Id
    @GeneratedValue
    private Long id;
    private Double price;

    @ManyToOne
    private OrderStatus status;

    @ManyToOne
    private Dimension dimension;

    @ManyToOne
    private Style style;

    @ManyToOne
    private Advertisement advertisement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }
}
