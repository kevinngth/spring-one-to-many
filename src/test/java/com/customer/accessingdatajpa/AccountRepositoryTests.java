package com.customer.accessingdatajpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@OpRepoTest
public class AccountRepositoryTests {
    @Autowired
    AccountRepository repo;
    @Autowired
    EntityManager em;

    void flushAndClear() {
        em.flush();
        em.clear();
    }

    @Test
    void shouldReturnCustomerWhoOwnsAccount() {
        Account a = new Account("Savings", 10000);
        Customer c = new Corporate("Company", "company@gmail.com", 1234567890 );
        a.setCustomer( c );
        repo.save(a);
        flushAndClear();
        Account result = repo.findByAccountType("Savings").get( 0 );
        assertThat( result.getCustomer().getUsername() ).isEqualTo( "Company" );
    }
}
