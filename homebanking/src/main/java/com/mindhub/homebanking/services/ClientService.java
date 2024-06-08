package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {

    List<Client> getListClients();

    List<ClientDTO> getListClientsDTO();

    Client getClientById(Long id);

    ClientDTO getClientDTO(Client client);

    Client getClientByEmail(String email);

    Client findByEmail (String email);

    void saveClient(Client client);
}
