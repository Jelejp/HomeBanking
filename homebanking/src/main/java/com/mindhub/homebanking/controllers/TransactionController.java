package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CreateTransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/")
    @Transactional
    public ResponseEntity<?> createTransaction (@RequestBody CreateTransactionDTO createTransactionDTO, Authentication authentication) {

        //OBTENGO CLIENT AUTENTICADO CON EL MAIL
        Client client = clientRepository.findByEmail(authentication.getName());

        // SI EL CAMPO DE DESCRIPCIÓN ESTA VACÍO
        if (createTransactionDTO.description().isBlank()) {
            return new ResponseEntity<>("The description field cannot be empty.", HttpStatus.FORBIDDEN);
        }

        //SI EL MONTO ES NAN (UN NÚM NO VÁLIDO)
        if (Double.isNaN(createTransactionDTO.amount())) {
            return new ResponseEntity<>("The field amount cannot be empty.", HttpStatus.FORBIDDEN);
        }

        //SI EL MONTO ES 0
        if (createTransactionDTO.amount() == 0.0) {
            return new ResponseEntity<>("The amount has to be greater than 0.1", HttpStatus.FORBIDDEN);
        }

        //SI EL CAMPO CUENTA DE ORIGEN ESTA VACÍO
        if (createTransactionDTO.sourceAccount().isBlank()) {
            return new ResponseEntity<>("The source account field cannot be empty.", HttpStatus.FORBIDDEN);
        }

        //SI EL CAMPO CUENTA DE DESTINO ESTA VACÍO
        if (createTransactionDTO.destinationAccount().isBlank()) {
            return new ResponseEntity<>("The destination account field cannot be empty.", HttpStatus.FORBIDDEN);
        }

        //BUSCA EL NUMERO DE CUENTA DE ORIGEN
        Account existingSourceAccount = accountRepository.findByNumber(createTransactionDTO.sourceAccount());

        //SI LA CUENTA DE ORIGEN NO EXISTE
        if (existingSourceAccount == null) {
            return new ResponseEntity<>("The source account number does not exist", HttpStatus.FORBIDDEN);
        }

        //BUSCA POR EL NUMERO DE CUENTA DE DESTINO
        Account existingDestinationAccount = accountRepository.findByNumber(createTransactionDTO.destinationAccount());

        //SI LA CUENTA DE DESTINO NO EXISTE
        if (existingDestinationAccount == null) {
            return new ResponseEntity<>("The destination account number does not exist", HttpStatus.FORBIDDEN);
        }

        //COMPARA CADA NUMERO DE CUENTA CON LA CUENTA DE ORIGEN QUE SE RECIBE
        boolean accountExists = client.getAccounts()
                .stream()
                .anyMatch(account -> account.getNumber().equals(createTransactionDTO.sourceAccount()));

        //SI LA CUENTA NO PERTENECE A UN CLIENT AUTENTICADO
        if (!accountExists) {
            return new ResponseEntity<>("The source account does not belong to the client", HttpStatus.FORBIDDEN);
        }

        //SI LA CUENTA DE ORIGEN NO TIENE SALDO
        if (existingSourceAccount.getBalance() == 0.0) {
            return new ResponseEntity<>("You don't have sufficient funds", HttpStatus.FORBIDDEN);
        }

        //SI LA CUENTA DE ORIGEN TIENE UN SALDO MENOR AL QUE QUIERE MANDAR
        if (existingSourceAccount.getBalance() < createTransactionDTO.amount()) {
            return new ResponseEntity<>("You don't have sufficient funds", HttpStatus.FORBIDDEN);
        }

        //SI LA CUENTA DE ORIGEN ES LA MISMA QUE LA CUENTA DE DESTINO
        if (existingSourceAccount == existingDestinationAccount) {
            return new ResponseEntity<>("The source account is the same as the destination account", HttpStatus.FORBIDDEN);
        }

        //
        LocalDateTime date = LocalDateTime.now();
        String descriptionDebit = createTransactionDTO.description() + " " + "from" + " " + existingSourceAccount.getNumber();
        String descriptionCredit = createTransactionDTO.description() + " " + "to" + " " + existingDestinationAccount.getNumber();
        double amountDebit = - createTransactionDTO.amount();

        //CREO TRANSACTIONS
        Transaction transactionDebit = new Transaction(TransactionType.DEBIT, amountDebit, descriptionDebit, date);
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT, createTransactionDTO.amount(), descriptionCredit, date);

        //ASOCIO TRASACTION CON SU CUENTA CORRESPONDIENTE
        existingSourceAccount.addTransaction(transactionDebit);
        existingDestinationAccount.addTransaction(transactionCredit);

        //ACTUALIZO LOS SALDOS DE LAS CUENTAS
        existingSourceAccount.setBalance( existingSourceAccount.getBalance() - createTransactionDTO.amount()) ;
        existingDestinationAccount.setBalance(existingDestinationAccount.getBalance() + createTransactionDTO.amount());

        //GUARDO TRANSACTION EN EL REPOSITORIO
        transactionRepository.save(transactionDebit);
        transactionRepository.save(transactionCredit);

        //GUARDO CUENTAS ACTUALIZADAS EN EL REPOSITORIÓ
        accountRepository.save(existingSourceAccount);
        accountRepository.save(existingDestinationAccount);

        return new ResponseEntity<>("Your transaction was sent successfully!", HttpStatus.CREATED);
    }
}