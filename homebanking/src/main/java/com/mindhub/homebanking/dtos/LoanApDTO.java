package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanApDTO {

        //PROPIEDADES
        private long id;

        private String name;

        private double maxAmount;

        private List<Integer> availableIntallments;


        //CONSTRUCTOR
        public LoanApDTO(Loan loan) {
            this.id = loan.getId();
            this.name = loan.getName();
            this.maxAmount = loan.getMaxAmount();
            this.availableIntallments = loan.getPayments();
        }

        //GETTERS

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getAvailableIntallments() {
        return availableIntallments;
    }
}