package com.example.bank.entities;

public class RegPerson {
    private User user;
    private Customer customer;


    public RegPerson(User user, Customer customer) {
        this.user = user;
        this.customer = customer;
    }

    public RegPerson() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
