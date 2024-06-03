package com.mindhub.homebanking.dtos;

public record LoanApplicationDTO(long loandId, double amount, int installments, String destinationAccount) {
}
