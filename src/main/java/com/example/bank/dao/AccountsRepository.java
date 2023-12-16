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

    Account findFirstByAccountid(Long id);

    Account findByAccountnumber(Long number);
}
