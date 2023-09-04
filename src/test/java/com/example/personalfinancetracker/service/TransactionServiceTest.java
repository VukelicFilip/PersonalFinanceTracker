package com.example.personalfinancetracker.service;

import com.example.personalfinancetracker.model.Transaction;
import com.example.personalfinancetracker.model.TransactionType;
import com.example.personalfinancetracker.model.User;
import com.example.personalfinancetracker.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class TransactionServiceTest {
    @InjectMocks
    TransactionService transactionService;
    TransactionRepository transactionRepository = mock(TransactionRepository.class);


    Transaction mockTransaction = new Transaction();
    User mockUser = new User();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser.setPassword("pass1234");
        mockUser.setUserId(1L);

        mockTransaction.setAmount(new BigDecimal(1000.00));
        mockTransaction.setDescription("description");
        mockTransaction.setId(1L);
        mockTransaction.setType(TransactionType.INCOME);
        mockTransaction.setUserId(mockUser.getUserId());
    }

    @Test
    @DisplayName("Test create transaction")
    void testCreateTransaction() {

        String expectedResponse = "Transaction created successfully";

        ResponseEntity<String> response = transactionService.create(mockTransaction, mockUser.getUserId());
        try {
            assertEquals(expectedResponse, response.getBody());
            System.out.println("Test done - Create transaction");

        } catch (AssertionError e) {
            System.out.println("Assertion failed: " + e.getMessage());
            throw e;
        }
    }
}
