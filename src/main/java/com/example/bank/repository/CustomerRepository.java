package com.example.bank.repository;

import com.example.bank.entities.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer,Long> {
    Optional<Customer> findById(Long id);
    List<Customer> findAll();
    Customer findCustomerByLogin(String login);
    @Transactional
    @Modifying
    @Query("UPDATE Customer a SET a.hasaccess = :hasAccess WHERE a.id = :id")
    void updateCustomerById(@Param("id") Long id, @Param("hasAccess") Boolean hasAccess);
}
