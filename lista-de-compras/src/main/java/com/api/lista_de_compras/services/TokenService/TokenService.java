package com.api.lista_de_compras.services.TokenService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.api.lista_de_compras.model.UserRegister;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    String secret;

    private final static Logger logger = LoggerFactory.getLogger(TokenService.class);

    public String generateToken(UserRegister user){
        
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                            .withIssuer("lista-de-compras")
                            // .withSubject(user.getUsername())
                            .withClaim("user", user.getUsername())
                            // .withClaim("nome Claim", "teste nome Clain")
                            .withExpiresAt(this.expirationDate("short"))
                            .sign(algorithm);

            logger.info("JWT Criado:" + token);
            return token;
        } catch (JWTCreationException | IllegalArgumentException e) {
           
            logger.info("Erro ao gerar JWT para usuário: " + user.getUsername());
            // System.out.println("Deu zika no JWT.");
            return "Erro ao gerar JWT.";
        }

    }

    public Instant expirationDate(String type){
       if(type.equals("short")){
            return LocalDateTime.now().plusMinutes(1).toInstant(ZoneOffset.of("-03:00"));
        } else {
            return LocalDateTime.now().plusMinutes(3).toInstant(ZoneOffset.of("-03:00"));
        }
        
        // return null;
    }

}
