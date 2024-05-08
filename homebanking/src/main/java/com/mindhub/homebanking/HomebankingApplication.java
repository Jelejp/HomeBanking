package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository){
		return (args  ) -> {
			//CREO INSTANCIAS DE CLIENTS
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Juana", "Lopez", "juanalopez@gmail.com");

			LocalDate today = LocalDate.now();
			LocalDate tomorrow = today.plusDays(1);
			LocalDateTime date = LocalDateTime.now();

			//LISTAS DE PLAZOS
			List<Integer> mortgageInstallments = List.of(12, 24, 36, 48, 60);
			List<Integer>personalInstallments = List.of(6, 12, 24);
			List<Integer>automotiveInstallments = List.of(6, 12, 24);

			//CREO INSTANCIAS DE ACCOUNTS
			Account account1_client1 = new Account("VIN001", today, 5000);
			Account account2_client1 = new Account("VIN002", tomorrow, 7500);

			Account account1_client2 = new Account("VIN010", today, 5500);
			Account account2_client2 = new Account("VIN011", tomorrow, 8000);

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
			Loan mortgageLoan = new Loan("Mortgage", 500.000, mortgageInstallments);
			Loan personalLoan = new Loan("Personal", 100.000, personalInstallments);
			Loan automotiveLoan = new Loan("Automotive", 300.000, automotiveInstallments);

			//CREO CLIENTLOAN
			ClientLoan clientLoan1 = new ClientLoan(400.000, 60, client1, mortgageLoan);
			ClientLoan clientLoan2 = new ClientLoan(50.000, 12, client1, personalLoan);

			ClientLoan clientLoan3 = new ClientLoan(100.000, 24, client2, personalLoan);
			ClientLoan clientLoan4 = new ClientLoan(200.000, 36, client2, automotiveLoan);

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
			client1.addClientLoan(clientLoan2);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);

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

			//IMPRIME POR CONSOLA CLIENT
			System.out.println(client1);
			System.out.println(client2);
		};
	}
}