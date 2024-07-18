package com.projetoPicPaySimplificado.dtos;

import com.projetoPicPaySimplificado.domain.users.TypeUser;

import java.math.BigDecimal;

public record UserDto(String firstName, String lastName,
                      String document, BigDecimal balance, String email,
                      String password, TypeUser typeUser) {
}
