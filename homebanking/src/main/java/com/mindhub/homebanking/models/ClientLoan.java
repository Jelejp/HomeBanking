package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ClientLoan {
//PROPIEDADES
    //PK
    @Id
    //ID AUTOMATICAMENTE
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double amount;

    private Integer payments;

    //RELACION
    @ManyToOne
    private Client client;

    //RELACION
    @ManyToOne
    private Loan loan;

//CONSTRUCTORES
    public ClientLoan() {
    }

    public ClientLoan(double amount, Integer payments) {
        this.amount = amount;
        this.payments = payments;
    }

//GETTERS Y SETTERS
    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public void setPayments(Integer payments) {
        this.payments = payments;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}