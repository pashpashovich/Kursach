package com.example.bank.services;

import com.example.bank.repository.AccountsRepository;
import com.example.bank.repository.CustomerRepository;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.entities.Account;
import com.example.bank.entities.Customer;
import com.example.bank.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public void validateTransfer(Account fromAccount, Account toAccount, double amount) throws Exception {
        if (fromAccount == null || toAccount == null) {
            throw new Exception("Такого счёта не существует");
        }
        if (fromAccount.getBalance() < amount) {
            throw new Exception("Недостаточно средств на счете для перевода");
        }
        if (amount <= 0) {
            throw new Exception("Неверная сумма для перевода");
        }
    }

    @Transactional(readOnly = true)
    public List<Account> getAccountsByCustomerId(Long customerId) {
        return accountsRepository.findAllByAccountid(customerId);
    }

    @Transactional
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Transactional
    public void updateAccountBalances(Account fromAccount, Account toAccount, double amount, String currency) {
        if (!currency.equals("BYN")) {
            BigDecimal amount2 = new BigDecimal(amount);
            amount2 = translation(amount2,currency);
            amount=Double.valueOf(amount2.toString());
        }
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
    }

    @Transactional
    public void updateAccountBalance(Account toAccount, double amount) {
        toAccount.setBalance(toAccount.getBalance() + amount);
    }

    @Transactional(readOnly = true)
    public Account findAccountByAccountNumber(String accountNumber) {
        return accountsRepository.findByAccountnumber(Long.valueOf(accountNumber));
    }

    @Transactional
    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }

    @Transactional
    public void updateCustomerAccess(Long userId, boolean hasAccess) {
        customerRepository.updateCustomerById(userId, hasAccess);
    }

    @Transactional
    public long generateTransactionId() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        int hours = time.getHour();
        int minutes = time.getMinute();
        int seconds = time.getSecond();
        return (year * 100000000 + month * 1000000 + day * 10000 + hours * 100 + minutes * 10 + seconds);
    }

    public BigDecimal translation (BigDecimal amount, String currency) {
        BigDecimal rus=new BigDecimal("0.037");
        BigDecimal usd=new BigDecimal("3.31");
        BigDecimal euro=new BigDecimal("0.37");
        BigDecimal yuan=new BigDecimal("3.61");
        switch (currency) {
            case "RUB" -> amount=amount.multiply(rus);
            case "USD" -> amount=amount.multiply(usd);
            case "XEU" -> amount=amount.multiply(euro);
            case "CNY" ->  amount=amount.multiply(yuan);
        }
        return amount;
    }
}
