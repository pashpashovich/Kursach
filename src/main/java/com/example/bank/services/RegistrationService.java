package com.example.bank.services;

import com.example.bank.repository.UserRepository;
import com.example.bank.entities.Customer;
import com.example.bank.entities.User;
import com.example.bank.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(Customer regPerson, BindingResult bindingResult, Model model) {
        regPerson.setPassword(passwordEncoder.encode(regPerson.getPassword()));
        regPerson.setRoles("USER");
        User result = userRepository.save(regPerson);
        if (result.getId() > 0) {
            model.addAttribute("successMessage", "Поздравляем! Вы зарегистрированы. Дождитесь подтверждения заявки");
        } else {
            model.addAttribute("errorMessage", "Вы не зарегистрированы");
        }
    }
}

