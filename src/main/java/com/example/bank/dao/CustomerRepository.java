package com.example.bank.dao;

import com.example.bank.entities.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer,Long> {


    Customer findCustomerByCustomerId(Long customerId);

//    @Override
//    @Query(nativeQuery = true, value = "SELECT * FROM customers ORDER BY customer_id")
    List<Customer> findAll();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE customers SET balance = :balance where customer_id = :customerId")
    void updateCustomer(@Param("balance") Double balance,@Param("customerId") long customerId);
}
