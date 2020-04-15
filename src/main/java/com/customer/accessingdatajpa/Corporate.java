package com.customer.accessingdatajpa;

import javax.persistence.*;

@Entity
@DiscriminatorValue("Corporate")
public class Corporate extends Customer {

    @Column
    private int uen;

    public Corporate(String username, String email, int uen) {
        super(username, email);
        this.uen = uen;
    }

    public Corporate() {}

    public int getUen() {
        return uen;
    }

    public void setUen(int uen) {
        this.uen = uen;
    }
}
