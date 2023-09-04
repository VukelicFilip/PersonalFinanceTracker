package com.example.personalfinancetracker.controller;

import com.example.personalfinancetracker.model.Balance;
import com.example.personalfinancetracker.model.Transaction;
import com.example.personalfinancetracker.model.TransactionDTO;
import com.example.personalfinancetracker.service.AuthService;
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
    private final AuthService authService;

    //staviti status 201 created
    @PostMapping(path = "/create")
    public ResponseEntity<String> create(@Valid @RequestBody Transaction transaction) {
        return transactionService.create(transaction,authService.getJwtUserId());
    }

    @GetMapping(path = "/read-all")
    public ResponseEntity<List<TransactionDTO>> readAll(){
        return transactionService.readAll(authService.getJwtUserId());
    }

    @GetMapping(path = "/read-income")
    public ResponseEntity<List<TransactionDTO>> readIncome(){
        return transactionService.readIncomeTransactions(authService.getJwtUserId());
    }

    @GetMapping(path = "/read-expense")
    public ResponseEntity<List<TransactionDTO>> readExpense(){
        return transactionService.readExpenseTransactions(authService.getJwtUserId());
    }

    @GetMapping(path = "/read-balance")
    public ResponseEntity<Balance> readBalance(){
        return transactionService.readBalance(authService.getJwtUserId());
    }
    @PutMapping(path = "/update")
    public ResponseEntity<String> update(@RequestParam Long transactionId,@Valid @RequestBody Transaction updateInfo){
        return transactionService.update(transactionId, updateInfo, authService.getJwtUserId());
    }

    //OBAVEZNO PROMENITI OVO SA USER ID
    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> delete(@RequestParam Long transactionId){
        return transactionService.delete(transactionId, 69l);
    }


}
