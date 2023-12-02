package com.example.bank.controller;

import com.example.bank.dao.AccountsRepository;
import com.example.bank.dao.TransactionRepository;
import com.example.bank.entities.Account;
import com.example.bank.entities.Transaction;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/transactions")
@SessionAttributes("transactions")
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountsRepository accountsRepository;

    @GetMapping("/all")
    public String getTransactions(Model model) {
        List<Transaction> transactions = transactionRepository.findAll();
        model.addAttribute("transactions",transactions);
        return "transaction/transactions";
    }

    @GetMapping("/{fromaccount_id}")
    public String getTransactions(@PathVariable("fromaccount_id") String fromAccountId, Model model) {
        List<Account> accounts = accountsRepository.findAllByAccountid(Long.valueOf(fromAccountId));
        model.addAttribute("accounts", accounts);
        return "transaction/transactions";
    }

    @PostMapping
    public String getTransactionsofUser(@RequestParam("selectedAccountNumber") String selectedAccountNumber,
                                        @RequestParam("fromAccountId") String fromAccountId,
                                        @RequestParam(name = "sortField", defaultValue = "date") String sortField,
                                        @RequestParam(name = "sortDirection", defaultValue = "desc") String sortDirection,
                                        @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                        @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                        RedirectAttributes redirectAttributes) {

        List<Transaction> transactions = new ArrayList<>();
        Account selectedAccount = accountsRepository.findByAccountnumber(Long.valueOf(selectedAccountNumber));
        transactions.addAll(transactionRepository.findAllByFromaccountOrToaccount(selectedAccount.getAccountnumber(), selectedAccount.getAccountnumber()));
        transactions = transactions.stream()
                .filter(transaction -> transaction.getDate().isAfter(ChronoLocalDate.from(startDate.atStartOfDay()))
                        && transaction.getDate().isBefore(ChronoLocalDate.from(endDate.plusDays(1).atStartOfDay())))
                .collect(Collectors.toList());
        if ("amount".equals(sortField)) {
            transactions.sort(Comparator.comparing(Transaction::getAmount));
        } else if ("date".equals(sortField)) {
            transactions.sort(Comparator.comparing(Transaction::getDate));
        }
        if ("desc".equals(sortDirection)) {
            Collections.reverse(transactions);
        }
        System.out.println(selectedAccountNumber);
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
        byte[] statementContent = generateStatement(fromAccountId, selectedAccountNumber,startDate, endDate);
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(statementContent));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=statement.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(statementContent.length)
                .body(resource);
    }

    private byte[] generateStatement(String fromAccountId, String accountNumber, LocalDate startDate, LocalDate endDate) {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                Document document = new Document();
                PdfWriter.getInstance(document, outputStream);
                document.open();
                document.add(new Paragraph("Statement for the period from " + startDate + " to " + endDate));
                document.add(new Paragraph(" ")); // пустая строка
                List<Transaction> transactions = transactionRepository.findByFromaccountOrToaccountAndDateBetween(
                        Long.parseLong(accountNumber), Long.parseLong(fromAccountId),startDate, endDate);
                for (Transaction transaction : transactions) {
                    String transactionInfo = String.format(
                            "Date: %s, Type: %s, Sum: %s%n",
                            transaction.getDate(), transaction.getType_tr(), transaction.getAmount());
                    document.add(new Paragraph(transactionInfo));
                }
                document.close();
                return outputStream.toByteArray();
            } catch (Exception e) {
                // Обработка ошибок, например, логирование
                e.printStackTrace();
                return new byte[0];
            }
    }


}
