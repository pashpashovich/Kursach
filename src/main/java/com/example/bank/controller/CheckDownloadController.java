package com.example.bank.controller;

import com.example.bank.dao.AccountsRepository;
import com.example.bank.dao.CustomerRepository;
import com.example.bank.dao.TransactionRepository;
import com.example.bank.entities.Account;
import com.example.bank.entities.Customer;
import com.example.bank.entities.Transaction;
import com.example.bank.entities.Transfer;
import com.itextpdf.text.*;
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
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fromId, Model model) {
        try {
            String fileName = "check_" + System.currentTimeMillis() + ".pdf";
            File pdfFile = new File("C:/Users/Павел/OneDrive/Рабочий стол/2 курс/3 семестр/ООПиП курсач/Bank/src/checks" + fileName);
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            Account account = accountsRepository.findFirstByAccountid(Long.parseLong(fromId));
            Transaction transaction2 = transactionRepository.findFirstByFromaccountOrderByDateDescTimeDesc(account.getAccountnumber());
            BaseFont baseFont = BaseFont.createFont("C:/Users/Павел/Downloads/Arial Cyr/Arial Cyr.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont, 12);
            Paragraph title = new Paragraph("Банковский чек",font);
            Paragraph checkDetails = new Paragraph("Чек: " + System.currentTimeMillis(),font);
            Paragraph transactionDetails = new Paragraph(
                    transaction2.getDate() + " " + transaction2.getTime() + "\n" +
                            "Тип транзакции: " + transaction2.getType_tr() + "\n" +
                            "Счёт отправителя: " + transaction2.getFromaccount_id() + "\n" +
                            "Счёт получателя: " + transaction2.getToaccount_id() + "\n" +
                            "Сумма транзакции: " + transaction2.getAmount(), font
            );
            Paragraph paragraph = new Paragraph();
            paragraph.add(new Phrase("ПРОГРЕССБАНК", font));
            Image img = Image.getInstance("C:/Users/Павел/OneDrive/Рабочий стол/2 курс/3 семестр/ООПиП курсач/Bank/src/main/resources/static/img/logo.png");
            img.setAlignment(Image.LEFT);
            img.scalePercent(2);
            paragraph.add(new Chunk(img, 0, 0, true));
            document.add(paragraph);
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
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

