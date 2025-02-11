package com.api.lista_de_compras.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.api.lista_de_compras.model.UserRegister;
import com.api.lista_de_compras.repository.UserRegisterRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRegisterRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username){
        try {
            
        UserRegister user = this.repository.findByUsername(username);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
        } catch (Exception e) {
            System.out.println("Erro no CustomUserDetailsService");
            return null;
        }
      
        
    }
}