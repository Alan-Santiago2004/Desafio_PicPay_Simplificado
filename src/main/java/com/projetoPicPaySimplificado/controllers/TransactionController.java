package com.projetoPicPaySimplificado.controllers;

import com.projetoPicPaySimplificado.domain.transactions.Transaction;
import com.projetoPicPaySimplificado.dtos.TransactionDto;
import com.projetoPicPaySimplificado.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDto transaction) throws Exception {
        Transaction newTransaction = this.transactionService.createTransaction(transaction);
        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransaction(){
        List<Transaction> transactionList = this.transactionService.getAllTransactions();
        return new ResponseEntity<>(transactionList,HttpStatus.OK);
    }
}
