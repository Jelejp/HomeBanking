package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
//PROPIEDADES
    private long id ;

    private String number ;

    private LocalDate createdDate ;

    private double balance ;

    private Set<TransactionDTO> transactions;

//CONSTRUCTOR
    public AccountDTO(Account account){
        this.id = account.getId();
        this.number = account.getNumber();
        this.createdDate = account.getCreatedDate();
        this.balance = account.getBalance();

        this.transactions = account.getTransactions()
                .stream()
                .map(transaction -> new TransactionDTO(transaction))
                .collect(Collectors.toSet());
    }

//GETTERS
    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public double getBalance() {
        return balance;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
}