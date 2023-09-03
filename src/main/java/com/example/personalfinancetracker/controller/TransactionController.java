package com.example.personalfinancetracker.controller;

import com.example.personalfinancetracker.model.Balance;
import com.example.personalfinancetracker.model.Transaction;
import com.example.personalfinancetracker.model.TransactionDTO;
import com.example.personalfinancetracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(path = "/create")
    public ResponseEntity<String> create(@Valid @RequestBody Transaction transaction, @RequestParam Long userId) {
        return transactionService.create(transaction,userId);
    }

    @GetMapping(path = "/read-all")
    public ResponseEntity<List<TransactionDTO>> readAll(@RequestParam Long userId){
        return transactionService.readAll(userId);
    }

    @GetMapping(path = "/read-income")
    public ResponseEntity<List<TransactionDTO>> readIncome(@RequestParam Long userId){
        return transactionService.readIncomeTransactions(userId);
    }

    @GetMapping(path = "/read-expense")
    public ResponseEntity<List<TransactionDTO>> readExpense(@RequestParam Long userId){
        return transactionService.readExpenseTransactions(userId);
    }

    @GetMapping(path = "/read-balance")
    public ResponseEntity<Balance> readBalance(@RequestParam Long userId){
        return transactionService.readBalance(userId);
    }
    @PutMapping(path = "/update")
    public ResponseEntity<String> update(@RequestParam Long transactionId,@Valid @RequestBody Transaction updateInfo){
        return transactionService.update(transactionId, updateInfo);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> delete(@RequestParam Long transactionId){
        return transactionService.delete(transactionId);
    }


}
