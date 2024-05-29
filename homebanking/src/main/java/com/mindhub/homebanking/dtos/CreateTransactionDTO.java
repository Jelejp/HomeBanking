package com.mindhub.homebanking.dtos;

public record CreateTransactionDTO(double amount, String description, String sourceAccount, String destinationAccount) {
}
