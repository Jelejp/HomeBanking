package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CreateTransactionDTO;
import com.mindhub.homebanking.servicesSecurity.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    // Controlador para manejar la solicitud de creación de transacción
    @PostMapping("/")
    public ResponseEntity<?> createTransaction(@RequestBody CreateTransactionDTO createTransactionDTO, Authentication authentication) {
        try {
            transactionService.processTransaction(createTransactionDTO, authentication.getName());
            return new ResponseEntity<>("Your transaction was sent successfully!", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}