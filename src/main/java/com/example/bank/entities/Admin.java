package com.example.bank.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

/**
 * The class Admin that extends class User and realizes the Singleton pattern. Also,except User's fields, has a static field users
 */
@Entity
@Table(name = "admins")
public class Admin extends User{

    /** the field instance with the type Admin for realization Singleton */
    @Transient
    private static User instance = null;
    /** the field users for storing all the users of the app */
   @Transient
    private static List<User> users;

    public Admin() {

    }

    /**
     * Этот статический метод создает новый объект класса Admin, если он еще не был создан.
     * @param user объект класса User
     * @param users список всех пользователей приложения.
     * @return возвращает экземпляр статического поля
     */
    public static Admin getInstance(User user,List<User> users) {
        if (instance == null) {
            instance = new Admin(user.getId(),user.getLogin(),user.getPassword(),users);
        }
        return (Admin) instance;
    }

    /**
     * Конструктор класса для метода getInstance()
     * @param user_id уникальный номер пользователя
     * @param login уникальное имя пользователя
     * @param password — хешированный пароль пользователя для входа в приложение.
     * @param users список всех пользователей приложения.
     */
    public Admin(Long user_id, String login, String password, List<User> users) {
        super(Long.valueOf(user_id.toString()), login, password);
        Admin.users = users;
    }

}
