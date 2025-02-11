package com.api.lista_de_compras.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.lista_de_compras.model.UserRegister;

public interface UserRegisterRepository extends JpaRepository<UserRegister,UUID> {
 
    UserRegister findByUsername(String username);

    UserRegister findByEmail(String email);
}
