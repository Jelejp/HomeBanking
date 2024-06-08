package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.CreateCardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.RandomNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientService clientService;

    @Override
    public Client getAuthenticatedClient(String email) {
        return clientService.findByEmail(email);
    }

    @Override
    public long countCardsByType(Client client, String type) {
        CardType cardType = validateAndConvertCardType(type);
        //CUENTO CANTIDAD DE CADS DE CLIENT
        return client.getCards()
                .stream()
                .filter(card -> card.getType() == cardType)
                .count();
    }

    @Override
    public boolean clientHasCardOfTypeAndColor(Client client, String type, String color) {
        CardType cardType = validateAndConvertCardType(type);
        CardColor cardColor = validateAndConvertCardColor(color);
        return client.getCards()
                .stream()
                .anyMatch(card -> card.getType() == cardType && card.getColor() == cardColor);
    }

    @Override
    public ResponseEntity<String> validateCardFields(CreateCardDTO createCardDTO) {

        //SI ESTAN VACIOS
        if (createCardDTO.type() == null || createCardDTO.type().isBlank()) {
            return new ResponseEntity<>("The type field is mandatory", HttpStatus.FORBIDDEN);
        }
        if (createCardDTO.color() == null || createCardDTO.color().isBlank()) {
            return new ResponseEntity<>("The colour field is mandatory", HttpStatus.FORBIDDEN);
        }
        return null;
    }

    @Override
    public String generateUniqueCardNumber() {
        //BUCLE PARA QUE NO SE GENEREN LOS MISMOS NUMEROS DE CARD
        String cardNumber;
        do {
            cardNumber = RandomNumberUtils.generateCardNumber();
        } while (cardRepository.findByNumber(cardNumber) != null);
        return cardNumber;
    }

    @Override
    public int generateCVV() {
        return RandomNumberUtils.generateCVV();
    }

    @Override
    public ResponseEntity<String> createCard(Client client, CreateCardDTO createCardDTO) {

        //VALIDA QUE LOS CAMPOS NO ESTEN VACIOS
        ResponseEntity<String> validation = validateCardFields(createCardDTO);
        if (validation != null) {
            return validation;
        }

        //BLOQUE
        try {
            //VALIDA CARD TYPE Y COLOR
            CardType cardType = validateAndConvertCardType(createCardDTO.type());
            CardColor cardColor = validateAndConvertCardColor(createCardDTO.color());

            long cardCountByType = countCardsByType(client, createCardDTO.type());

            //SI  SUPERA EL LIMITE DE CARDS TYPE
            if (cardCountByType >= 3) {
                return new ResponseEntity<>("You have exceeded the limit of cards of the same type (3)", HttpStatus.FORBIDDEN);
            }

            //SI TIENE UNA CARD DEL MISMO TIPO Y COLOR
            if (clientHasCardOfTypeAndColor(client, createCardDTO.type(), createCardDTO.color())) {
                return new ResponseEntity<>("You already have a card of the same type and colour", HttpStatus.FORBIDDEN);
            }

            int cvv = generateCVV();
            String cardNumber = generateUniqueCardNumber();
            LocalDate currentDate = LocalDate.now();
            LocalDate expirationDate = currentDate.plusYears(5);

            // CREA CARD
            Card card = new Card(client, cardType, cardColor, cardNumber, cvv, expirationDate, currentDate);

            client.addCard(card);

            saveCard(card);
            clientService.saveClient(client);

            return new ResponseEntity<>("Card successfully created", HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid card type or colour provided", HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>("Error when creating the card", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Set<CardDTO> getCardDTOsForClient(Client client) {
        return client.getCards()
                .stream()
                .map(CardDTO::new)
                .collect(Collectors.toSet());
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    //AUX
    private CardType validateAndConvertCardType(String type) {
        try {
            return CardType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid card type provided");
        }
    }

    private CardColor validateAndConvertCardColor(String color) {
        try {
            return CardColor.valueOf(color.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid card color provided");
        }
    }

}