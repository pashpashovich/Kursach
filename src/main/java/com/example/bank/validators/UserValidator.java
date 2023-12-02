package com.example.bank.validators;

import com.example.bank.dao.UserRepository;
import com.example.bank.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class UserValidator implements Validator {

    private final UserRepository userRepository;
    @Autowired
    public UserValidator( UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if(userRepository.findUserByLogin(user.getLogin())!=null)
            errors.rejectValue("login","","Пользователь с таким логином уже существует, придумайте другой");

    }

}
