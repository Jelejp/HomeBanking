package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {SpringApplication.run(HomebankingApplication.class, args);}
	@Bean
	//por parametro inyecto interfaz para usarlo
	public CommandLineRunner initData(ClientRepository clientRepository) {
		return (args) -> {
			System.out.println("hola");

			Client client1 = new Client( "Juana", "Vasquez", "juanavasquez@gmail.com");
			Client client2 = new Client( "Gabriela", "Sanchez", "gabrielasanchez@gmail.com");
			Client client3 = new Client( "Juan", "Perez", "juanperez@gmail.com");
			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);
			System.out.println(client1);
			System.out.println(client2);
			System.out.println(client3);

		};
	}


}
