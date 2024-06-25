package com.example.bank.services;

import com.example.bank.repository.AccountsRepository;
import com.example.bank.entities.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class AccountService {

    private final AccountsRepository accountsRepository;

    @Autowired
    public AccountService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public List<Account> getAllAccounts() {
        return accountsRepository.findAll();
    }

    public void createAccount(Long requestId, Long accountnumber, double balance) {
        Account account = new Account();
        account.setAccountnumber(accountnumber);
        account.setBalance(balance);
        account.setCurrency_t("BYN");
        account.setDate_of_open(Date.valueOf(LocalDate.now()));
        account.setAccountid(requestId);
        accountsRepository.save(account);
    }

    public void updateAllAccounts(int interest) {
        accountsRepository.increaseAllBalancesByInterest(interest);
    }

    public List<Account> getAccountsByAccountId(Long accountId) {
        return accountsRepository.findAllByAccountid(accountId);
    }

    public Account getAccountByAccountNumber(String accountNumber) {
        return accountsRepository.findByAccountnumber(Long.valueOf(accountNumber));
    }

    public void deleteAccount(String accountNumber) {
        accountsRepository.deleteById(Long.valueOf(accountNumber));
    }
}
