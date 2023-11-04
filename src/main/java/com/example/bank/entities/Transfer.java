package com.example.bank.entities;

public class Transfer {

    private String toId;
    private double balance;

    public Transfer() {
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
