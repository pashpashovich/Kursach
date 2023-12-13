package com.example.bank.controller;

import org.springframework.web.bind.annotation.*;

@org.springframework.stereotype.Controller
@RequestMapping()
public class Controller {
    @GetMapping
    public String goH0me() {
        return "main/authorization";
    }
}
