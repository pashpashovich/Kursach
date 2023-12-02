package com.example.bank.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
@Entity
@Table(name = "customers")
public class Customer extends User{

    @NotEmpty(message = "Пожалуйста, укажите ФИО")
    private String fio;
    private boolean hasaccess;
    @NotEmpty(message = "Пожалуйста, укажите email")
    @Email(message = "Пожалуйста, укажите корректный email")
    private String email;
}

