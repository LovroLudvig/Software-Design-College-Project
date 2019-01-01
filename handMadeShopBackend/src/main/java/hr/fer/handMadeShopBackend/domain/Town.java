package hr.fer.handMadeShopBackend.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Town {

    @Id
    private Long postCode;
    private String name;

    public Long getPostCode() {
        return postCode;
    }

    public void setPostCode(Long postCode) {
        this.postCode = postCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
