package com.mindhub.homebanking.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

//PRUEBAS UNITARIAS
@SpringBootTest
public class CardUtilsTest {

    //VERIFICA QUE EL METODO RETORNE UNA CADENA QUE NO SEA NULA NI VACIA
    @Test
    void cardNumberIsCreated() {
        String cardNumber = CardUtils.generateCardNumber();
        assertThat(cardNumber, is(not(emptyOrNullString())));
    }

    //VERIFICA QUE EL NUMERO GENERADO TENGA UN FORMATO CORRECTO
    @Test
    public void cardNumberHasCorrectFormat() {
        String cardNumber = CardUtils.generateCardNumber();
        System.out.println("Generated card number: " + cardNumber);
        assertThat(cardNumber, matchesPattern("\\d{4}-\\d{4}-\\d{4}-\\d{4}"));
    }


    //VERIFICA QUE EL METODO RETORNE UN NUMERO ENTRE 100 Y 999
    @Test
    public void cvvIsThreeDigits() {
        int cvv = CardUtils.generateCVV();
        assertThat(cvv, allOf(greaterThanOrEqualTo(100), lessThanOrEqualTo(999)));
    }
}