package com.example.bank.controller;

import com.example.bank.dao.AccountsRepository;
import com.example.bank.dao.CustomerRepository;
import com.example.bank.dao.TransactionRepository;
import com.example.bank.entities.Account;
import com.example.bank.entities.Customer;
import com.example.bank.entities.Transaction;
import com.example.bank.entities.Transfer;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

@Controller
public class CheckDownloadController {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AccountsRepository accountsRepository;

    @RequestMapping(value = "/download/{fromId}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fromId, Transfer transfer, Model model) {
        String fileName = "check_" + System.currentTimeMillis() + ".txt"; // генерируем уникальное имя файла
        File file = new File("/D:/Java/cleverBank/src/check/" + fileName);
        Account account=accountsRepository.findFirstByAccountid(Long.parseLong(fromId));
        Transaction transaction=transactionRepository.findFirstByFromaccountOrderByDateDescTimeDesc(account.getAccountnumber());
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("\tБанковский чек\n");
            writer.write("Чек: " + System.currentTimeMillis()  + "\n");
            writer.write(transaction.getDate() + "\t" + transaction.getTime() + "\n");
            writer.write("Тип транзакции: " + transaction.getType_tr() + "\n");
            writer.write("Счёт отправителя: " + transaction.getFromaccount_id() + "\n");
            writer.write("Счёт получателя: " + transaction.getToaccount_id() + "\n");
            writer.write("Сумма транзакции: " + transaction.getAmount() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] fileContent=null;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileContent = IOUtils.toByteArray(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName); // Используйте имя файла, которое вы сгенерировали
        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }
}

