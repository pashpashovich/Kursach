package com.example.bank.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;
    private String type_tr;
    private Long fromaccount;
    private Long toaccount;
    private double amount;
    private LocalDate date;
    private LocalTime time;

    public Transaction() {

    }

    public Transaction(Long transactionId, String type_tr, Long fromaccount, Long toaccount, double amount, LocalDate date, LocalTime time) {
        this.transactionId = transactionId;
        this.type_tr = type_tr;
        this.fromaccount = fromaccount;
        this.toaccount = toaccount;
        this.amount = amount;
        this.date = date;
        this.time = time;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public String getType_tr() {
        return type_tr;
    }

    public void setType_tr(String type_tr) {
        this.type_tr = type_tr;
    }

    public Long getFromaccount_id() {
        return fromaccount;
    }

    public void setFromaccount_id(Long fromaccount_id) {
        this.fromaccount = fromaccount_id;
    }

    public Long getToaccount_id() {
        return toaccount;
    }

    public void setToaccount_id(Long toaccount_id) {
        this.toaccount = toaccount_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}

