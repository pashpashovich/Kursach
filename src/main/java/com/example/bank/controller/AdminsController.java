package com.example.bank.controller;

import com.example.bank.entities.Account;
import com.example.bank.entities.Customer;
import com.example.bank.entities.Transaction;
import com.example.bank.entities.Transaction_type;
import com.example.bank.services.AccountService;
import com.example.bank.services.CustomerService;
import com.example.bank.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminsController {

    private final CustomerService customerService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    @Autowired
    public AdminsController(CustomerService customerService, AccountService accountService, TransactionService transactionService)
    {
        this.customerService = customerService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @GetMapping
    @Secured("ROLE_ADMIN")
    public String getAnAdmin(Model model) {
        List<Customer> customerList = customerService.getAllCustomers();
        List<Account> accountList = accountService.getAllAccounts();
        List<Long> customerId = accountList.stream().map(Account::getAccountid).collect(Collectors.toList());
        List<Customer> customersWithAccounts = customerList.stream()
                .filter(customer -> customerId.contains(customer.getId()))
                .collect(Collectors.toList());
        model.addAttribute("customers", customersWithAccounts);
        return "admins/customers";
    }

    @GetMapping("/applies")
    public String applies(Model model) {
        List<Customer> customerList = customerService.getAllCustomers();
        model.addAttribute("customers", customerList);

        List<Account> accountList = accountService.getAllAccounts();
        List<Long> customerId = accountList.stream().map(Account::getAccountid).collect(Collectors.toList());

        List<Customer> customersWithOutAccounts = customerList.stream()
                .filter(customer -> !customerId.contains(customer.getId()))
                .collect(Collectors.toList());

        model.addAttribute("customersWithOutAccounts", customersWithOutAccounts);
        return "admins/applies";
    }


    @PostMapping("/changeAccess")
    public String changeAccess(@RequestParam Long userId) {
        customerService.updateCustomerAccess(userId, !customerService.getCustomerById(userId).get().isHasaccess());
        return "redirect:/admin";
    }

    @PostMapping("/processRegistration")
    public String reg(@RequestParam Long requestId, Model model) {
        model.addAttribute("Id", requestId);
        return "redirect:/admin/new-customer?requestId=" + requestId;
    }

    @PostMapping("/deleteAppliance")
    public String deleteAppliance(@RequestParam Long userId) {
        customerService.deleteCustomer(userId);
        return "redirect:/admin/applies";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam Long userId) {
        customerService.deleteCustomer(userId);
        return "redirect:/admin";
    }



    @GetMapping("/new-customer")
    public String displayEmployeeForm(@RequestParam(required = false) Long requestId, Model model) {
        Customer customer = new Customer();
        Account account = new Account();
        model.addAttribute("customer", customer);
        model.addAttribute("account", account);
        model.addAttribute("requestId", requestId);
        return "admins/new-customer";
    }



    @GetMapping("/{customerId}")
    public String viewAccounts(@PathVariable Long customerId, Model model) {
        Optional<Customer> customer = customerService.getCustomerById(customerId);
        List<Account> accounts = customerService.getAccountsByCustomerId(customerId);
        model.addAttribute("customer", customer);
        model.addAttribute("accounts", accounts);
        Transaction transaction = new Transaction();
        model.addAttribute("transaction", transaction);
        return "admins/accounts";
    }

    @PostMapping("/save")
    public String saveCustomer(@RequestParam Long requestId, @RequestParam Long accountnumber, @RequestParam Double balance) {
        accountService.createAccount(requestId,accountnumber,balance);
        return "redirect:/admin";
    }

    @PostMapping("/deleteAccount")
    public String deleteAccount(@RequestParam Long accountNumber, @RequestParam Long customerId) {
        accountService.deleteAccount(String.valueOf(accountNumber));
        return "redirect:/admin/"+customerId;
    }

    @GetMapping("/addAccount")
    public String addAccount(@RequestParam(name = "customerId") Long customerId, Model model) {
        model.addAttribute("Id", customerId);
        Account account = new Account();
        model.addAttribute("account", account);
        return "admins/new-account";
    }

    @PostMapping("/saveAccount")
    public String saveAccount(@RequestParam Long requestId, @RequestParam Long accountnumber, @RequestParam Double balance) {
        accountService.createAccount(requestId,accountnumber,balance);
        return "redirect:/admin/"+requestId;
    }


    @PostMapping( "/deposit")
    public String deposit(@RequestParam String customerId, Transaction transaction, Model model) {
        Account toAccount = accountService.getAccountByAccountNumber(String.valueOf(transaction.getToaccount()));
        transactionService.createTransaction("0", String.valueOf(toAccount.getAccountnumber()), transaction.getAmount(), Transaction_type.DEPOSIT.name(),"BYN");
        customerService.updateAccountBalance(toAccount,transaction.getAmount());
        return "redirect:/admin/"+customerId;
    }

    @PostMapping("/setInterest")
    public String setInterest(@RequestParam int interest) {
        accountService.updateAllAccounts(interest);
        return "redirect:/admin";
    }


}
