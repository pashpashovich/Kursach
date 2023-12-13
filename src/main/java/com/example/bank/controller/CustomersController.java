package com.example.bank.controller;

import com.example.bank.dao.AccountsRepository;
import com.example.bank.dao.CustomerRepository;
import com.example.bank.dao.TransactionRepository;
import com.example.bank.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomersController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountsRepository accountsRepository;

    @GetMapping
    public String getCustomers(Model model) {
        List<Customer> customerList = customerRepository.findAll();
        model.addAttribute("customers", customerList);
        return "customer/customers";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Secured("ROLE_USER")
    public String getACustomer(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Customer> customerOptional = customerRepository.findById(Long.parseLong(id));
        Customer customer = customerOptional.orElse(new Customer());
        List<Customer> customerList = customerRepository.findAll();
        List<Account> accountList = accountsRepository.findAllByAccountid(customer.getId());
        Transaction transaction = new Transaction();
        model.addAttribute("transaction", transaction);
        model.addAttribute("customer", customer); // Теперь у нас есть объект Customer, а не Optional
        model.addAttribute("accounts", accountList);
        model.addAttribute("customers", customerList);
        String errorMessage = (String) redirectAttributes.getFlashAttributes().get("errorMessage");
        if (errorMessage != null) model.addAttribute("errorMessage", errorMessage);
        return "customer/aCustomer";
    }

    @GetMapping("/noAccess")
    public String noAccess() {
        return "customer/noAccess";
    }

    @RequestMapping(value = "/transfer/{fromId}", method = RequestMethod.POST)
    public String transferAmount(@PathVariable String fromId, Transaction transaction, RedirectAttributes redirectAttributes) {
        Account fromAccount = accountsRepository.findByAccountnumber(transaction.getFromaccount_id());
        Account toAccount = accountsRepository.findByAccountnumber(transaction.getToaccount_id());
        if (fromAccount == null || toAccount == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Такого счёта не существует");
            return "redirect:/customers/" + fromId;
        }
        if (fromAccount.getBalance() < transaction.getAmount()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Недостаточно средств на счете для перевода");
            return "redirect:/customers/" + fromId;
        }
        if (transaction.getAmount() <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Неверная сумма для перевода");
            return "redirect:/customers/" + fromId;
        }
        Transaction transaction2 = new Transaction(
                generateId(LocalDate.now(), LocalTime.now()),
                Transaction_type.ACCOUNT_TRANSFER.name(),
                fromAccount.getAccountnumber(),
                toAccount.getAccountnumber(),
                transaction.getAmount(),
                LocalDate.now(),
                LocalTime.now().withNano(0)
        );
        fromAccount.setBalance(fromAccount.getBalance() - transaction.getAmount());
        toAccount.setBalance(toAccount.getBalance() + transaction.getAmount());
        transactionRepository.save(transaction2);
        return "redirect:/customers/" + fromId;
    }


    public long generateId(LocalDate date, LocalTime time) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        int hours = time.getHour();
        int minutes = time.getMinute();
        int seconds = time.getSecond();
        return (year * 100000000 + month * 1000000 + day * 10000 + hours * 100 + minutes * 10 + seconds);
    }

}
