package com.mindhub.homebanking.servicesSecurity;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServicelmpl implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Client client = clientRepository.findByEmail(username);

        if (client == null){
            throw new UsernameNotFoundException(username);
        }

        String role;
        if (client.isAdmin()){
            role = "ADMIN";
        } else {
            role = "CLIENT";
        }

        return User
                .withUsername(username)
                .password(client.getPassword())
                .roles(role)
                .build();
    }
}