package com.projetoPicPaySimplificado.services;

import com.projetoPicPaySimplificado.domain.transactions.Transaction;
import com.projetoPicPaySimplificado.domain.users.User;
import com.projetoPicPaySimplificado.dtos.TransactionDto;
import com.projetoPicPaySimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction createTransaction(TransactionDto transaction) throws Exception {
        User sender = userService.findUserById(transaction.senderId());
        User receiver = userService.findUserById(transaction.receiverId());

        //validate transaction
        userService.validateTransaction(sender,transaction.value());
        this.authorizationService.authorizeTransaction(sender,transaction.value());

        //set transaction
        Transaction newTransaction = new Transaction();
        this.transactionBody(transaction,newTransaction,sender,receiver);
        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        //save transaction
        this.transactionRepository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        this.notificationService.sendNotification(sender,"transação aprovada com sucesso");
        this.notificationService.sendNotification(receiver,"transação recebida com sucesso");

        return newTransaction;
    }

    public void transactionBody(TransactionDto transactionDto,Transaction newTransaction,User sender,User receiver){
        newTransaction.setAmount(transactionDto.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());
    }

    public List<Transaction> getAllTransactions(){
        return this.transactionRepository.findAll();
    }
}
