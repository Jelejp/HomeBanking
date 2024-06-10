package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    //VERIFICA QUE EL METODO FUNCIONE CORRECTAMENTE CUANDO SE LE PASA UN NUMERO DE CUENTA VALIDO
    @Test
    public void findByNumber() {
        Account account = accountRepository.findByNumber("VIN-011");
        assertThat(account, is(notNullValue()));
        assertThat(account.getNumber(), is("VIN-011"));
    }

    //VERIFICA SI EXISTE UNA CUENTA CON EL NUMERO
    @Test
    public void existByNumber() {
        boolean exists = accountRepository.existsByNumber("VIN-011");
        assertThat(exists, is(true));
    }

    @Test
    public void existAccounts() {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, is(not(empty())));
    }
}