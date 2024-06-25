package com.example.bank.services;

import com.example.bank.repository.AccountsRepository;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.entities.Account;
import com.example.bank.entities.Transaction;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

@Service
public class CheckService {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public File generateCheckFile(String fromAccountId) throws Exception {
        long min = 100000000000L;
        long max = 999999999999L;
        Random random = new Random();
        String fileName = "check_" + random.nextLong(max - min + 1) + min  + ".pdf";
        File pdfFile = new File("C:/Users/Павел/OneDrive/Рабочий стол/2 курс/3 семестр/ООПиП курсач/Bank/src/checks" + fileName);
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
        document.open();
        Account account = accountsRepository.findFirstByAccountid(Long.parseLong(fromAccountId));
        Transaction transaction = transactionRepository.findLastByFromAccount(account.getAccountnumber());
        BaseFont baseFont = BaseFont.createFont("C:/Users/Павел/Downloads/Arial Cyr/Arial Cyr.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 12);
        Paragraph title = new Paragraph("Банковский чек", font);
        Paragraph checkDetails = new Paragraph("Чек: " + System.currentTimeMillis(), font);
        Paragraph transactionDetails = new Paragraph(
                transaction.getDate() + " " + transaction.getTime() + "\n" +
                        "Тип транзакции: " + transaction.getType_tr() + "\n" +
                        "Валюта транзакции: " + transaction.getCurrency()+ "\n" +
                        "Счёт отправителя: " + transaction.getFromaccount() + "\n" +
                        "Счёт получателя: " + transaction.getToaccount() + "\n" +
                        "Сумма транзакции: " + transaction.getAmount(), font
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
        return pdfFile;
    }


}
