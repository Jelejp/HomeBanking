package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {
	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return (args  ) -> {
			//CREO INSTANCIAS DE CLIENTS
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("123"));
			Client client2 = new Client("Juana", "Lopez", "juanalopez@gmail.com", passwordEncoder.encode("345"));

			LocalDate today = LocalDate.now();
			LocalDate tomorrow = today.plusDays(1);
			LocalDateTime date = LocalDateTime.now();
			LocalDate expirationDate = today.plusYears(5);

			//LISTAS DE PLAZOS
			List<Integer> mortgageInstallments = List.of(12, 24, 36, 48, 60);
			List<Integer>personalInstallments = List.of(6, 12, 24);
			List<Integer>automotiveInstallments = List.of(6, 12, 24);

			//CREO INSTANCIAS DE ACCOUNTS
			Account account1_client1 = new Account("VIN-001", today, 5000);
			Account account2_client1 = new Account("VIN-002", tomorrow, 7500);

			Account account1_client2 = new Account("VIN-010", today, 5500);
			Account account2_client2 = new Account("VIN-011", tomorrow, 8000);

			//CREO TRANSACTIONS
			Transaction transaction1_account1_client1 = new Transaction(TransactionType.DEBIT, -2000, "Purchase in supermarket", date);
			Transaction transaction2_account1_client1 = new Transaction(TransactionType.CREDIT, 1000, "Purchase in supermarket", date);
			Transaction transaction3_account1_client1 = new Transaction(TransactionType.DEBIT, -2500, "Purchase in greengrocer's", date);

			Transaction transaction1_account2_client1 = new Transaction(TransactionType.DEBIT, -1000, "Purchase in supermarket", date);
			Transaction transaction2_account2_client1 = new Transaction(TransactionType.CREDIT, 3000, "Purchase in pharmacy", date);
			Transaction transaction3_account2_client1 = new Transaction(TransactionType.DEBIT, -4500, "Purchase in supermarket", date);

			Transaction transaction1_account1_client2 = new Transaction(TransactionType.DEBIT, -5500, "Purchase in bookshop", date);
			Transaction transaction2_account1_client2 = new Transaction(TransactionType.CREDIT, 5000, "Purchase in toy shops", date);
			Transaction transaction3_account1_client2 = new Transaction(TransactionType.DEBIT, -500, "Purchase in bakery", date);

			Transaction transaction1_account2_client2 = new Transaction(TransactionType.DEBIT, -1800, "Purchase in dietetics", date);
			Transaction transaction2_account2_client2 = new Transaction(TransactionType.DEBIT, -3100, "Purchase pharmacy", date);
			Transaction transaction3_account2_client2 = new Transaction(TransactionType.DEBIT, -3050, "Purchase in butcher's ", date);

			//CREO PRESTAMOS
			Loan mortgageLoan = new Loan("Mortgage", 500000, mortgageInstallments);
			Loan personalLoan = new Loan("Personal", 100000, personalInstallments);
			Loan automotiveLoan = new Loan("Automotive", 300000, automotiveInstallments);

			//CREO CLIENTLOAN
			ClientLoan clientLoan1 = new ClientLoan(400000, 60);
			ClientLoan clientLoan2 = new ClientLoan(50000, 12);

			ClientLoan clientLoan3 = new ClientLoan(100000, 24);
			ClientLoan clientLoan4 = new ClientLoan(200000, 36);

			//CREO CARD
			Card card1 = new Card(client1, CardType.DEBIT, CardColor.GOLD, "5254-5011-6245-9531", 308, expirationDate, today);
			Card card2 = new Card(client1, CardType.CREDIT, CardColor.TITANIUM, "5254-0262-9654-8536", 905, expirationDate, today);
			Card card3 = new Card(client2, CardType.CREDIT, CardColor.SILVER, "5254-6200-1717-6210", 211, expirationDate, today);

//METODOS
			//METODO ADDACCOUNT
			client1.addAccount(account1_client1);
			client1.addAccount(account2_client1);
			client2.addAccount(account1_client2);
			client2.addAccount(account2_client2);

            //METODO ADDTRANSACTION
			account1_client1.addTransaction(transaction1_account1_client1);
			account1_client1.addTransaction(transaction2_account1_client1);
			account1_client1.addTransaction(transaction3_account1_client1);

			account2_client1.addTransaction(transaction1_account2_client1);
			account2_client1.addTransaction(transaction2_account2_client1);
			account2_client1.addTransaction(transaction3_account2_client1);

			account1_client2.addTransaction(transaction1_account1_client2);
			account1_client2.addTransaction(transaction2_account1_client2);
			account1_client2.addTransaction(transaction3_account1_client2);

			account2_client2.addTransaction(transaction1_account2_client2);
			account2_client2.addTransaction(transaction2_account2_client2);
			account2_client2.addTransaction(transaction3_account2_client2);

			//METODO ADDCLIENTLOAN
			client1.addClientLoan(clientLoan1);
			mortgageLoan.addClientLoan(clientLoan1);

			client1.addClientLoan(clientLoan2);
			personalLoan.addClientLoan(clientLoan2);

			client2.addClientLoan(clientLoan3);
			personalLoan.addClientLoan(clientLoan3);

			client2.addClientLoan(clientLoan4);
			automotiveLoan.addClientLoan(clientLoan4);

			//METODO ADDCARD
			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);

//REPOSITORIO
			//GUARDO CLIENT EN LA BASE DE DATOS
			clientRepository.save(client1);
			clientRepository.save(client2);

			// ACCOUNT
			accountRepository.save(account1_client1);
			accountRepository.save(account2_client1);
			accountRepository.save(account1_client2);
			accountRepository.save(account2_client2);

			//TRANSACTION
			//CLIENT 1
			transactionRepository.save(transaction1_account1_client1);
			transactionRepository.save(transaction2_account1_client1);
			transactionRepository.save(transaction3_account1_client1);

			transactionRepository.save(transaction1_account2_client1);
			transactionRepository.save(transaction2_account2_client1);
			transactionRepository.save(transaction3_account2_client1);
			//CLIENT 2
			transactionRepository.save(transaction1_account1_client2);
			transactionRepository.save(transaction2_account1_client2);
			transactionRepository.save(transaction3_account1_client2);

			transactionRepository.save(transaction1_account2_client2);
			transactionRepository.save(transaction2_account2_client2);
			transactionRepository.save(transaction3_account2_client2);

			//PRESTAMOS
			loanRepository.save(mortgageLoan);
			loanRepository.save(personalLoan);
			loanRepository.save(automotiveLoan);

			//CLIENTLOAN
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			//CARD
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);

//SOUNT
			//IMPRIME POR CONSOLA CLIENT
			System.out.println(client1);
			System.out.println(client2);

			System.out.println(client1.getLoans());
			System.out.println(mortgageLoan.getClient());
		};

	}
}