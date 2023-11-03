package com.example.bank.entities;

public class Transfer {

    private long toId;
    private double balance;

    public Transfer() {
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
