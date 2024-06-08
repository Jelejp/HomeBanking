package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CreateTransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;

public interface TransactionService {

    ResponseEntity<?> processTransaction(CreateTransactionDTO createTransactionDTO, Authentication authentication);

    Client getAuthenticatedClient(String email);

    ResponseEntity<?> validateTransaction(CreateTransactionDTO createTransactionDTO);

    Account getAccountByNumber(String accountNumber);

    boolean accountBelongsClient(Account account, Client client);

    void updateAccountBalances(Account sourceAccount, Account destinationAccount, double amount);

    Transaction createTransaction(Account account, double amount, String description, TransactionType type, LocalDateTime date);

    void saveTransactions(Transaction transactionDebit, Transaction transactionCredit);
    void saveTransaction(Transaction transaction);
}