package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.CreateCardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/clients/current")
public class CardController {

   @Autowired
    private CardService cardService;

    //METODO POST PARA CREARCARD
    @PostMapping("/cards")
    public ResponseEntity<?> createCard(@RequestBody CreateCardDTO createCardDTO, Authentication authentication) {
        Client client = cardService.getAuthenticatedClient(authentication.getName());

        return cardService.createCard(client, createCardDTO);
    }

    //METODO GET PARA OBTENER LA CARD CREADA
    @GetMapping("/cards")
    public ResponseEntity<?> getClientCards (Authentication authentication) {

        Client client = cardService.getAuthenticatedClient(authentication.getName());
        Set<CardDTO> cardDTOS = cardService.getCardDTOsForClient(client);

        return new ResponseEntity<>(cardDTOS, HttpStatus.OK);
    }
}