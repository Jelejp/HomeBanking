package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryTest {
    @Autowired
    private ClientRepository clientRepository;

/*    @Test
    public void canClientByEmail() {
        Client client = clientRepository.findByEmail("melba@mindhub.com");
        assertThat(client, is(notNullValue()));
        assertThat(client.getEmail(), is("melba@mindhub.com"));
        assertThat(client.getFirstName(), is("Melba"));
        assertThat(client.getLastName(), is("Morel"));
    }

    @Test
    public void canGetAllClient(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
        assertThat(clients.size(), is(greaterThan(0)));
    }*/
}