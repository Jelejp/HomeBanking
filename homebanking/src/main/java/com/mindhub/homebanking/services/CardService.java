package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.CreateCardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface CardService {

    Client getAuthenticatedClient(String email);

    long countCardsByType(Client client, String type);

    boolean clientHasCardOfTypeAndColor(Client client, String type, String color);

    ResponseEntity<String> validateCardFields(CreateCardDTO createCardDTO);

    String generateUniqueCardNumber();

    int generateCVV();

    ResponseEntity<?> createCard(Client client, CreateCardDTO createCardDTO);

    Set<CardDTO> getCardDTOsForClient(Client client);

    void saveCard(Card card);

}