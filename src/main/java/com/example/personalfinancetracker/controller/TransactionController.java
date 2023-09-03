package com.example.personalfinancetracker.controller;

import com.example.personalfinancetracker.model.Transaction;
import com.example.personalfinancetracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(path = "/create")
    public ResponseEntity<String> create(@Valid @RequestBody Transaction transaction) {
        return transactionService.create(transaction);
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
