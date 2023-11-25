package com.example.bank.controller;

import com.example.bank.dao.CustomerRepository;
import com.example.bank.dao.UserRepository;
import com.example.bank.entities.Customer;
import com.example.bank.entities.RegPerson;
import com.example.bank.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Controller
public class RegistrationController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomersController customersController;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PersistenceContext
    EntityManager entityManager;

    @GetMapping(("/reg"))
    public String getHome(Model model) {
        model.addAttribute("reg", new RegPerson());
        return "main/registration";
    }

    @PostMapping("/reg")
    public ResponseEntity<Object> saveUSer(@ModelAttribute RegPerson regPerson){
        regPerson.getUser().setPassword(passwordEncoder.encode(regPerson.getUser().getPassword()));
        regPerson.getUser().setRoles("USER");
        System.out.println(regPerson.getUser());
        User result = userRepository.save(regPerson.getUser());
        if (result.getId() > 0){
            return ResponseEntity.ok("User Was Saved");
        }
        return ResponseEntity.status(404).body("Error, USer Not Saved");
    }

    private boolean isValidUser(String username, String password) {
        System.out.println(username);
        User user=userRepository.findUserByLogin(username);
        if (user!=null) {
            return true;
        }
        return false;
    }
}
