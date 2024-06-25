package com.example.bank.services;

import com.example.bank.repository.AccountsRepository;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.entities.Account;
import com.example.bank.entities.Transaction;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    public List<Transaction> getTransactions(String selectedAccountNumber, String fromAccountId,
                                             String sortField, String sortDirection,
                                             LocalDate startDate, LocalDate endDate) {

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

        return transactions;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }


    public List<Transaction> getAllSTransactions(String selectedAccountNumber,
                                             String sortField, String sortDirection,
                                             LocalDate startDate, LocalDate endDate) {
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

        return transactions;
    }
    public List<Transaction> getTransactionsByAccount(String accountNumber, LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByFromaccountOrToaccountAndDateBetween(
                Long.parseLong(accountNumber), Long.parseLong(accountNumber), startDate, endDate);
    }

    public void createTransaction(String fromAccountId, String toAccountId, double amount,String typeTr, String currency) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateId(LocalDate.now(),LocalTime.now()));
        transaction.setFromaccount(Long.parseLong(fromAccountId));
        transaction.setToaccount(Long.parseLong(toAccountId));
        transaction.setAmount(amount);
        transaction.setDate(LocalDate.now());
        transaction.setTime(LocalTime.now().withNano(0));
        transaction.setCurrency(currency);
        transaction.setType_tr(typeTr);
        transactionRepository.save(transaction);
    }

    private long generateId(LocalDate date, LocalTime time) {
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        int hours = time.getHour();
        int minutes = time.getMinute();
        int seconds = time.getSecond();
        return (year * 100000000 + month * 1000000 + day * 10000 + hours * 100 + minutes * 10 + seconds);
    }


    public byte[] generateStatement(String fromAccountId, String accountNumber, LocalDate startDate, LocalDate endDate) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            BaseFont baseFont = BaseFont.createFont("C:/Users/Павел/Downloads/Arial Cyr/Arial Cyr.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont, 12);
            document.open();
            document.add(new Paragraph("Выписка за период с " + startDate + " по " + endDate, font));
            document.add(new Paragraph(" ", font));
            List<Transaction> transactions = getTransactions(accountNumber,fromAccountId,"date","ndesc",startDate,endDate);
            for (Transaction transaction : transactions) {
                String transactionInfo = String.format(
                        "Дата: %s, Тип: %s, Валюта:%s Сумма: %s%n",
                        transaction.getDate(), transaction.getType_tr(), transaction.getCurrency(), transaction.getAmount());
                document.add(new Paragraph(transactionInfo, font));
            }
            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }
}

