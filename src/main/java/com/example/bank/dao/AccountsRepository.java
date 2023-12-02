package com.example.bank.dao;

import com.example.bank.entities.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountsRepository extends CrudRepository<Account,Long> {


    List<Account> findAll();

   List<Account> findAllByAccountid(Long id);

    Account findByAccountid(Long id);

    Account findFirstByAccountid(Long id);

    Account findByAccountnumber(Long number);
    @Transactional
    @Modifying
    @Query("UPDATE Account a SET a.balance = :newBalance WHERE a.accountnumber = :accountNumber")
    void updateAccountByAccountnumber(@Param("accountNumber") Long accountNumber, @Param("newBalance") Double newBalance);

//    @Transactional
//    @Modifying
//    @Query(nativeQuery = true, value = "UPDATE customers SET balance = :balance where customer_id = :customerId")
//    void updateCustomer(@Param("balance") Double balance,@Param("customerId") long customerId);
}
