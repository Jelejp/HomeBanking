package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {
//PROPIEDADES
    //PK
    @Id
    //ID AUTOMATICAMENTE
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firsName;
    private String lastName;
    private String email;

//RELACION
    @OneToMany(mappedBy = "client", fetch =FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();

//CONSTRUCTORES
    public Client() {
    }

    public Client(String firsName, String lastName, String email) {
        this.firsName = firsName;
        this.lastName = lastName;
        this.email = email;
    }

//GETTERS Y SETTERS

    public long getId() {
        return id;
    }

    public String getFirsName() {
        return firsName;
    }

    public void setFirsName(String firsName) {
        this.firsName = firsName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    //METODOS PROPIOS
    public void addAccount(Account account){
        account.setClient(this);
        accounts.add(account);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firsName='" + firsName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", accounts=" + accounts +
                '}';
    }
}
