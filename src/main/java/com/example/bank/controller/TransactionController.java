package com.example.bank.controller;

import com.example.bank.dao.TransactionRepository;
import com.example.bank.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;
    
    @GetMapping("/all")
    public String getTransactions(Model model) {
        List<Transaction> transactions = transactionRepository.findAll();
        model.addAttribute("transactions",transactions);
        return "transaction/transactions";
    }

    @GetMapping( "/{fromaccount_id}")
    public String getTransactionsofUser(@PathVariable("fromaccount_id") String fromaccount_id, Model model) {
        List<Transaction> transactions = transactionRepository.findAllByFromaccountAndToaccount(Long.parseLong(fromaccount_id), Long.parseLong(fromaccount_id));
        model.addAttribute("transactions", transactions);
        return "transaction/transactions";
    }



}
