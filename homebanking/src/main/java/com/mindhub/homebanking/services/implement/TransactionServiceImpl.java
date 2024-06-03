package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.CreateTransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional
    public ResponseEntity<?> processTransaction(CreateTransactionDTO createTransactionDTO, Authentication authentication) {
        Client client = getAuthenticatedClient(authentication.getName());
        ResponseEntity<?> validateResponse = validateTransaction(createTransactionDTO);
        if (validateResponse != null) {
            return validateResponse;
        }

        Account sourceAccount = getAccountByNumber(createTransactionDTO.sourceAccount());
        Account destinationAccount = getAccountByNumber(createTransactionDTO.destinationAccount());

        if (!accountBelongsClient(sourceAccount, client)) {
            return new ResponseEntity<>("The source account does not belong to the client", HttpStatus.FORBIDDEN);
        }

        double amount = createTransactionDTO.amount();

        if (sourceAccount.getBalance() < amount) {
            return new ResponseEntity<>("You don't have sufficient funds", HttpStatus.FORBIDDEN);
        }

        performTransaction(sourceAccount, destinationAccount, amount, createTransactionDTO.description());

        return new ResponseEntity<>("Your transaction was sent successfully!", HttpStatus.CREATED);
    }

    @Override
    public Client getAuthenticatedClient(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public ResponseEntity<?> validateTransaction(CreateTransactionDTO createTransactionDTO) {
        if (createTransactionDTO.description().isBlank()) {
            return new ResponseEntity<>("The description field cannot be empty.", HttpStatus.FORBIDDEN);
        }

        if (createTransactionDTO.amount() <= 0) {
            return new ResponseEntity<>("The amount has to be greater than 0", HttpStatus.FORBIDDEN);
        }

        if (createTransactionDTO.sourceAccount().isBlank() || createTransactionDTO.destinationAccount().isBlank()) {
            return new ResponseEntity<>("The source or destination account fields cannot be empty.", HttpStatus.FORBIDDEN);
        }

        return null;
    }

    @Override
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByNumber(accountNumber);
    }

    @Override
    public boolean accountBelongsClient(Account account, Client client) {
        return account != null && client.getAccounts().contains(account);
    }

    private void performTransaction(Account sourceAccount, Account destinationAccount, double amount, String description) {
        LocalDateTime date = LocalDateTime.now();
        Transaction transactionDebit = createTransaction(sourceAccount, -amount, description, TransactionType.DEBIT, date);
        Transaction transactionCredit = createTransaction(destinationAccount, amount, description, TransactionType.CREDIT, date);

        updateAccountBalances(sourceAccount, destinationAccount, amount);
        saveTransactions(transactionDebit, transactionCredit);
    }

    public void updateAccountBalances(Account sourceAccount, Account destinationAccount, double amount) {
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
    }

    public Transaction createTransaction(Account account, double amount, String description, TransactionType type, LocalDateTime date) {
        return new Transaction(type, amount, description, date);
    }

    public void saveTransactions(Transaction transactionDebit, Transaction transactionCredit) {
        transactionRepository.save(transactionDebit);
        transactionRepository.save(transactionCredit);
    }
}