package com.example.bank.controller;

import com.example.bank.dao.CustomerRepository;
import com.example.bank.dao.UserRepository;
import com.example.bank.entities.Customer;
import com.example.bank.entities.User;
import com.example.bank.validators.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(("/reg"))
    public String getHome(Model model) {
        model.addAttribute("reg", new Customer());
        return "main/registration";
    }

    @PostMapping("/reg")
    public String saveUSer(@ModelAttribute("reg") @Valid Customer regPerson, BindingResult bindingResult, Model model){
        userValidator.validate(regPerson,bindingResult);
        if (bindingResult.hasErrors()) {
            return "main/registration";
        }
        else {
            regPerson.setPassword(passwordEncoder.encode(regPerson.getPassword()));
        regPerson.setRoles("USER");
        User result = userRepository.save(regPerson);
        if (result.getId() > 0){
            model.addAttribute("successMessage", "Поздравляем! Вы зарегистрированы");
        }
        else model.addAttribute("errorMessage", "Вы не зарегистрированы"); }
        return "main/registration";
    }
}
