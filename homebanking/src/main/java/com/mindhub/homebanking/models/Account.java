package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.time.LocalDate;

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

//RELACION
    @ManyToOne(fetch =FetchType.EAGER)
    private Client client;

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

 //METODOS PROPIOS

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", createdDate=" + createdDate +
                ", balance=" + balance +
                '}';
    }
}