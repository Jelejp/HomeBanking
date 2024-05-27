package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.RegisterDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.servicesSecurity.AccountService;
import com.mindhub.homebanking.utils.RandomNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @GetMapping("/")
    public ResponseEntity<?>getAllAccounts(){
        List<Account> accounts = accountRepository.findAll();

        //SI NO HAY ACCOUNTS
        if(accounts.isEmpty()){
            return new ResponseEntity<>("No accounts found.", HttpStatus.NOT_FOUND);
        }
        //SI HAY
        return new ResponseEntity<>(accounts.stream()
                .map(account -> new AccountDTO(account))
                .collect(java.util.stream.Collectors.toList()), HttpStatus.OK);
    }

    //VARIABLE DE RUTA
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id){
        //BUSCAR ACCOUNT POR ID
        Account account = accountRepository.findById(id).orElse(null);
        //SI NO EXISTE
        if (account == null){
            return new ResponseEntity<>("No account with this id was found.", HttpStatus.NOT_FOUND);
        }
        //SI EXISTE
        AccountDTO accountDTO = new AccountDTO(account);
        return new ResponseEntity<>(accountDTO, HttpStatus.OK);
    }

    @Autowired
    ClientRepository clientRepository;


    @PostMapping("/current/accounts")
    public ResponseEntity<?> createAccount (Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        return accountService.createAccount(client);

    }
}