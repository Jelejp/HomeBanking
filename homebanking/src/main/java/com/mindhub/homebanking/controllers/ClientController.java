package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @GetMapping("/hello")
    public String getClients(){
        return "Hello Clients!";
    }

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/")
    //public List<Client> getAllClients(){
        //return clientRepository.findAll();
    public ResponseEntity<List<ClientDTO>> getAllClients(){
        // DEVUELVE TODOS LOS CLIENTS
        List<Client> clients = clientRepository.findAll();
        return new ResponseEntity<>(clients.stream().map(client -> new ClientDTO(client)).collect(java.util.stream.Collectors.toList()), HttpStatus.OK);
    }

    //VARIABLE DE RUTA
    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id){
        //BUSCAR CLIENT POR ID
        Client client = clientRepository.findById(id).orElse(null);
        //SI NO EXISTE
        if (client == null){
            return new ResponseEntity<>("No customer with this id was found.", HttpStatus.NOT_FOUND);
        }
        //SI EXISTE
        ClientDTO dtoClient = new ClientDTO(client);
        return new ResponseEntity<>(dtoClient, HttpStatus.OK);
    }

}
