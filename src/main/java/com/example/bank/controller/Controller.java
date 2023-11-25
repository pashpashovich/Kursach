package com.example.bank.controller;

import com.example.bank.dao.UserRepository;
import com.example.bank.entities.RegPerson;
import com.example.bank.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@org.springframework.stereotype.Controller
@RequestMapping()
public class Controller {
    @Autowired
    private UserRepository ourUserRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String goH0me(Model model){
        model.addAttribute("reg", new RegPerson());
        return "main/registration";
    }

    @PostMapping
    public ResponseEntity<Object> saveUSer(@RequestBody RegPerson regPerson){
        regPerson.getUser().setPassword(passwordEncoder.encode(regPerson.getUser().getPassword()));
        regPerson.getUser().setRoles("USER");
        System.out.println(regPerson.getUser());
        User result = ourUserRepo.save(regPerson.getUser());
        if (result.getId() > 0){
            return ResponseEntity.ok("USer Was Saved");
        }
        return ResponseEntity.status(404).body("Error, USer Not Saved");
    }
    @GetMapping("/users/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> getAllUSers(){
        return ResponseEntity.ok(ourUserRepo.findAll());
    }
    @GetMapping("/users/single")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<Object> getMyDetails(){
        return ResponseEntity.ok(ourUserRepo.findUserByLogin(getLoggedInUserDetails().getUsername()));
    }

    public UserDetails getLoggedInUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails){
            return (UserDetails) authentication.getPrincipal();
        }
        return null;
    }
}
