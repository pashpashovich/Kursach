package com.example.bank.repository;

import com.example.bank.entities.Account;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AccountsRepository extends CrudRepository<Account,Long> {
    List<Account> findAll();
   List<Account> findAllByAccountid(Long id);

    Account findFirstByAccountid(Long id);

    Account findByAccountnumber(Long number);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = ROUND(a.balance * (1 + :interest / 100.0), 1)")
    void increaseAllBalancesByInterest(int interest);
}
