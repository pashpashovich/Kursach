package com.example.bank.controller;

import com.example.bank.dao.CustomerRepository;
import com.example.bank.dao.UserRepository;
import com.example.bank.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(("/auth"))
public class AuthorizationController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomersController customersController;


    @GetMapping
    public String getHome(Model model) {
        model.addAttribute("user", new User());
        return "main/authorization";
    }

    @PostMapping
    public String authorization(
            @ModelAttribute("user") User user,
            RedirectAttributes redirectAttributes
    ) {
        if (isValidUser(user.getLogin(), user.getPassword())) {
            User user2=userRepository.findUserByLogin(user.getLogin());
            System.out.println(user2.getUser_id());
            redirectAttributes.addAttribute("id", Long.toString(user2.getUser_id()));
            return "redirect:/customers/{id}";
        } else {
            return "redirect:/";
        }
    }

    private boolean isValidUser(String username, String password) {
        System.out.println(username);
        User user=userRepository.findUserByLogin(username);
        if (user!=null) {
            if (user.getPassword().equals(password))
                return true;
            else return false;
        }
        return false;
    }
}
