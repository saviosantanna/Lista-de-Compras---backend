package com.api.lista_de_compras.model;

// import java.util.UUID;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "app_user")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegister {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    // @Column(name = "username")
    private String username;
    // @Column(name = "name")
    private String name;
    private String email;
    private String password;
}
