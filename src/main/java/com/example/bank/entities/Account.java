package com.example.bank.entities;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Это класс, который предстваляет отдельный счёт клиента
 */

@Entity
@Table(name = "accounts")
@Data
public class Account {
    /** Номер счета*/
    @Id
    private Long accountnumber;
    /** Баланс на счету*/
    private double balance;
    /** ID клиента*/
    private Long accountid;
    /** Дата открытия счета */
    private Date date_of_open;
    /** Валюта счёта*/
    private String currency_t;



    public Account() {
    }

    public Account(double balance, Long account_id, Date date_of_open, String currency_t) {
        this.balance = balance;
        this.accountid = account_id;
        this.date_of_open = date_of_open;
        this.currency_t = currency_t;
    }


}
