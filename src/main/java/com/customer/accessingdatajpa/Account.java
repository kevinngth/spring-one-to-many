package com.customer.accessingdatajpa;

import javax.persistence.*;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String accountType;
    private int balance;

    @ManyToOne( cascade = CascadeType.ALL )
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Version
    private long version;
    protected Account() {}

    public Account( String accountType, int balance) {
        this.accountType = accountType;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountType='" + accountType + '\'' +
                ", balance=" + balance +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getAccountType() {
        return accountType;
    }

    public int getBalance() {
        return balance;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void deposit(int amount) {
        setBalance( getBalance() + amount );
    }

    public void withdraw(int amount) {
        setBalance( getBalance() - amount );
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
