package com.example.personalfinancetracker.service;

import com.example.personalfinancetracker.model.Balance;
import com.example.personalfinancetracker.model.Transaction;
import com.example.personalfinancetracker.model.TransactionDTO;
import com.example.personalfinancetracker.model.TransactionType;
import com.example.personalfinancetracker.repository.BalanceRepository;
import com.example.personalfinancetracker.repository.TransactionRepository;
import com.example.personalfinancetracker.util.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repo;
    private final TransactionMapper transactionMapper;
    private final BalanceRepository balanceRepository;

    public ResponseEntity<String> create(Transaction transaction, Long userId) {
        repo.save(transaction);
        Balance balance = balanceRepository.findById(userId).get();
        if (transaction.getType().equals(TransactionType.INCOME)) {
            balance.setTotal(balance.getTotal().add(transaction.getAmount()));
            balance.setIncomeTotal(balance.getIncomeTotal().add(transaction.getAmount()));
        } else {
            balance.setTotal(balance.getTotal().subtract(transaction.getAmount()));
            balance.setIncomeTotal(balance.getExpenseTotal().add(transaction.getAmount()));
        }
        return ResponseEntity.ok("Transaction created successfully");
    }

    public ResponseEntity<List<TransactionDTO>> readAll(Long userId) {
        return ResponseEntity.ok(repo.findByUserId(userId));
    }

    public ResponseEntity<List<TransactionDTO>> readIncomeTransactions(Long userId) {
        return ResponseEntity.ok(repo.findByUserIdAndType(userId, TransactionType.INCOME));
    }

    public ResponseEntity<List<TransactionDTO>> readExpenseTransactions(Long userId) {
        return ResponseEntity.ok(repo.findByUserIdAndType(userId, TransactionType.EXPENSE));
    }


    public ResponseEntity<String> update(Long transactionId, Transaction updateInfo) {
        Transaction toBeUpdated = repo.findById(transactionId).get();
        transactionMapper.update(toBeUpdated, updateInfo);
        repo.save(toBeUpdated);
        return ResponseEntity.ok("Transaction updated successfully");
    }

    public ResponseEntity<String> delete(Long transactionId) {
        repo.deleteByIdAndUserId(transactionId, );
        return ResponseEntity.ok("Transaction deleted successfully");
    }

}
