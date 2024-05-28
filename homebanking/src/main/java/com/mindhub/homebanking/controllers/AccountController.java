package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.servicesSecurity.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @GetMapping("/accounts")
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
    @GetMapping("/accounts/{id}")
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

//CREAR ACCOUNT
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<?> createAccount (Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        return accountService.createAccount(client);

    }

    @GetMapping("/clients/current/accounts")
    public ResponseEntity<?> getAccountsOfCurrentClient(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client == null) {
            return new ResponseEntity<>("Client not fount", HttpStatus.NOT_FOUND);
        }

        Set<Account> accounts = client.getAccounts();

        Set<AccountDTO> accountDTOS = accounts.stream()
                .map(AccountDTO::new)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(accountDTOS, HttpStatus.OK);
    }
}