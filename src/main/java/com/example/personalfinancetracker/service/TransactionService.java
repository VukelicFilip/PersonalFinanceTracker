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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repo;
    private final TransactionMapper transactionMapper;
    private final BalanceRepository balanceRepository;

    public ResponseEntity<String> create(Transaction transaction, Long userId) {
        transaction.setUserId(userId);
        repo.save(transaction);
        balanceUpdate(userId, transaction.getType(), transaction.getAmount());
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

    public ResponseEntity<Balance> readBalance(Long userId) {
        return ResponseEntity.ok(balanceRepository.findById(userId).get());
    }

    public ResponseEntity<String> update(Long transactionId, Transaction updateInfo, Long userId) {
        Optional<Transaction> transactionOptional = repo.findByIdAndUserId(transactionId, userId);
        if(transactionOptional.isPresent()){
            Transaction toBeUpdated = transactionOptional.get();
            if(toBeUpdated.getAmount()!=null){
                balanceUpdate(userId, toBeUpdated.getType(), toBeUpdated.getAmount().subtract(updateInfo.getAmount()).negate());
            }
            transactionMapper.update(toBeUpdated, updateInfo);
            repo.save(toBeUpdated);
            return ResponseEntity.ok("Transaction updated successfully");
        }
        return ResponseEntity.badRequest().body("Wrong transaction id");
    }

    public ResponseEntity<String> delete(Long transactionId, Long userId) {
        Optional<Transaction> transactionOptional = repo.findByIdAndUserId(transactionId, userId);
        if(transactionOptional.isPresent()){
            balanceUpdate(userId, transactionOptional.get().getType(),transactionOptional.get().getAmount().negate());
            repo.deleteById(transactionId);
            return ResponseEntity.ok("Transaction deleted successfully");
        }
        return ResponseEntity.badRequest().body("Wrong transaction Id");
    }


    private void balanceUpdate(Long userId, TransactionType transactionType, BigDecimal amounth){
        Optional<Balance> balanceOptional = balanceRepository.findById(userId);
        Balance balance;
        if (balanceOptional.isPresent()) {
            balance = balanceOptional.get();
        } else {
            balance = new Balance();
            balance.setUserId(userId);
        }
        if (transactionType.equals(TransactionType.INCOME)) {
            balance.setTotal(balance.getTotal().add(amounth));
            balance.setIncomeTotal(balance.getIncomeTotal().add(amounth));
        } else {
            balance.setTotal(balance.getTotal().subtract(amounth));
            balance.setExpenseTotal(balance.getExpenseTotal().add(amounth));
        }
        balanceRepository.save(balance);
    }

}
