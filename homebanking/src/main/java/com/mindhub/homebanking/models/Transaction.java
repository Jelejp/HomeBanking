package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Transaction {
//PROPIEDADES
    //PK
    @Id
    //ID AUTOMATICAMENTE
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private TransactionType type;

    private double amount;

    private String description;

    private LocalDateTime date;

    //RELACION M a 1 ACCOUNT
    @ManyToOne(fetch =FetchType.EAGER)
    private Account account;

    //CONSTRUCTORES
    public Transaction() {
    }

    public Transaction(TransactionType type, double amount, String description, LocalDateTime date) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }
//GETTERS Y SETTERS

    public long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

//METODOS PROPIOS
    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}