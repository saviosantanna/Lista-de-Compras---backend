package com.api.lista_de_compras.dto;

public record RegisterRequestDto(
    String username,
    String name,
    String email,
    String password
) {
    
}
