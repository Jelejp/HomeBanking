package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
//PROPIEDADES
    //PK
    @Id
    //ID AUTOMATICAMENTE
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;

    private String number ;

    private LocalDate createdDate ;

    private double balance ;

//RELACION M a 1 CLIENT
    @ManyToOne(fetch =FetchType.EAGER)
    private Client client;

    //RELACION 1 a M TRANSACTION
    @OneToMany(mappedBy = "account", fetch =FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

//CONSTRUCTORES
    public Account() {
    }

    public Account(String number, LocalDate createdDate, double balance) {
        this.number = number;
        this.createdDate = createdDate;
        this.balance = balance;
    }

//GETTERS Y SETTERS
    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

//METODOS PROPIOS

    //ESTABLECE LA RELACION ENTRE LA T Y LA C QUE SE INSTANCIA
    //Y AGREGA LA T AL CONJUNTO DE T
    public void addTransaction(Transaction transaction){
       transaction.setAccount(this);
        transactions.add(transaction);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", createdDate=" + createdDate +
                ", balance=" + balance +
                ", transactions=" + transactions +
                '}';
    }
}