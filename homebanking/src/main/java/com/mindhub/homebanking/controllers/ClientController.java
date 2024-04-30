package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.ClientRepository;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }
}


