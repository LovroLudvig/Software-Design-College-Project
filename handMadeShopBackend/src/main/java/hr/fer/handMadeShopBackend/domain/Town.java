package hr.fer.handMadeShopBackend.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Town {

    @Id
    private int postCode;
    private String name;

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
