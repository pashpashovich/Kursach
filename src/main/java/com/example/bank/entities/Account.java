package com.example.bank.entities;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Date;

/**
 * This is a class account that represents the account of the exact customer
 */

@Entity
@Table(name = "accounts")
@Data
public class Account {
    @Id
    /** Number of the account*/
    private Long accountnumber;
    /** The balance of the account*/
    private double balance;

    private Long accountid;
    /** The date of creating the account*/

    private Date date_of_open;
    /** The type of the currency*/
    private String currency_t;



    public Account() {
    }

    public Account(double balance, Long account_id, Date date_of_open, String currency_t) {
        this.balance = balance;
        this.accountid = account_id;
        this.date_of_open = date_of_open;
        this.currency_t = currency_t;
    }



    /**
     * The method of the deposit
     * @param amount - to add to the existing balance
     */
    public void deposit(double amount) {
        amount=translation(amount,currency_t);
        balance=balance+amount;
    }

    /**
     * The method of the withdrawal
     * @param amount - to take from the existing balance
     */
    public void withdraw(double amount) {
        amount=translation(amount,currency_t);
        balance=balance-amount;
    }

    /**
     * This method translates from not BYN currency to BYN currency
     * @param amount - the sum transaction
     * @param currency - name of currency as enum
     * @return returns the amount in BYN
     */
    public double translation (double amount, String currency) {
        double rus=38.02;
        double usd=0.39;
        double euro=0.37;
        double yuan=2.82;
        switch (currency) {
            case "RUB" -> amount=amount*rus;
            case "USD" -> amount=amount*usd;
            case "XEU" -> amount=amount*euro;
            case "CNY" ->  amount=amount*yuan;
        }
        return amount;
    }

}
