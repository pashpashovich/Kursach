package com.example.bank.controller;

import com.example.bank.services.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;

@Controller
public class CheckDownloadController {

    @Autowired
    private CheckService checkService;

    @RequestMapping(value = "/download/{fromId}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fromId, Model model) {
        try {
            File pdfFile = checkService.generateCheckFile(fromId);
            byte[] fileContent = Files.readAllBytes(pdfFile.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", pdfFile.getName());
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
