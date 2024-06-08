package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface AccountService {
    List<Account> getAllAccounts();

    Account getAccountById(Long id);

    Client getAuthenticatedClient(String email);

    List<AccountDTO> convertAccountsToDTOs(List<Account> accounts);

    Set<AccountDTO> getAccountsOfCurrentClient(Client client);

    ResponseEntity<?> createAccount (Client client);

    void saveAccount(Account account);

    Account findByNumber(String number);

    boolean existsByNumber(String number);
}
