package com.example.bank.dao;

import com.example.bank.entities.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    @Override
    List<Transaction> findAll();

    List<Transaction> findAllByFromaccountAndToaccount(Long from,Long to);
}
