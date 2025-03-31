package com.api.lista_de_compras.controllers.auth;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.api.lista_de_compras.dto.LoginRequestDto;
import com.api.lista_de_compras.dto.RegisterRequestDto;
import com.api.lista_de_compras.dto.ResponseAuthDto;
import com.api.lista_de_compras.model.UserRegister;
import com.api.lista_de_compras.repository.UserRegisterRepository;
import com.api.lista_de_compras.services.EmailService.EmailService;
import com.api.lista_de_compras.services.TelegramService.TelegramService;
import com.api.lista_de_compras.services.TokenService.TokenService;
import com.auth0.jwt.JWT;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final PasswordEncoder passwordEncoder;
    private final UserRegisterRepository authRepository;
    private final TelegramService telegramService;
    private final EmailService emailService;
    private final TokenService tokenService;

    @Autowired
    private TemplateEngine templateEngine;
    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("login")
    public ResponseEntity<ResponseAuthDto> login(@RequestBody LoginRequestDto body, HttpServletResponse response){
        
        try {
            UserRegister user = authRepository.findByUsername(body.username());
            if(passwordEncoder.matches(body.password(),user.getPassword())){
                logger.info("Usuario " + body.username() + " logado com sucesso." );

                String token = this.tokenService.generateToken(user);

                System.out.println("Token Gerado JWT:");
                System.out.println(token);

                return ResponseEntity.ok(new ResponseAuthDto("success", "Login realizado com sucesso!", token));

            } else {
                System.out.println("A senha inserida pelo usuário é inválida.");
                logger.error("A senha inserida pelo usuario é invalida.");
                return ResponseEntity.ok(new ResponseAuthDto("error", "Usuário ou senha inválidos.", null));
            }
        } catch (Exception e) {
            // System.out.println("Usuário " + body.username() + " não encontrado. " + e.getMessage());
            logger.info("Usuario " + body.username() + " não encontrado. " + e.getMessage());
            return ResponseEntity.ok(new ResponseAuthDto("error", "Usuário ou senha inválidos.", null));
        }
    }

    @PostMapping("register")
    public ResponseEntity<ResponseAuthDto> login(@RequestBody RegisterRequestDto body, HttpServletResponse response){
        System.out.println("Entrou no register!!!");
        System.out.println(body);
        try {
            boolean user = authRepository.findByUsername(body.username()) != null;
            boolean emailUser = authRepository.findByEmail(body.email()) != null;
            System.out.println("Valor de user " + user);
            System.out.println("Valor de emailUser " + emailUser);
            if(user){
                System.out.println("Executou user false.");
                return ResponseEntity.ok().body(new ResponseAuthDto("error", "Usuário já cadastrado.", null));
            } else if (emailUser) {
                return ResponseEntity.ok(new ResponseAuthDto("error", "E-mail já cadastrado.", null)); 
                // .body(new ResponseAuthDto("error", "E-mail já cadastrado!")); 
                // body(new ResponseAuthDto("error", "E-mail já cadastrado."));
            } else {
                this.telegramService.sendMessage("Novo usuário '" + body.username() + "' cadastrado");

                Context context = new Context();
                context.setVariable("user", body.name());
                String textEmail = templateEngine.process("emailCadastro", context);

                UserRegister newUser = new UserRegister();
                    newUser.setUsername(body.username());
                    newUser.setName(body.name());
                    newUser.setEmail(body.email());
                    newUser.setPassword(passwordEncoder.encode(body.password()));
                authRepository.save(newUser);
                
                this.emailService.sendEmail(body.email(), "Cadastro realizado", textEmail);

                return ResponseEntity.ok().body(new ResponseAuthDto("success", "Cadastro realizado com sucesso!", null));
            }
            //saasdsadasdas

            // if(user.getPassword().equals(body.password())){
            //     logger.info("Usuario " + body.username() + " logado com sucesso." );
            //     return ResponseEntity.ok(new ResponseAuthDto("success", "Login realizado com sucesso!"));

            // } else {
            //     System.out.println("A senha inserida pelo usuário é inválida.");
            //     logger.error("A senha inserida pelo usuario é invalida.");
            //     return ResponseEntity.ok(new ResponseAuthDto("error", "Usuário ou senha inválidos."));
            // }
            // if(passwordEncoder.matches(body.password(), user.getPassword())){
            //     System.out.println(passwordEncoder.matches(body.password(), user.getPassword()));
            //     return ResponseEntity.ok().body("Login realizado com sucesso!");
            // }
        } catch (Exception e) {
            // System.out.println("Usuário " + body.username() + " não encontrado. " + e.getMessage());
            logger.info("Erro ao acessar repository. "+ e.getMessage());
            //Verificar lógica para envio de mensagem via Telegram.
            return ResponseEntity.ok(new ResponseAuthDto("warning", "Não foi possível conectar ao servidor. Tente novamente mais tarde.", null));
        }
    }

    @PostMapping("validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestBody String token){

        String user = JWT.decode(token).getClaim("user").toString();
        boolean valid = JWT.decode(token).getExpiresAt().getTime() > new Date().getTime();
        if(valid){
            System.out.println("Token de " + user + " ainda está válido.");
            return ResponseEntity.ok(valid);
        } else {
            return null;
        }

    }

}
