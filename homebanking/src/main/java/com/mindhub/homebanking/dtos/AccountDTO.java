package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

public class AccountDTO {
    private long id ;
    private String number ;
    private LocalDate createdDate ;
    private double balance ;

//CONSTRUCTORES

    public AccountDTO(Account account){
        this.id = account.getId();
        this.number = account.getNumber();
        this.createdDate = account.getCreatedDate();
        this.balance = account.getBalance();
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
}



