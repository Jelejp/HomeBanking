package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.CreateCardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.RandomNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients/current")
public class CardController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardRepository cardRepository;

    //METODO POST PARA CREARCARD
    @PostMapping("/cards")
    public ResponseEntity<?> createCard(@RequestBody CreateCardDTO createCardDTO, Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        //SI ESTAN VACIOS
        if (createCardDTO.type() == null ) {
            return new ResponseEntity<>("The type field is mandatory", HttpStatus.BAD_REQUEST);
        }
        if (createCardDTO.color() == null) {
            return new ResponseEntity<>("The colour field is mandatory", HttpStatus.BAD_REQUEST);
        }

        //
        long cardCountByType = client.getCards()
                .stream()
                .filter(card -> card.getType() == createCardDTO.type())
                .count();

        //SI  SUPERA EL LIMITE DE CARDS TYPE
        if (cardCountByType >= 3) {
            return new ResponseEntity<>("You have exceeded the limit of cards of the same type (3)", HttpStatus.FORBIDDEN);
        }


        //SI TIENE UNA CARD DEL MISMO TIPO Y COLOR
        if (client.getCards()
                .stream()
                .anyMatch(card -> card.getType() == createCardDTO.type() && card.getColor() == createCardDTO.color())) {
            return new ResponseEntity<>("You already have a card of the same type and colour", HttpStatus.FORBIDDEN);
        }

        //BLOQUE
        try {
            int cvv = RandomNumberUtils.generateCVV();

            String cardNumber;

            //BUCLE PARA QUE NO SE GENEREN LOS MISMOS NUMEROS DE CARD
            do {
                cardNumber = RandomNumberUtils.generateCardNumber();
            } while (cardRepository.findByNumber(cardNumber) != null);

            LocalDate currentDate = LocalDate.now();

            LocalDate expirationDate = currentDate.plusYears(5);

            // CREA CARD
            Card card = new Card(client, createCardDTO.type(), createCardDTO.color(), cardNumber, cvv, expirationDate, currentDate);
            card.setClient(client);

            client.getCards().add(card);

            cardRepository.save(card);

            clientRepository.save(client);

            return new ResponseEntity<>("Card successfully created", HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Error when creating the card", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //METODO GET PARA OBTENER LA CARD CREADA
    @GetMapping("/cards")
    public ResponseEntity<?> getClientCards (Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());

        Set<Card> cards = client.getCards();

        Set<CardDTO> cardDTOS = cards.stream()
                .map(CardDTO::new)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(cardDTOS, HttpStatus.OK);
    }
}