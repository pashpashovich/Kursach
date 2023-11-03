package com.example.bank.entities;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
@Data
public class Customer {

    @Id
    private Long customerId;
    private String fio;
    private String email;
    private double balance;
    private boolean hasaccess;


    public Customer() {

    }

    public Customer(String fio, String email, double balance) {
        this.fio = fio;
        this.email = email;
        this.balance = balance;;
    }
}
