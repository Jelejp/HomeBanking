package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/loans")
    public ResponseEntity<String> applyForLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {
        return loanService.applyForLoan(loanApplicationDTO, authentication);
    }

    @GetMapping("/loans")
    public ResponseEntity<?> getAllLoans() {
        if (loanService.getAllLoans().isEmpty()) {
            return new ResponseEntity<>("No loans found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(loanService.getAllLoans(), HttpStatus.OK);
    }
}