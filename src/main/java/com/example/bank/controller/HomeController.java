package com.example.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String getHome() {
        return "main/home";
    }

    @GetMapping("/banks")
    public String getBanks() {
        return "staticPages/banks";
    }

    @GetMapping("/help")
    public String getHelp() {
        return "staticPages/help";
    }

    @GetMapping("/cont")
    public String getCont() {
        return "staticPages/contacts";
    }


}
