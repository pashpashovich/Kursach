package com.example.bank.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users2")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String login;
    private String password;
    private String roles;

    public User(String login, String password, String roles) {

    }

    public User() {

    }
}
