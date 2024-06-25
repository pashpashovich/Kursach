package com.example.bank.controller;

import com.example.bank.entities.*;
import com.example.bank.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomersController {

    @Autowired
    private CustomerService customersService;

    @GetMapping
    public String getCustomers(Model model) {
        List<Customer> customerList = customersService.getAllCustomers();
        model.addAttribute("customers", customerList);
        return "admins/customers";
    }

    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public String getACustomer(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Customer> customerOptional = customersService.getCustomerById(id);
        Customer customer = customerOptional.orElse(new Customer());
        List<Customer> customerList = customersService.getAllCustomers();
        List<Account> accountList = customersService.getAccountsByCustomerId(customer.getId());
        Transaction transaction = new Transaction();
        model.addAttribute("currency",CurrencyT.values());
        model.addAttribute("transaction", transaction);
        model.addAttribute("customer", customer);
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

    @PostMapping("/transfer/{fromId}")
    public String transferAmount(@PathVariable Long fromId, Transaction transaction, RedirectAttributes redirectAttributes) {
        Account fromAccount = customersService.findAccountByAccountNumber(String.valueOf(transaction.getFromaccount()));
        Account toAccount = customersService.findAccountByAccountNumber(String.valueOf(transaction.getToaccount()));
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
        long transactionId = customersService.generateTransactionId();
        Transaction transaction2 = new Transaction(
                transactionId,
                Transaction_type.ACCOUNT_TRANSFER.name(),
                fromAccount.getAccountnumber(),
                toAccount.getAccountnumber(),
                transaction.getAmount(),
                LocalDate.now(),
                LocalTime.now().withNano(0),
                transaction.getCurrency()
        );
        customersService.updateAccountBalances(fromAccount, toAccount, transaction.getAmount(), transaction.getCurrency());
        customersService.saveTransaction(transaction2);
        return "redirect:/customers/" + fromId;
    }

    @PostMapping("/deleteUser/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        Customer customer = customersService.getCustomerById(userId).orElse(null);
        if (customer != null) {
            customersService.deleteCustomer(customer);
        }
        return "redirect:/customers";
    }

    @PostMapping("/changeAccess/{userId}")
    public String changeAccess(@PathVariable Long userId) {
        Customer customer = customersService.getCustomerById(userId).orElse(null);
        if (customer != null) {
            boolean hasAccess = !customer.isHasaccess();
            customersService.updateCustomerAccess(userId, hasAccess);
        }
        return "redirect:/customers";
    }
}
