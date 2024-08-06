package com.projetoPicPaySimplificado.repositories;

import com.projetoPicPaySimplificado.domain.users.TypeUser;
import com.projetoPicPaySimplificado.domain.users.User;
import com.projetoPicPaySimplificado.dtos.UserDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("should get User successfully from data base")
    void findUserByDocumentCase1() {
        String document = "1234567890";
        UserDto data = new UserDto("Teste","Unitario",
                document, new BigDecimal(10000),"test@gmail.com",
                "password", TypeUser.COMMON);
        this.createUser(data);

        Optional<User> foundedUser = this.userRepository.findUserByDocument(document);
        assertThat(foundedUser.isPresent()).isTrue();
    }

    @Test
    @DisplayName("should not get User successfully from data base when it is empty")
    void findUserByDocumentCase2() {
        String document = "1234567890";
        Optional<User> foundedUser = this.userRepository.findUserByDocument(document);
        assertThat(foundedUser.isEmpty()).isTrue();
    }

    private User createUser(UserDto data){
        User newUser = new User(data);
        this.entityManager.persist(newUser);
        return newUser;
    }
}