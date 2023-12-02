package com.example.bank.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NotEmpty(message = "Пожалуйста, придумайте себе логин")
    private String login;
    @NotEmpty(message = "Пожалуйста, укажите пароль")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;
    private String roles;

    public User(String login, String password, String roles) {

    }

    public User() {

    }
}
