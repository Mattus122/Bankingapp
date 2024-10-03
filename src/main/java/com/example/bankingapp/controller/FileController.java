package com.example.bankingapp.controller;

import com.example.bankingapp.repository.AccountRepository;
import com.example.bankingapp.repository.TransactionRepository;
import com.example.bankingapp.repository.UserRepository;
import com.example.bankingapp.util.CsvFileGenerator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class FileController {
    private final UserRepository userRepository;
    private final CsvFileGenerator csvFileGenerator;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @GetMapping("/export-to-csv")
    public void exportIntoCSV(HttpServletResponse response) throws IOException{
        response.setContentType("text/csv");
        response.addHeader("Content-Disposition", "attachment; filename=\"user.csv\"");
        csvFileGenerator.writeUserToCsv(userRepository.findAll(), response.getWriter());
    }

}

