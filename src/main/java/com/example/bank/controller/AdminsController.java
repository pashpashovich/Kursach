package com.example.bank.controller;

import com.example.bank.dao.AccountsRepository;
import com.example.bank.dao.CustomerRepository;
import com.example.bank.dao.TransactionRepository;
import com.example.bank.entities.Account;
import com.example.bank.entities.Customer;
import com.example.bank.entities.Transaction;
import com.example.bank.entities.Transaction_type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class AdminsController {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountsRepository accountsRepository;



    @GetMapping
    @Secured("ROLE_ADMIN")
    public String getAnAdmin(Model model) {
        List<Customer> customerList = customerRepository.findAll();
        model.addAttribute("customers",customerList);
        List <Account> accountList=accountsRepository.findAll();
        List<Long> customerId= new ArrayList<>();
        for (Account account:accountList) {
            customerId.add(account.getAccountid());
        }
        List<Customer> customersWithOutAccounts = customerList.stream()
                .filter(customer -> !customerId.contains(customer.getId()))
                .collect(Collectors.toList());
        model.addAttribute("customersWithOutAccounts",customersWithOutAccounts);
        return "customer/customers";
    }
    @GetMapping("/applies")
    public String applies(Model model) {
        List<Customer> customerList = customerRepository.findAll();
        model.addAttribute("customers",customerList);
        List <Account> accountList=accountsRepository.findAll();
        List<Long> customerId= new ArrayList<>();
        for (Account account:accountList) {
            customerId.add(account.getAccountid());
        }
        List<Customer> customersWithOutAccounts = customerList.stream()
                .filter(customer -> !customerId.contains(customer.getId()))
                .collect(Collectors.toList());
        model.addAttribute("customersWithOutAccounts",customersWithOutAccounts);
        return "customer/applies";
    }

    @PostMapping("/changeAccess")
    public String changeAccess(@RequestParam Long userId) {
        Customer customer = customerRepository.findById(userId).get();
        if (customer.isHasaccess()) customerRepository.updateCustomerById(userId,false);
        else customerRepository.updateCustomerById(userId,true);
        return "redirect:/admin";
    }

    @PostMapping("/processRegistration")
    public String reg(@RequestParam Long requestId, Model model) {
        model.addAttribute("Id", requestId);
        return "redirect:/admin/new-customer?requestId=" + requestId;
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam Long userId) {
        Customer customer = customerRepository.findById(userId).get();
        customerRepository.delete(customer);
        return "redirect:/admin";
    }

    @GetMapping("/new-customer")
    public String displayEmployeeForm(@RequestParam(required = false) Long requestId, Model model) {
        Customer customer = new Customer();
        Account account = new Account();
        model.addAttribute("customer", customer);
        model.addAttribute("account", account);
        model.addAttribute("requestId", requestId);
        return "customer/new-customer";
    }

    @PostMapping("/save")
    public String createAccount(@RequestParam Long requestId,Account account, Model model) {
        account.setCurrency_t("BLR");
        account.setDate_of_open(Date.valueOf(LocalDate.now()));
        account.setAccountid(requestId);
        accountsRepository.save(account);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/transfer/{fromId}", method = RequestMethod.POST)
    public String transferAmount(@PathVariable String fromId, Transaction transaction, Model model) {
        Account fromAccount =accountsRepository.findByAccountnumber(transaction.getFromaccount_id());
        Account toAccount =accountsRepository.findByAccountnumber(transaction.getToaccount_id());
        Transaction transaction2 = new Transaction(generateId(LocalDate.now(), LocalTime.now()), Transaction_type.ACCOUNT_TRANSFER.name(),fromAccount.getAccountnumber(),
                toAccount.getAccountnumber(),transaction.getAmount(), LocalDate.now(), LocalTime.now().withNano(0));
        fromAccount.setBalance(fromAccount.getBalance()-transaction.getAmount());
        toAccount.setBalance(toAccount.getBalance()+transaction.getAmount());
        transactionRepository.save(transaction2);
        return "redirect:/customers/"+fromId;
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
