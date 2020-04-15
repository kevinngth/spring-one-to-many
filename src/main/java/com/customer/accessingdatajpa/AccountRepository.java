package com.customer.accessingdatajpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface AccountRepository extends CrudRepository<Account, Long> {

    List<Account> findByAccountType(String accountType);
}

// understand how logs are supposed to work
// spring: @Autowire