package com.example.bank.dao;

import com.example.bank.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends CrudRepository<User,Long> {


//        @Override
//        @Query(nativeQuery = true, value = "SELECT * FROM Users ORDER BY user_id")
//        List<User> findAll();

        User findUserByLogin(String login);

//        @Transactional
//        @Modifying
//        @Query(nativeQuery = true, value = "UPDATE customers SET balance = :balance where customer_id = :customerId")
//        void updateCustomer(@Param("balance") Double balance, @Param("customerId") long customerId);

    }

