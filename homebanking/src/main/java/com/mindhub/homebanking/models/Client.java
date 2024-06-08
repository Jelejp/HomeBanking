package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Client {
//PROPIEDADES
    //PK
    @Id
    //ID AUTOMATICAMENTE
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private boolean isAdmin = false;

    //RELACION 1 a M ACCOUNT
    @OneToMany(mappedBy = "client", fetch =FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    //RELACION 1 a M CLIENTLOAN
    @OneToMany(mappedBy = "client")
    private Set<ClientLoan> clientLoans = new HashSet<>();

    //RELACION 1 a M CARD
    @OneToMany(mappedBy = "client")
    private Set<Card> cards = new HashSet<>();

//CONSTRUCTORES
    public Client() {
    }

    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }



//GETTERS Y SETTERS
    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firsName) {
        this.firstName = firsName;
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

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
    //METODOS PROPIOS

    //ESTABLECE LA RELACION ENTRE LA C Y EL C QUE SE INSTANCIA
    //Y AGREGA LA C AL CONJUNTO DE C
    public void addAccount(Account account){
        account.setClient(this);
        accounts.add(account);
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public void addCard(Card card){
        card.setClient(this);
        cards.add(card);
    }

    public List<Loan> getLoans(){
        return clientLoans.stream()
                .map(ClientLoan ::getLoan)
                .collect(Collectors.toList());
    }

    public void createAdmin() {
        isAdmin = true;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", accounts=" + accounts +
                '}';
    }
}