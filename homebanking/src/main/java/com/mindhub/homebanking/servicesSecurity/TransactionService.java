package com.mindhub.homebanking.servicesSecurity;

import com.mindhub.homebanking.dtos.CreateTransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Método para procesar la transacción
    @Transactional
    public void processTransaction(CreateTransactionDTO createTransactionDTO, String userEmail) {
        // Obtiene el cliente autenticado
        Client client = clientRepository.findByEmail(userEmail);

        // Obtiene las cuentas de origen y destino
        Account sourceAccount = accountRepository.findByNumber(createTransactionDTO.sourceAccount());
        Account destinationAccount = accountRepository.findByNumber(createTransactionDTO.destinationAccount());

        // Valida la transacción
        validateTransaction(client, sourceAccount, destinationAccount, createTransactionDTO.amount());

        // Obtiene la fecha actual
        LocalDateTime date = LocalDateTime.now();

        // Crea las descripciones para las transacciones
        String descriptionDebit = createTransactionDTO.description() + " from " + sourceAccount.getNumber();
        String descriptionCredit = createTransactionDTO.description() + " to " + destinationAccount.getNumber();

        // Crea las transacciones de débito y crédito
        double amountDebit = -createTransactionDTO.amount();
        Transaction transactionDebit = new Transaction(TransactionType.DEBIT, amountDebit, descriptionDebit, date);
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT, createTransactionDTO.amount(), descriptionCredit, date);

        // Asocia las transacciones con las cuentas correspondientes
        sourceAccount.addTransaction(transactionDebit);
        destinationAccount.addTransaction(transactionCredit);

        // Actualiza los saldos de las cuentas
        sourceAccount.setBalance(sourceAccount.getBalance() - createTransactionDTO.amount());
        destinationAccount.setBalance(destinationAccount.getBalance() + createTransactionDTO.amount());

        // Guarda las transacciones en el repositorio
        transactionRepository.save(transactionDebit);
        transactionRepository.save(transactionCredit);

        // Guarda las cuentas actualizadas en el repositorio
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
    }

    // Método para validar la transacción
    private void validateTransaction(Client client, Account sourceAccount, Account destinationAccount, double amount) {
        if (client == null) {
            throw new IllegalArgumentException("Client not found");
        }

        if (sourceAccount == null) {
            throw new IllegalArgumentException("Source account not found");
        }
        if (destinationAccount == null) {
            throw new IllegalArgumentException("Destination account not found");
        }
        if (amount <= 0.0) {
            throw new IllegalArgumentException("Amount must be greater than 0.1");
        }
        if (sourceAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds in the source account");
        }
        if (sourceAccount.equals(destinationAccount)) {
            throw new IllegalArgumentException("Source account cannot be the same as destination account");
        }
    }
}