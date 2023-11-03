package com.example.bank.controller;

import com.example.bank.dao.CustomerRepository;
import com.example.bank.dao.UserRepository;
import com.example.bank.entities.Customer;
import com.example.bank.entities.RegPerson;
import com.example.bank.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Controller
@RequestMapping(("/reg"))
public class RegistrationController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomersController customersController;
    @PersistenceContext
    EntityManager entityManager;

    @GetMapping
    public String getHome(Model model) {
        model.addAttribute("reg", new RegPerson());
        return "main/registration";
    }

    @PostMapping
    public String registration(
            @ModelAttribute("reg") RegPerson regPerson,
            RedirectAttributes redirectAttributes
    ) {
       User user=regPerson.getUser();
       Customer customer=regPerson.getCustomer();
       userRepository.save(user);
       customer.setCustomerId(user.getUser_id());
       customer.setHasaccess(false);
       customerRepository.save(customer);
       return "redirect:/";
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
