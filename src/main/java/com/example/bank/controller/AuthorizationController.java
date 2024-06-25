package com.example.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(("/auth"))
public class AuthorizationController {

    @GetMapping
    public String login() {
        return "main/authorization";
    }

    @GetMapping ("/failToLog")
    public String failToLog(Model model) {
        model.addAttribute("errorMessage", "Неверные учетные данные. Пожалуйста, попробуйте снова.");
        return "main/authorization";
    }
}
