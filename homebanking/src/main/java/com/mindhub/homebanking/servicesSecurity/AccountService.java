package com.mindhub.homebanking.servicesSecurity;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.utils.RandomNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<?> createAccount (Client client) {

        //si tiene tres cuentas
        if (client.getAccounts().size() >= 3){
            return new  ResponseEntity<>("You have reached the number of accounts created (3). You cannot create a new account.", HttpStatus.FORBIDDEN);
        }

        String accountNumber;
        do {
            accountNumber = "VIN-" + RandomNumberUtils.getRandomNumber(111, 99999999);
        }while (accountRepository.findByNumber(accountNumber) != null);

        LocalDate createdDate = LocalDate.now();
        double balance = 0.0;
        Account account = new Account(accountNumber,createdDate, balance);

        client.addAccount(account);
        accountRepository.save(account);

        return new ResponseEntity<>("Account created!", HttpStatus.CREATED);
    }
}
