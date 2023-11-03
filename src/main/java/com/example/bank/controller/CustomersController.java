package com.example.bank.controller;

import com.example.bank.dao.AccountsRepository;
import com.example.bank.dao.CustomerRepository;
import com.example.bank.dao.TransactionRepository;
import com.example.bank.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
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
        model.addAttribute("customers",customerList);
        return "customer/customers";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getACustomer(@PathVariable String id, Model model) {
        Optional<Customer> customerOptional = customerRepository.findById(Long.parseLong(id));

        Customer customer = customerOptional.orElse(new Customer());

        List<Customer> customerList = customerRepository.findAll();
        List<Account> accountList=accountsRepository.findAllByAccountid(customer.getCustomerId());
        Transfer transfer = new Transfer();
        model.addAttribute("transfer", transfer);
        model.addAttribute("customer", customer); // Теперь у нас есть объект Customer, а не Optional
        model.addAttribute("accounts",accountList);
        model.addAttribute("customers", customerList);
        return "customer/aCustomer";
    }


    @GetMapping("/new-customer")
    public String displayEmployeeForm(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer",customer);
        return "customer/new-customer";
    }

    @PostMapping("/save")
    public String createEmployee(Customer customer, Model model) {
        customerRepository.save(customer);
        return "redirect:";
    }

    @RequestMapping(value = "/transfer/{fromId}", method = RequestMethod.POST)
    public String transferAmount(@PathVariable String fromId, Transfer transfer, Model model) {

        Customer toCustomer = customerRepository.findById(transfer.getToId()).get();
        Customer fromCustomer = customerRepository.findById(Long.parseLong(fromId)).get();

        if (fromCustomer.getBalance() >= transfer.getBalance()) {
            customerRepository.updateCustomer(fromCustomer.getBalance() - transfer.getBalance(),
                    fromCustomer.getCustomerId());
            customerRepository.updateCustomer(toCustomer.getBalance() + transfer.getBalance(),
                    toCustomer.getCustomerId());
        }

        model.addAttribute("customer",fromCustomer);

        Transaction transaction = new Transaction(Transaction_type.ACCOUNT_TRANSFER.name(),fromCustomer.getCustomerId(),
                toCustomer.getCustomerId(),transfer.getBalance(), LocalDate.now(), LocalTime.now());

        transactionRepository.save(transaction);

        return "redirect:/customers";
    }

}
