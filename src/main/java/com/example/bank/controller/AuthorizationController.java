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

    @GetMapping
    public String login() {
        return "main/authorization";
    }

    @GetMapping("/failToLog")
    public String failToLog() {
        return "main/registration";
    }
}
