package com.example.bank.controller;

import com.example.bank.entities.Customer;
import com.example.bank.services.RegistrationService;
import com.example.bank.validators.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserValidator userValidator;

    @GetMapping("/reg")
    public String getHome(Model model) {
        model.addAttribute("reg", new Customer());
        return "main/registration";
    }

    @PostMapping("/reg")
    public String saveUser(@ModelAttribute("reg") @Valid Customer regPerson, BindingResult bindingResult, Model model) {
        userValidator.validate(regPerson, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("reg", regPerson);
            return "main/registration";
        }
        registrationService.registerUser(regPerson, bindingResult, model);
        return "main/registration";
    }

}
