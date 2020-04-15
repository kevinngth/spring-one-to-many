package com.customer.accessingdatajpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface CustomerRepository extends CrudRepository<Customer, Long> {
//    Example
//    @Query("from Auction a join a.category c where c.name=:categoryName")
//    public Iterable<Auction> findByCategory(@Param("categoryName") String categoryName);

    @Query("FROM Customer c WHERE c.email=:email")
    List<Customer> findByEmail(@Param("email") String email);

    @Query("FROM Customer c WHERE c.username=:username")
    List<Customer> findByUsername(@Param("username") String username);

    @Query("FROM Customer c WHERE c.uen=:uen")
    List<Customer> findByUen(@Param("uen") int uen);

    @Query("FROM Customer c WHERE c.idNumber=:idNumber")
    List<Customer> findByIdNumber(@Param("idNumber") String idNumber);

    @Query("SELECT c FROM Customer c JOIN c.accounts a GROUP BY c.id HAVING SUM(a.balance) > :threshold ")
    List<Customer> getRichCustomers(@Param("threshold") long threshold);

    @Query("SELECT c FROM Customer c JOIN c.accounts a GROUP BY c.id ORDER BY SUM(a.balance) DESC")
    List<Customer> sortByWealth();

    @Query("SELECT c FROM Customer c JOIN c.accounts a GROUP BY c.id HAVING (SUM(a.balance) > :threshold AND Customer_Type = :customerType)")
    List<Customer> getRichByType(@Param("threshold") long threshold, @Param("customerType") String customerType);
}