package com.projetoPicPaySimplificado.services;

import com.projetoPicPaySimplificado.domain.users.TypeUser;
import com.projetoPicPaySimplificado.domain.users.User;
import com.projetoPicPaySimplificado.dtos.UserDto;
import com.projetoPicPaySimplificado.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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
    public User createUser(UserDto data){
        User newUser = new User(data);
        this.saveUser(newUser);
        return newUser;
    }
    public void saveUser(User user){
        this.repository.save(user);
    }

    public List<User> getAllUsers(){
        return this.repository.findAll();
    }
}
