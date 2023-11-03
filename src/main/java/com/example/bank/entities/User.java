package com.example.bank.entities;

import jakarta.persistence.*;

/**
 * The class of User of the app with fields user_id,login and password
 */
@Entity
@Table(name = "users")
public class User {
    /** This is the unique number of the user */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    Long user_id;
    /** This is the unique name of the user*/
    @Column(name = "login")
    String login;
    /** This is the hashed password with salt of the user to get into the app*/
    @Column(name = "password")
    String password;

    /**
     * This is the constructor with all fields
     * @param login the unique name of the user
     * @param password the hashed password with salt of the user to get into the app
     */

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }



    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {

    }
}
