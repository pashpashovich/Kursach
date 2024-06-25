package com.example.bank.repository;

import com.example.bank.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends CrudRepository<User,Long> {
        User findUserByLogin(String login);
    }

