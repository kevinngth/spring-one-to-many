package com.customer.accessingdatajpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Customer_Type")
public abstract class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;

    @Version
    private long version;

    @OneToMany( cascade = CascadeType.ALL )
    private List<Account> accounts = new ArrayList<>();

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "postalCode", column = @Column),
            @AttributeOverride(name = "street", column = @Column),
            @AttributeOverride(name = "city", column = @Column)
    })
    private Address address;

    protected Customer() {}

    public Customer(String username, String email) {
        this.username = username;
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, username='%s']",
                id, username);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
