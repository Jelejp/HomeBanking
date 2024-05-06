package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.mindhub.homebanking.repositories.ClientRepository;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository){
		return (args  ) -> {

			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Juana", "Lopez", "juanalopez@gmail.com");
			Client client3 = new Client("Juan", "Perez", "juanperez@gmail.com");

			LocalDate today = LocalDate.now();
			LocalDate tomorrow = today.plusDays(1);

			Account account1_client1 = new Account("VIN001", today, 5000);
			Account account2_client1 = new Account("VIN002", tomorrow, 7500);

			Account account1_client2 = new Account("VIN010", today, 5500);
			Account account2_client2 = new Account("VIN011", tomorrow, 8000);

			Account account1_client3 = new Account("VIN020", today, 4000);
			Account account2_client3 = new Account("VIN021", tomorrow, 1000);

			client1.addAccount(account1_client1);
			client1.addAccount(account2_client1);
			client2.addAccount(account1_client2);
			client2.addAccount(account2_client2);
			client3.addAccount(account1_client3);
			client3.addAccount(account2_client3);


			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);

			accountRepository.save(account1_client1);
			accountRepository.save(account2_client1);
			accountRepository.save(account1_client2);
			accountRepository.save(account2_client2);
			accountRepository.save(account1_client3);
			accountRepository.save(account2_client3);


			System.out.println(client1);
			System.out.println(client2);
			System.out.println(client3);

		};

	}

}
