package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanApDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

public interface LoanService {

    ResponseEntity<String> applyForLoan(LoanApplicationDTO loanApplicationDTO, Authentication authentication);

    Client getAuthenticatedClient(String email);

    Loan getLoanById(Long id);

    ResponseEntity<String> validateLoanApplication(LoanApplicationDTO loanApplicationDTO);

    Account getAccountByNumber(String accountNumber);

    boolean accountBelongsToClient(Account account, Client client);

    double calculateTotalAmount(double loanAmount, int installments);

    Transaction createLoanTransaction(Account account, double amount, String description, LocalDateTime date);

    void saveTransaction(Transaction transaction);

    void updateAccountBalance(Account account, double amount);

    List<LoanApDTO> getAllLoans();
}