package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/hello")
    public String getClients(){
        return "Hello Clients!";
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllClients(){

        //DEVUELVE TODOS LOS CLIENTS
        List<ClientDTO> clients = clientService.getListClientsDTO();

        //SI NO HAY
        if(clients.isEmpty()){
            return new ResponseEntity<>("No customers found.", HttpStatus.NOT_FOUND);
        }

        //SI HAY
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    //VARIABLE DE RUTA
    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id){

        //BUSCAR CLIENT POR ID
        Client client = clientService.getClientById(id);

        //SI NO EXISTE
        if (client == null){
            return new ResponseEntity<>("No customer with this id was found.", HttpStatus.NOT_FOUND);
        }
        //SI EXISTE
        ClientDTO dtoClient = clientService.getClientDTO(client);
        return new ResponseEntity<>(dtoClient, HttpStatus.OK);
    }
}