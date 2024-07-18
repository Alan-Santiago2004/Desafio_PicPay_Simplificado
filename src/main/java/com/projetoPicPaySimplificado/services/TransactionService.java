package com.projetoPicPaySimplificado.services;

import com.projetoPicPaySimplificado.authorizations.AuthorizationResponse;
import com.projetoPicPaySimplificado.domain.transactions.Transaction;
import com.projetoPicPaySimplificado.domain.users.User;
import com.projetoPicPaySimplificado.dtos.TransactionDto;
import com.projetoPicPaySimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction createTransaction(TransactionDto transaction) throws Exception {
        User sender = userService.findUserById(transaction.senderId());
        User receiver = userService.findUserById(transaction.receiverId());

        userService.validateTransaction(sender,transaction.value());

        boolean isAuthorized = this.authorizeTransaction(sender,transaction.value());
        if(!isAuthorized){
            throw new Exception("Transação não autorizada");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.transactionRepository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        this.notificationService.sendNotification(sender,"transação aprovada com sucesso");
        this.notificationService.sendNotification(receiver,"transação recebida com sucesso");

        return newTransaction;
    }

    public boolean authorizeTransaction(User sender, BigDecimal value){
        ResponseEntity<AuthorizationResponse> response = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", AuthorizationResponse.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            AuthorizationResponse authorizationResponse = response.getBody();
            if (authorizationResponse != null && authorizationResponse.getStatus().equals("success") && authorizationResponse.getData() != null && authorizationResponse.getData().isAuthorization()) {
                return true;
            }
        }
        return false;
    }

    public List<Transaction> getAllTransactions(){
        return this.transactionRepository.findAll();
    }
}
