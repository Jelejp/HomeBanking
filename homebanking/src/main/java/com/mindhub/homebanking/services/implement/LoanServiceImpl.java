package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.LoanApDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private ClientLoanRepository clientLoanRepository;


    @Override
    @Transactional
    public ResponseEntity<String> applyForLoan(LoanApplicationDTO loanApplicationDTO, Authentication authentication) {

        Client client = getAuthenticatedClient(authentication.getName());

        ResponseEntity<String> validationResponse = validateLoanApplication(loanApplicationDTO);
        if (validationResponse != null) {
            return validationResponse;
        }

        Account destinationAccount = getAccountByNumber(loanApplicationDTO.destinationAccount());
        if (!accountBelongsToClient(destinationAccount, client)) {
            return new ResponseEntity<>("The destination account does not belong to the client", HttpStatus.FORBIDDEN);
        }

        Loan loan = getLoanById(loanApplicationDTO.loandId());
        if (loan == null) {
            return new ResponseEntity<>("Loan not found", HttpStatus.BAD_REQUEST);
        }

        //CALCULO EL TOTAL DEL LOAN
        double totalAmount = calculateTotalAmount(loanApplicationDTO.amount(), loanApplicationDTO.installments());
        //CREO LA TRANSACCION
        Transaction transaction = createLoanTransaction(destinationAccount, totalAmount,
                "Loan approved" + " " + loan.getName(), LocalDateTime.now());
        //CREO NUEVO CLIENTLOAN
        ClientLoan clientLoan = new ClientLoan(totalAmount, loanApplicationDTO.installments());
        clientLoan.setClient(client); //ASIGNO EL CLIENTE DEL LOAN
        clientLoan.setLoan(loan); //ASIGNO EL LOAN
        clientLoanRepository.save(clientLoan); //GUARDO EL CLIENTLOAN

        destinationAccount.addTransaction(transaction);
        updateAccountBalance(destinationAccount, totalAmount);
        saveTransaction(transaction);


        return new ResponseEntity<>("Loan application successful", HttpStatus.CREATED);
    }

    @Override
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public ResponseEntity<String> validateLoanApplication(LoanApplicationDTO loanApplicationDTO) {

        // Verificar que los datos no estén vacíos y que el monto y las cuotas no sean 0
        if (loanApplicationDTO.amount() <= 0 || loanApplicationDTO.installments() <= 0) {
            return new ResponseEntity<>("Loan amount and installments must be greater than 0", HttpStatus.BAD_REQUEST);
        }

        // Verificar que el préstamo exista
        Loan loan = getLoanById(loanApplicationDTO.loandId());
        if (loan == null) {
            return new ResponseEntity<>("Loan not found", HttpStatus.BAD_REQUEST);
        }

        // Verificar que el monto solicitado no exceda el máximo permitido para el préstamo
        if (loanApplicationDTO.amount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("Requested amount exceeds maximum allowed amount", HttpStatus.BAD_REQUEST);
        }

        // Verificar que el número de cuotas esté entre las disponibles en el préstamo
        List<Integer> allowedPayments = loan.getPayments();
        if (!allowedPayments.contains(loanApplicationDTO.installments())) {
            return new ResponseEntity<>("Requested number of installments is not allowed", HttpStatus.BAD_REQUEST);
        }

        // Verificar que la cuenta de destino exista
        if (!accountService.existsByNumber(loanApplicationDTO.destinationAccount())) {
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.BAD_REQUEST);
        }

        return null; // Si todas las validaciones pasan, retorna null
    }


    @Override
    public Account getAccountByNumber(String accountNumber) {
        return accountService.findByNumber(accountNumber);
    }

    @Override
    public boolean accountBelongsToClient(Account account, Client client) {
        return client.getAccounts().contains(account);
    }

    @Override
    public double calculateTotalAmount(double loanAmount, int installments) {
        double interestRate;
        if (installments == 12) {
            interestRate = 0.20; // 20%
        } else if (installments > 12) {
            interestRate = 0.25; // 25%
        } else {
            interestRate = 0.15; // 15%
        }

        // Calcula el monto total del préstamo incluyendo el interés
        double totalAmount = loanAmount * (1 + interestRate);
        return totalAmount;
    }

    @Override
    public Transaction createLoanTransaction(Account account, double amount, String description, LocalDateTime date) {
        return new Transaction(TransactionType.CREDIT, amount, description, date);
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionService.saveTransaction(transaction);
    }

    @Override
    public void updateAccountBalance(Account account, double amount) {
        account.setBalance(account.getBalance() + amount);
        accountService.saveAccount(account);
    }
    @Override
    public Client getAuthenticatedClient(String email) {
        return clientService.findByEmail(email);
    }

    @Override
    public List<LoanApDTO> getAllLoans() {
        return loanRepository.findAll().stream().map(loan -> new LoanApDTO(loan)).collect(Collectors.toList());
    }
}