/*
package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void existTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, is(not(empty())));
    }

    @Test
    public void transactionSave() {
        Transaction transactionTest = new Transaction(TransactionType.CREDIT,1000, "Test transaction", LocalDateTime.now());
        transactionRepository.save(transactionTest);
        assertThat(transactionRepository.findById(transactionTest.getId()), is(notNullValue()));

    }
}*/
