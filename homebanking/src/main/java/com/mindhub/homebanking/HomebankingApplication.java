package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository){
		return (args  ) -> {
			System.out.println("hola");
			System.out.println("ola");

			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Juana", "Lopez", "juanalopez@gmail.com");
			Client client3 = new Client("Juan", "Perez", "juanperez@gmail.com");
			Client client4 = new Client("Dante", "Herrera", "danteherrera@gmail.com");

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);
			clientRepository.save(client4);

			System.out.println(client1);
			System.out.println(client2);
			System.out.println(client3);
			System.out.println(client4);



		};

	}


}
