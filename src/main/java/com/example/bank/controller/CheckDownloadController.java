package com.example.bank.controller;

import com.example.bank.dao.AccountsRepository;
import com.example.bank.dao.CustomerRepository;
import com.example.bank.dao.TransactionRepository;
import com.example.bank.entities.Account;
import com.example.bank.entities.Customer;
import com.example.bank.entities.Transaction;
import com.example.bank.entities.Transfer;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;

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
        try {
            String fileName = "check_" + System.currentTimeMillis() + ".pdf";
            File pdfFile = new File("C:/Users/Павел/AppData/Local/Temp/Bank/src/checks/" + fileName);

            // Создайте новый документ PDF
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            Account account = accountsRepository.findFirstByAccountid(Long.parseLong(fromId));
            Transaction transaction = transactionRepository.findFirstByFromaccountOrderByDateDescTimeDesc(account.getAccountnumber());
            BaseFont baseFont = BaseFont.createFont("C:/Users/Павел/Downloads/Arial Cyr/Arial Cyr.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont, 12);
            // Добавьте содержимое в PDF-документ
            Paragraph title = new Paragraph("Банковский чек",font);
            Paragraph checkDetails = new Paragraph("Чек: " + System.currentTimeMillis(),font);
            Paragraph transactionDetails = new Paragraph(
                    transaction.getDate() + " " + transaction.getTime() + "\n" +
                            "Тип транзакции: " + transaction.getType_tr() + "\n" +
                            "Счёт отправителя: " + transaction.getFromaccount_id() + "\n" +
                            "Счёт получателя: " + transaction.getToaccount_id() + "\n" +
                            "Сумма транзакции: " + transaction.getAmount(), font
            );
            document.add(title);
            document.add(checkDetails);
            document.add(transactionDetails);
            document.close();
            byte[] fileContent = Files.readAllBytes(pdfFile.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", fileName);

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            // Обработка ошибки
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

