package com.projetoPicPaySimplificado.services;

import com.projetoPicPaySimplificado.domain.users.TypeUser;
import com.projetoPicPaySimplificado.domain.users.User;
import com.projetoPicPaySimplificado.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception{
        if(sender.getTypeUser() == TypeUser.MERCHANT){
            throw new Exception("Usuário do tipo logista não está autorizado a realizar transação");
        }
        if(sender.getBalance().compareTo(amount) < 0){
            throw new Exception("Usuário não possui saldo suficiente");
        }
    }

    public User findUserById(Long id) throws Exception{
        return this.repository.findUserById(id).orElseThrow(() -> new Exception("Usuário não encontrado"));
    }
    public void saveUser(User user){
        this.repository.save(user);
    }
}
