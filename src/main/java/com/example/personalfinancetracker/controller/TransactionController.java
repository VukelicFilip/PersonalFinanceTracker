package com.example.personalfinancetracker.controller;

import com.example.personalfinancetracker.model.Balance;
import com.example.personalfinancetracker.model.Transaction;
import com.example.personalfinancetracker.model.TransactionDTO;
import com.example.personalfinancetracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    //staviti status 201 created
    @PostMapping(path = "/create")
    public ResponseEntity<String> create(@Valid @RequestBody Transaction transaction, @RequestParam Long userId) {
        return transactionService.create(transaction,userId);
    }

    //By REST convention, the best solution would be to have 3 controllers for these 3 read methods
    //in order to avoid additional naming but to keep this example simple I opted for different approach
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
