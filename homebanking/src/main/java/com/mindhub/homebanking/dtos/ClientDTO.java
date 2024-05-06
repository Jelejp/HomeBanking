package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;

import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private Long id;
    private String firsName;
    private String email;
    private Set<AccountDTO> accounts;

//CONSTRUCTOR CLIENT

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firsName = client.getFirsName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
    }
//GETTERS

    public Long getId() {
        return id;
    }

    public String getFirsName() {
        return firsName;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }
}
