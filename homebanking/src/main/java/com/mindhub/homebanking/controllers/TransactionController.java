package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CreateTransactionDTO;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/")
    public ResponseEntity<?> createTransaction(@RequestBody CreateTransactionDTO createTransactionDTO, Authentication authentication) {
        ResponseEntity<?> response = transactionService.processTransaction(createTransactionDTO, authentication);
        return response;
    }
}