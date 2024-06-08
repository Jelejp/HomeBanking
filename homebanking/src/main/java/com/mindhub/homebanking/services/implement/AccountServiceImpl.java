package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.RandomNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientService clientService;

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Client getAuthenticatedClient(String email) {
      return clientService.findByEmail(email);
    }

    @Override
    public List<AccountDTO> convertAccountsToDTOs(List<Account> accounts) {
        return accounts.stream()
                .map(account -> new AccountDTO(account))
                .collect(Collectors.toList());
    }

    @Override
    public Set<AccountDTO> getAccountsOfCurrentClient(Client client) {
        return client.getAccounts().stream()
                .map(account -> new AccountDTO(account))
                .collect(Collectors.toSet());
    }

    @Override
    public ResponseEntity<?> createAccount(Client client) {
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
        saveAccount(account);

        return new ResponseEntity<>("Account created!", HttpStatus.CREATED);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public boolean existsByNumber(String number) {
        return accountRepository.findByNumber(number) != null;
    }
}