package com.projetoPicPaySimplificado.services;

import com.projetoPicPaySimplificado.domain.users.TypeUser;
import com.projetoPicPaySimplificado.domain.users.User;
import com.projetoPicPaySimplificado.dtos.TransactionDto;
import com.projetoPicPaySimplificado.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class TransactionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private TransactionRepository transactionRepository;

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should create a transaction when everything is OK")
    void createTransactionCase1() throws Exception {
        User sender = new User(1L,"Alan","Santiago",
                "1234567890","alan@gmail.com", "password",
                new BigDecimal(100), TypeUser.COMMON);
        User receiver = new User(2L,"Carlos","Santana",
                "1234567899","carlos@gmail.com", "password",
                new BigDecimal(100), TypeUser.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        when(authorizationService.authorizeTransaction(any(),any())).thenReturn(true);

        TransactionDto request = new TransactionDto(new BigDecimal(100),1L,2L);
        transactionService.createTransaction(request);

        verify(transactionRepository,times(1)).save(any());

        verify(userService,times(1)).saveUser(sender);
        assertThat(sender.getBalance()).isEqualTo(new BigDecimal(0));

        verify(userService,times(1)).saveUser(receiver);
        assertThat(receiver.getBalance()).isEqualTo(new BigDecimal(200));
    }

    @Test
    @DisplayName("Should throw an Exception when transaction is not allowed")
    void createTransactionCase2() {
    }
}