package com.example.bank.repository;

import com.example.bank.entities.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    @Override
    List<Transaction> findAll();

    List<Transaction> findAllByFromaccountOrToaccount(Long from, Long to);

    List<Transaction> findByFromaccountOrToaccountAndDateBetween(Long from, Long to, LocalDate fromDate, LocalDate toDate);

    @Query("SELECT t FROM Transaction t WHERE t.fromaccount = :fromAccount ORDER BY t.date DESC, t.time DESC LIMIT 1")
    Transaction findLastByFromAccount(@Param("fromAccount") Long fromAccount);
}
