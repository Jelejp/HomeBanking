package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Card;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    //VERIFICA QUE EL METODO FUNCIONE CORRECTAMENTE CUANDO SE LE PASA UN NUMERO DE TARJETA VALIDO
    @Test
    public void findByNumber() {
        Card card = cardRepository.findByNumber("5254-5011-6245-9531");
        assertThat(card, is(notNullValue()));
        assertThat(card.getNumber(), is("5254-5011-6245-9531"));
    }

    //VERIFICA SI EXISTE UNA TARJETA CON EL NUMERO
    @Test
    public void existCards() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, is(not(empty())));
    }
}