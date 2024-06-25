package com.example.bank.controller;

import com.example.bank.entities.Account;
import com.example.bank.entities.Transaction;
import com.example.bank.services.AccountService;
import com.example.bank.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/transactions")
@SessionAttributes("transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/all")
    public String getTransactions(Model model) {
        List<Transaction> transactions = transactionService.getAllTransactions();
        List<Account> accounts = accountService.getAllAccounts();
        model.addAttribute("transactions", transactions);
        model.addAttribute("accounts", accounts);
        return "admins/allTransactions";
    }

    @GetMapping("/{fromaccount_id}")
    public String getTransactions(@PathVariable("fromaccount_id") String fromAccountId, Model model) {
        List<Account> accounts = accountService.getAccountsByAccountId(Long.valueOf(fromAccountId));
        model.addAttribute("accounts", accounts);
        return "customer/transactions";
    }

    @GetMapping("/allSorted")
    public String getSTransactions(Model model) {
        List<Account> accounts = accountService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "admins/allTransactions";
    }

    @PostMapping("/allTransactions")
    public String getAllTransactions(@RequestParam(name = "selectedAccountNumber") Long selectedAccountNumber,
                                        @RequestParam(name = "sortField", defaultValue = "date") String sortField,
                                        @RequestParam(name = "sortDirection", defaultValue = "desc") String sortDirection,
                                        @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                        @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                        RedirectAttributes redirectAttributes) {
        List<Transaction> transactions = transactionService.getAllSTransactions(String.valueOf(selectedAccountNumber), sortField, sortDirection, startDate, endDate);
        redirectAttributes.addFlashAttribute("transactions", transactions);
        System.out.println(transactions);
        redirectAttributes.addFlashAttribute("selectedAccountNumber", selectedAccountNumber);
        redirectAttributes.addFlashAttribute("startDate", startDate);
        redirectAttributes.addFlashAttribute("endDate", endDate);
        return "redirect:/transactions/allSorted";
    }

    @PostMapping
    public String getTransactionsofUser(@RequestParam("selectedAccountNumber") String selectedAccountNumber,
                                        @RequestParam("fromAccountId") String fromAccountId,
                                        @RequestParam(name = "sortField", defaultValue = "date") String sortField,
                                        @RequestParam(name = "sortDirection", defaultValue = "desc") String sortDirection,
                                        @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                        @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                        RedirectAttributes redirectAttributes) {

        List<Transaction> transactions = transactionService.getTransactions(selectedAccountNumber, fromAccountId, sortField, sortDirection, startDate, endDate);

        redirectAttributes.addAttribute("fromaccount_id", fromAccountId);
        redirectAttributes.addFlashAttribute("transactions", transactions);
        redirectAttributes.addFlashAttribute("selectedAccountNumber", selectedAccountNumber);
        redirectAttributes.addFlashAttribute("startDate", startDate);
        redirectAttributes.addFlashAttribute("endDate", endDate);
        return "redirect:/transactions/" + fromAccountId;
    }

    @RequestMapping(value = "/download-statement", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<InputStreamResource> downloadStatement(
            @RequestParam("fromAccountId") String fromAccountId,
            @RequestParam("selectedAccountNumber") String selectedAccountNumber,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws IOException {
        if (fromAccountId == null || selectedAccountNumber == null || startDate == null || endDate == null) {
            return ResponseEntity.badRequest().body(null);
        }
        byte[] statementContent = transactionService.generateStatement(fromAccountId, selectedAccountNumber, startDate, endDate);
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(statementContent));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=statement.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(statementContent.length)
                .body(resource);
    }




}
