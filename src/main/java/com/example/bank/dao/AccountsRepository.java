package com.example.bank.dao;

import com.example.bank.entities.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountsRepository extends CrudRepository<Account,Long> {


    List<Account> findAll();


   List<Account> findAllByAccountid(Long id);

//    @Transactional
//    @Modifying
//    @Query(nativeQuery = true, value = "UPDATE customers SET balance = :balance where customer_id = :customerId")
//    void updateCustomer(@Param("balance") Double balance,@Param("customerId") long customerId);
}
