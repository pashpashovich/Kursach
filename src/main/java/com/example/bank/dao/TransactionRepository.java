package com.example.bank.dao;

import com.example.bank.entities.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    @Override
    List<Transaction> findAll();

    List<Transaction> findAllByFromaccountOrToaccount(Long from, Long to);

    List<Transaction> findByFromaccountOrToaccountAndDateBetween(Long from, Long to, LocalDate fromDate, LocalDate toDate);

    Transaction findFirstByFromaccountOrderByDateDescTimeDesc(Long fromAccount);
}
