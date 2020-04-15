package com.customer.accessingdatajpa;

import javax.persistence.*;

@Entity
@DiscriminatorValue("Individual")
public class Individual extends Customer {

    @Column
    private String idNumber;

    public Individual(String username, String email, String idNumber) {
        super(username, email);
        this.idNumber = idNumber;
    }

    public Individual() {}

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
}
