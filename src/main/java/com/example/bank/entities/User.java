package com.example.bank.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
/**
 * Пользователь - и администратор, и клиент
 */
public class User {
    /**
     * ID для БД
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Логин - нельзя оставлять пустым
     */
    @Column(unique = true)
    @NotEmpty(message = "Пожалуйста, придумайте себе логин")
    private String login;
    @NotEmpty(message = "Пожалуйста, укажите пароль")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;
    private String roles;

    public User(Long id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }
}
